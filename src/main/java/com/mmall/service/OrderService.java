package com.mmall.service;

import com.github.pagehelper.PageInfo;
import com.mmall.common.JsonResult;
import com.mmall.vo.OrderVO;

/**
 * 订单Service
 * <p>
 * @Author LeifChen
 * @Date 2019-03-08
 */
public interface OrderService {

    /**
     * 创建订单
     * @param userId     用户id
     * @param shippingId 收货地址id
     * @return
     */
    JsonResult<OrderVO> create(Integer userId, Integer shippingId);

    /**
     * 取消订单
     * @param userId  用户id
     * @param orderNo 订单号
     * @return
     */
    JsonResult<String> cancel(Integer userId, Long orderNo);

    /**
     * 获取订单的购物车商品明细
     * @param userId 用户id
     * @return
     */
    JsonResult getOrderCartProduct(Integer userId);

    /**
     * 普通用户查看订单明细
     * @param userId  用户id
     * @param orderNo 订单号
     * @return
     */
    JsonResult getOrderDetail(Integer userId, Long orderNo);

    /**
     * 普通用户查看订单列表
     * @param userId   用户id
     * @param pageNum  页码
     * @param pageSize 每页显示数量
     * @return
     */
    JsonResult<PageInfo> getOrderList(Integer userId, int pageNum, int pageSize);

    /**
     * 管理员查看订单列表
     * @param pageNum  页码
     * @param pageSize 每页显示数量
     * @return
     */
    JsonResult<PageInfo> manageList(int pageNum, int pageSize);

    /**
     * 管理员查看订单明细
     * @param orderNo 订单号
     * @return
     */
    JsonResult<OrderVO> manageDetail(Long orderNo);

    /**
     * 管理员搜索订单号
     * @param orderNo  订单号
     * @param pageNum  页码
     * @param pageSize 每页显示数量
     * @return
     */
    JsonResult<PageInfo> manageSearch(Long orderNo, int pageNum, int pageSize);

    /**
     * 根据订单号发货
     * @param orderNo 订单号
     * @return
     */
    JsonResult<String> manageSendGoods(Long orderNo);
}
