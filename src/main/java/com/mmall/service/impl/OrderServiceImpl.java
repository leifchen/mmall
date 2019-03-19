package com.mmall.service.impl;

import com.alipay.api.AlipayResponse;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.alipay.demo.trade.config.Configs;
import com.alipay.demo.trade.model.ExtendParams;
import com.alipay.demo.trade.model.GoodsDetail;
import com.alipay.demo.trade.model.builder.AlipayTradePrecreateRequestBuilder;
import com.alipay.demo.trade.model.result.AlipayF2FPrecreateResult;
import com.alipay.demo.trade.service.AlipayTradeService;
import com.alipay.demo.trade.service.impl.AlipayTradeServiceImpl;
import com.alipay.demo.trade.utils.ZxingUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mmall.common.Const;
import com.mmall.common.JsonResult;
import com.mmall.dao.*;
import com.mmall.pojo.*;
import com.mmall.service.OrderService;
import com.mmall.util.BigDecimalUtils;
import com.mmall.util.DateTimeUtils;
import com.mmall.util.FtpUtils;
import com.mmall.util.PropertiesUtils;
import com.mmall.vo.OrderItemVO;
import com.mmall.vo.OrderProductVO;
import com.mmall.vo.OrderVO;
import com.mmall.vo.ShippingVO;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

/**
 * 订单Service的实现类
 * <p>
 * @Author LeifChen
 * @Date 2019-03-08
 */
@Service("orderService")
public class OrderServiceImpl implements OrderService {

    private final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderItemMapper orderItemMapper;
    @Autowired
    private CartMapper cartMapper;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private ShippingMapper shippingMapper;
    @Autowired
    private PayInfoMapper payInfoMapper;

    @Override
    public JsonResult<OrderVO> create(Integer userId, Integer shippingId) {
        // 从购物车中获取数据
        List<Cart> cartList = cartMapper.selectCheckedCartByUserId(userId);
        // 计算订单总价
        JsonResult response = getCartOrderItem(userId, cartList);
        if (!response.isSuccess()) {
            return response;
        }
        List<OrderItem> orderItemList = (List<OrderItem>) response.getData();
        BigDecimal payment = getOrderTotalPrice(orderItemList);
        // 生成订单
        Order order = assembleOrder(userId, shippingId, payment);
        if (order == null) {
            return JsonResult.error("生成订单错误");
        }
        if (CollectionUtils.isEmpty(orderItemList)) {
            return JsonResult.error("购物车为空");
        }
        for (OrderItem orderItem : orderItemList) {
            orderItem.setOrderNo(order.getOrderNo());
        }
        // Mybatis 批量插入
        orderItemMapper.batchInsert(orderItemList);

        // 减少产品库存
        reduceProductStock(orderItemList);
        // 清空购物车
        clearCart(cartList);

        OrderVO orderVO = assembleOrderVO(order, orderItemList);
        return JsonResult.success(orderVO);
    }

    @Override
    public JsonResult<String> cancel(Integer userId, Long orderNo) {
        Order order = orderMapper.selectByUserIdAndOrderNo(userId, orderNo);
        if (order == null) {
            return JsonResult.error("该用户此订单不存在");
        }
        if (order.getStatus() != Const.OrderStatusEnum.NO_PAY.getCode()) {
            return JsonResult.error("已付款，无法取消订单");
        }
        Order updateOrder = new Order();
        updateOrder.setId(order.getId());
        updateOrder.setStatus(Const.OrderStatusEnum.CANCELED.getCode());

        int rowCount = orderMapper.updateByPrimaryKeySelective(updateOrder);
        if (rowCount > 0) {
            return JsonResult.success();
        }
        return JsonResult.error("取消订单失败");
    }

    @Override
    public JsonResult getOrderCartProduct(Integer userId) {
        OrderProductVO orderProductVO = new OrderProductVO();
        // 从购物车中获取数据
        List<Cart> cartList = cartMapper.selectCheckedCartByUserId(userId);
        JsonResult response = getCartOrderItem(userId, cartList);
        if (!response.isSuccess()) {
            return response;
        }
        List<OrderItem> orderItemList = (List<OrderItem>) response.getData();
        List<OrderItemVO> orderItemVOList = Lists.newArrayList();
        BigDecimal payment = BigDecimal.ZERO;
        for (OrderItem orderItem : orderItemList) {
            payment = BigDecimalUtils.add(payment.doubleValue(), orderItem.getTotalPrice().doubleValue());
            orderItemVOList.add(assembleOrderItemVO(orderItem));
        }
        orderProductVO.setProductTotalPrice(payment);
        orderProductVO.setOrderItemVOList(orderItemVOList);
        orderProductVO.setIamgeHost(PropertiesUtils.getProperty("ftp.server.http.prefix"));
        return JsonResult.success(orderProductVO);
    }

    @Override
    public JsonResult getOrderDetail(Integer userId, Long orderNo) {
        Order order = orderMapper.selectByUserIdAndOrderNo(userId, orderNo);
        if (order != null) {
            List<OrderItem> orderItemList = orderItemMapper.getByOrderNoAndUserId(orderNo, userId);
            OrderVO orderVO = assembleOrderVO(order, orderItemList);
            return JsonResult.success(orderVO);
        }
        return JsonResult.error("没有找到该订单");
    }

    @Override
    public JsonResult<PageInfo> getOrderList(Integer userId, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Order> orderList = orderMapper.selectByUserId(userId);
        List<OrderVO> orderVOList = assembleOrderVOList(orderList, userId);
        PageInfo pageInfo = new PageInfo(orderList);
        pageInfo.setList(orderVOList);
        return JsonResult.success(pageInfo);
    }

    @Override
    public JsonResult<PageInfo> manageList(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Order> orderList = orderMapper.selectAll();
        List<OrderVO> orderVOList = assembleOrderVOList(orderList, null);
        PageInfo pageInfo = new PageInfo(orderList);
        pageInfo.setList(orderVOList);
        return JsonResult.success(pageInfo);
    }

    @Override
    public JsonResult<OrderVO> manageDetail(Long orderNo) {
        Order order = orderMapper.selectByOrderNo(orderNo);
        if (order != null) {
            List<OrderItem> orderItemList = orderItemMapper.getByOrderNo(orderNo);
            OrderVO orderVO = assembleOrderVO(order, orderItemList);
            return JsonResult.success(orderVO);
        }
        return JsonResult.error("订单不存在");
    }

    @Override
    public JsonResult<PageInfo> manageSearch(Long orderNo, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        Order order = orderMapper.selectByOrderNo(orderNo);
        if (order != null) {
            List<OrderItem> orderItemList = orderItemMapper.getByOrderNo(orderNo);
            OrderVO orderVO = assembleOrderVO(order, orderItemList);
            PageInfo pageInfo = new PageInfo(Lists.newArrayList(order));
            pageInfo.setList(Lists.newArrayList(orderVO));
            return JsonResult.success(pageInfo);
        }
        return JsonResult.error("订单不存在");
    }

    @Override
    public JsonResult<String> manageSendGoods(Long orderNo) {
        Order order = orderMapper.selectByOrderNo(orderNo);
        if (order != null) {
            if (order.getStatus() == Const.OrderStatusEnum.PAID.getCode()) {
                order.setStatus(Const.OrderStatusEnum.SHIPPED.getCode());
                order.setSendTime(new Date());
                int rowCount = orderMapper.updateByPrimaryKeySelective(order);
                if (rowCount > 0) {
                    return JsonResult.success("发货成功");
                } else {
                    return JsonResult.error("发货失败");
                }
            } else {
                return JsonResult.error("该订单未付款，无法发货");
            }
        }
        return JsonResult.error("订单不存在");
    }

    /**
     * 组装订单VO列表
     * @param orderList
     * @param userId
     * @return
     */
    private List<OrderVO> assembleOrderVOList(List<Order> orderList, Integer userId) {
        List<OrderVO> orderVOList = Lists.newArrayList();
        for (Order order : orderList) {
            List<OrderItem> orderItemList;
            if (userId == null) {
                orderItemList = orderItemMapper.getByOrderNo(order.getOrderNo());
            } else {
                orderItemList = orderItemMapper.getByOrderNoAndUserId(order.getOrderNo(), userId);
            }
            OrderVO orderVO = assembleOrderVO(order, orderItemList);
            orderVOList.add(orderVO);
        }
        return orderVOList;
    }

    /**
     * 获取购物车商品明细
     * @param userId   用户id
     * @param cartList 购物车
     * @return
     */
    private JsonResult getCartOrderItem(Integer userId, List<Cart> cartList) {
        List<OrderItem> orderItemList = Lists.newArrayList();
        if (CollectionUtils.isEmpty(cartList)) {
            return JsonResult.error("购物车为空");
        }

        // 校验购物车的数据，包括商品的状态和数量
        for (Cart cartItem : cartList) {
            OrderItem orderItem = new OrderItem();
            Product product = productMapper.selectByPrimaryKey(cartItem.getProductId());
            if (Const.ProductStatusEnum.ON_SALE.getCode() != product.getStatus()) {
                return JsonResult.error("商品" + product.getName() + "不是在线售卖状态");
            }
            // 校验库存
            if (cartItem.getQuantity() > product.getStock()) {
                return JsonResult.error("商品" + product.getName() + "库存不足");
            }
            orderItem.setUserId(userId);
            orderItem.setProductId(product.getId());
            orderItem.setProductName(product.getName());
            orderItem.setProductImage(product.getMainImage());
            orderItem.setCurrentUnitPrice(product.getPrice());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setTotalPrice(BigDecimalUtils.mul(product.getPrice().doubleValue(), cartItem.getQuantity().doubleValue()));
            orderItemList.add(orderItem);
        }
        return JsonResult.success(orderItemList);
    }

    /**
     * 计算订单总价
     * @param orderItemList 订单明细
     * @return
     */
    private BigDecimal getOrderTotalPrice(List<OrderItem> orderItemList) {
        BigDecimal payment = BigDecimal.ZERO;
        for (OrderItem orderItem : orderItemList) {
            payment = BigDecimalUtils.add(payment.doubleValue(), orderItem.getTotalPrice().doubleValue());
        }
        return payment;
    }

    /**
     * 组装订单信息
     * @param userId     用户id
     * @param shippingId 收货地址id
     * @param payment    订单总价
     * @return
     */
    private Order assembleOrder(Integer userId, Integer shippingId, BigDecimal payment) {
        Order order = new Order();
        long orderNo = generateOrderNo();
        order.setOrderNo(orderNo);
        order.setStatus(Const.OrderStatusEnum.NO_PAY.getCode());
        order.setPostage(0);
        order.setPaymentType(Const.PaymentTypeEnum.ONLINE_PAY.getCode());
        order.setPayment(payment);
        order.setUserId(userId);
        order.setShippingId(shippingId);
        int rowCount = orderMapper.insert(order);
        if (rowCount > 0) {
            return order;
        }
        return null;
    }

    /**
     * 生成订单号
     * @return
     */
    private long generateOrderNo() {
        long currentTime = System.currentTimeMillis();
        return currentTime + new Random().nextInt(100);
    }

    /**
     * 减少商品库存
     * @param orderItemList 订单明细
     */
    private void reduceProductStock(List<OrderItem> orderItemList) {
        for (OrderItem orderItem : orderItemList) {
            Product product = productMapper.selectByPrimaryKey(orderItem.getProductId());
            product.setStock(product.getStock() - orderItem.getQuantity());
            productMapper.updateByPrimaryKeySelective(product);
        }
    }

    /**
     * 清空购物车
     * @param cartList 购物车
     */
    private void clearCart(List<Cart> cartList) {
        for (Cart cart : cartList) {
            cartMapper.deleteByPrimaryKey(cart.getId());
        }
    }

    /**
     * 组装订单VO
     * @param order         订单
     * @param orderItemList 订单明细
     * @return
     */
    private OrderVO assembleOrderVO(Order order, List<OrderItem> orderItemList) {
        OrderVO orderVO = new OrderVO();
        orderVO.setOrderNo(order.getOrderNo());
        orderVO.setPayment(order.getPayment());
        orderVO.setPaymentType(order.getPaymentType());
        orderVO.setPaymentTypeDesc(Const.PaymentTypeEnum.codeOf(order.getPaymentType()).getValue());
        orderVO.setPostage(order.getPostage());
        orderVO.setStatus(order.getStatus());
        orderVO.setStatusDesc(Const.OrderStatusEnum.codeOf(order.getStatus()).getValue());
        orderVO.setShippingId(order.getShippingId());
        Shipping shipping = shippingMapper.selectByPrimaryKey(order.getShippingId());
        if (shipping != null) {
            orderVO.setReceiverName(shipping.getReceiverName());
            orderVO.setShippingVO(assembleShippingVO(shipping));
        }
        orderVO.setPaymentTime(DateTimeUtils.dateToStr(order.getPaymentTime()));
        orderVO.setSendTime(DateTimeUtils.dateToStr(order.getSendTime()));
        orderVO.setEndTime(DateTimeUtils.dateToStr(order.getEndTime()));
        orderVO.setCreateTime(DateTimeUtils.dateToStr(order.getCreateTime()));
        orderVO.setCloseTime(DateTimeUtils.dateToStr(order.getCloseTime()));

        orderVO.setImageHost(PropertiesUtils.getProperty("ftp.server.http.prefix"));

        List<OrderItemVO> orderItemVOList = Lists.newArrayList();
        for (OrderItem orderItem : orderItemList) {
            OrderItemVO orderItemVO = assembleOrderItemVO(orderItem);
            orderItemVOList.add(orderItemVO);
        }
        orderVO.setOrderItemVOList(orderItemVOList);
        return orderVO;
    }

    /**
     * 组装订单明细VO
     * @param orderItem 订单明细
     * @return
     */
    private OrderItemVO assembleOrderItemVO(OrderItem orderItem) {
        OrderItemVO orderItemVO = new OrderItemVO();
        BeanUtils.copyProperties(orderItem, orderItemVO);
        return orderItemVO;
    }

    /**
     * 组装收货地址信息VO
     * @param shipping 收货地址信息
     * @return
     */
    private ShippingVO assembleShippingVO(Shipping shipping) {
        ShippingVO shippingVO = new ShippingVO();
        BeanUtils.copyProperties(shipping, shippingVO);
        return shippingVO;
    }

    @Override
    public JsonResult pay(Long orderNo, Integer userId, String path) {
        Map<String, String> resultMap = Maps.newHashMap();
        Order order = orderMapper.selectByUserIdAndOrderNo(userId, orderNo);
        if (order == null) {
            return JsonResult.error("用户没有该订单");
        }
        resultMap.put("orderNo", String.valueOf(order.getOrderNo()));

        // (必填) 商户网站订单系统中唯一订单号，64个字符以内，只能包含字母、数字、下划线，
        String outTradeNo = order.getOrderNo().toString();

        // (必填) 订单标题，粗略描述用户的支付目的。如“xxx品牌xxx门店当面付扫码消费”
        String subject = "mall扫码支付" + outTradeNo;

        // (必填) 订单总金额，单位为元，不能超过1亿元
        // 如果同时传入了【打折金额】,【不可打折金额】,【订单总金额】三者,则必须满足如下条件:【订单总金额】=【打折金额】+【不可打折金额】
        String totalAmount = order.getPayment().toString();

        // (可选) 订单不可打折金额，可以配合商家平台配置折扣活动，如果酒水不参与打折，则将对应金额填写至此字段
        // 如果该值未传入,但传入了【订单总金额】,【打折金额】,则该值默认为【订单总金额】-【打折金额】
        String undiscountableAmount = "0";

        // 卖家支付宝账号ID，用于支持一个签约账号下支持打款到不同的收款账号，(打款到sellerId对应的支付宝账号)
        // 如果该字段为空，则默认为与支付宝签约的商户的PID，也就是appid对应的PID
        String sellerId = "";

        // 订单描述，可以对交易或商品进行一个详细地描述，比如填写"购买商品2件共15.00元"
        String body = "购买商品共" + totalAmount + "元";

        // 商户操作员编号，添加此参数可以为商户操作员做销售统计
        String operatorId = "test_operator_id";

        // (必填) 商户门店编号，通过门店号和商家后台可以配置精准到门店的折扣信息，详询支付宝技术支持
        String storeId = "test_store_id";

        // 业务扩展参数，目前可添加由支付宝分配的系统商编号(通过setSysServiceProviderId方法)，详情请咨询支付宝技术支持
        ExtendParams extendParams = new ExtendParams();
        extendParams.setSysServiceProviderId("2088100200300400500");

        // 支付超时，定义为120分钟
        String timeoutExpress = "120m";

        // 商品明细列表，需填写购买商品详细信息，
        List<GoodsDetail> goodsDetailList = new ArrayList<>();
        List<OrderItem> orderItemList = orderItemMapper.getByOrderNoAndUserId(orderNo, userId);
        for (OrderItem orderItem : orderItemList) {
            GoodsDetail goods = GoodsDetail.newInstance(orderItem.getProductId().toString(),
                    orderItem.getProductName(),
                    BigDecimalUtils.mul(orderItem.getCurrentUnitPrice().doubleValue(), 100).longValue(),
                    orderItem.getQuantity());
            goodsDetailList.add(goods);
        }

        // 创建扫码支付请求builder，设置请求参数
        AlipayTradePrecreateRequestBuilder builder = new AlipayTradePrecreateRequestBuilder()
                .setSubject(subject).setTotalAmount(totalAmount).setOutTradeNo(outTradeNo)
                .setUndiscountableAmount(undiscountableAmount).setSellerId(sellerId).setBody(body)
                .setOperatorId(operatorId).setStoreId(storeId).setExtendParams(extendParams)
                .setTimeoutExpress(timeoutExpress)
                // 支付宝服务器主动通知商户服务器里指定的页面http路径,根据需要设置
                .setNotifyUrl(PropertiesUtils.getProperty("alipay.callback.url"))
                .setGoodsDetailList(goodsDetailList);

        Configs.init("zfbinfo.properties");
        AlipayTradeService tradeService = new AlipayTradeServiceImpl.ClientBuilder().build();
        AlipayF2FPrecreateResult result = tradeService.tradePrecreate(builder);
        switch (result.getTradeStatus()) {
            case SUCCESS:
                logger.info("支付宝预下单成功: )");

                AlipayTradePrecreateResponse response = result.getResponse();
                dumpResponse(response);

                File folder = new File(path);
                if (!folder.exists()) {
                    folder.setWritable(true);
                    folder.mkdirs();
                }

                String qrPath = String.format(path + "/qr-%s.png", response.getOutTradeNo());
                String qrFileName = String.format("qr-%s.png", response.getOutTradeNo());
                ZxingUtils.getQRCodeImge(response.getQrCode(), 256, qrPath);

                File targetFile = new File(path, qrFileName);
                try {
                    FtpUtils.uploadFile(Lists.newArrayList(targetFile));
                } catch (IOException e) {
                    logger.error("上传二维码异常", e);
                }
                logger.info("qrPath:" + qrPath);
                String qrUrl = PropertiesUtils.getProperty("ftp.server.http.prefix") + targetFile.getName();
                resultMap.put("qrUrl", qrUrl);
                return JsonResult.success(resultMap);

            case FAILED:
                logger.error("支付宝预下单失败!!!");
                return JsonResult.error("支付宝预下单失败!!!");

            case UNKNOWN:
                logger.error("系统异常，预下单状态未知!!!");
                return JsonResult.error("系统异常，预下单状态未知!!!");

            default:
                logger.error("不支持的交易状态，交易返回异常!!!");
                return JsonResult.error("不支持的交易状态，交易返回异常!!!");
        }
    }

    /**
     * 简单打印应答
     */
    private void dumpResponse(AlipayResponse response) {
        if (response != null) {
            logger.info(String.format("code:%s, msg:%s", response.getCode(), response.getMsg()));
            if (StringUtils.isNotEmpty(response.getSubCode())) {
                logger.info(String.format("subCode:%s, subMsg:%s", response.getSubCode(),
                        response.getSubMsg()));
            }
            logger.info("body:" + response.getBody());
        }
    }

    @Override
    public JsonResult alipayCallback(Map<String, String> params) {
        Long orderNo = Long.valueOf(params.get("out_trade_no"));
        String tradeNo = params.get("trade_no");
        String tradeStatus = params.get("trade_status");
        Order order = orderMapper.selectByOrderNo(orderNo);
        if (order == null) {
            return JsonResult.error("非mall的订单，回调忽略");
        }
        if (order.getStatus() >= Const.OrderStatusEnum.PAID.getCode()) {
            return JsonResult.error("支付宝重复调用");
        }
        if (Const.AlipayCallback.TRADE_STATUS_TRADE_SUCCESS.equals(tradeStatus)) {
            order.setPaymentTime(DateTimeUtils.strToDate(params.get("gmt_payment")));
            order.setStatus(Const.OrderStatusEnum.PAID.getCode());
            orderMapper.updateByPrimaryKeySelective(order);
        }

        PayInfo payInfo = new PayInfo();
        payInfo.setUserId(order.getUserId());
        payInfo.setOrderNo(order.getOrderNo());
        payInfo.setPayPlatform(Const.PayPlatformEnum.ALIPAY.getCode());
        payInfo.setPlatformNumber(tradeNo);
        payInfo.setPlatformStatus(tradeStatus);
        payInfoMapper.insert(payInfo);

        return JsonResult.success();
    }

    @Override
    public JsonResult queryOrderPayStatus(Integer userId, Long orderNo) {
        Order order = orderMapper.selectByUserIdAndOrderNo(userId, orderNo);
        if (order == null) {
            return JsonResult.error("用户没有该订单");
        }
        if (order.getStatus() >= Const.OrderStatusEnum.PAID.getCode()) {
            return JsonResult.success("该订单已支付");
        }
        return JsonResult.error("该订单尚未付款");
    }
}
