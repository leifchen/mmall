package com.mmall.controller.portal;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.demo.trade.config.Configs;
import com.google.common.collect.Maps;
import com.mmall.common.Const;
import com.mmall.common.JsonResult;
import com.mmall.controller.common.UserCheck;
import com.mmall.pojo.User;
import com.mmall.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 订单Controller
 * <p>
 * @Author LeifChen
 * @Date 2019-03-08
 */
@Slf4j
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private UserCheck userCheck;

    @RequestMapping(value = "/create.do")
    public JsonResult create(HttpServletRequest httpServletRequest, Integer shippingId) {
        JsonResult response = userCheck.checkLogin(httpServletRequest);
        if (response.isSuccess()) {
            User user = (User) response.getData();
            return orderService.create(user.getId(), shippingId);
        } else {
            return response;
        }
    }

    @RequestMapping(value = "/cancel.do")
    public JsonResult cancel(HttpServletRequest httpServletRequest, Long orderNo) {
        JsonResult response = userCheck.checkLogin(httpServletRequest);
        if (response.isSuccess()) {
            User user = (User) response.getData();
            return orderService.cancel(user.getId(), orderNo);
        } else {
            return response;
        }
    }

    @RequestMapping(value = "/get_order_cart_product.do")
    public JsonResult getOrderCartProduct(HttpServletRequest httpServletRequest) {
        JsonResult response = userCheck.checkLogin(httpServletRequest);
        if (response.isSuccess()) {
            User user = (User) response.getData();
            return orderService.getOrderCartProduct(user.getId());
        } else {
            return response;
        }
    }

    @RequestMapping(value = "/detail.do")
    public JsonResult detail(HttpServletRequest httpServletRequest, Long orderNo) {
        JsonResult response = userCheck.checkLogin(httpServletRequest);
        if (response.isSuccess()) {
            User user = (User) response.getData();
            return orderService.getOrderDetail(user.getId(), orderNo);
        } else {
            return response;
        }
    }

    @RequestMapping(value = "/list.do")
    public JsonResult list(HttpServletRequest httpServletRequest,
                                     @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                     @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {

        JsonResult response = userCheck.checkLogin(httpServletRequest);
        if (response.isSuccess()) {
            User user = (User) response.getData();
            return orderService.getOrderList(user.getId(), pageNum, pageSize);
        } else {
            return response;
        }
    }

    @RequestMapping(value = "/pay.do")
    public JsonResult pay(HttpServletRequest httpServletRequest, Long orderNo) {
        JsonResult response = userCheck.checkLogin(httpServletRequest);
        if (response.isSuccess()) {
            User user = (User) response.getData();
            String path = httpServletRequest.getSession().getServletContext().getRealPath("upload");
            return orderService.pay(orderNo, user.getId(), path);
        } else {
            return response;
        }
    }

    @RequestMapping(value = "/alipay_callback.do")
    public Object alipayCallback(HttpServletRequest request) {
        Map<String, String> params = Maps.newHashMap();

        Map requestParams = request.getParameterMap();
        for (Object obj : requestParams.keySet()) {
            String name = (String) obj;
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
            }
            params.put(name, valueStr);
        }
        log.info("支付宝回调,sign:{},trade_status:{},参数:{}", params.get("sign"), params.get("trade_status"), params.toString());

        // 验证回调
        params.remove("sign_type");
        try {
            boolean alipayRSACheckedV2 = AlipaySignature.rsaCheckV2(params, Configs.getAlipayPublicKey(), "utf-8", Configs.getSignType());
            if (!alipayRSACheckedV2) {
                return JsonResult.error("非法请求，验证不通过");
            }
        } catch (AlipayApiException e) {
            log.error("支付宝验证回调异常", e);
        }

        JsonResult response = orderService.alipayCallback(params);
        if (response.isSuccess()) {
            return Const.AlipayCallback.RESPONSE_SUCCESS;
        }
        return Const.AlipayCallback.RESPONSE_FAILED;
    }

    @RequestMapping(value = "/query_order_pay_status.do")
    public JsonResult queryOrderPayStatus(HttpServletRequest httpServletRequest, Long orderNo) {
        JsonResult response = userCheck.checkLogin(httpServletRequest);
        if (response.isSuccess()) {
            User user = (User) response.getData();
            JsonResult result = orderService.queryOrderPayStatus(user.getId(), orderNo);
            if (result.isSuccess()) {
                return JsonResult.success(true);
            }
            return result;
        } else {
            return response;
        }
    }
}
