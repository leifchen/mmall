package com.mmall.service;

import com.github.pagehelper.PageInfo;
import com.mmall.common.JsonResult;
import com.mmall.pojo.Shipping;

/**
 * 收货地址Service
 * <p>
 * @Author LeifChen
 * @Date 2019-03-05
 */
public interface ShippingService {

    /**
     * 新增
     * @param userId   用户id
     * @param shipping 收货地址信息
     * @return
     */
    JsonResult add(Integer userId, Shipping shipping);

    /**
     * 修改
     * @param userId   用户id
     * @param shipping 收货地址信息
     * @return
     */
    JsonResult<String> update(Integer userId, Shipping shipping);

    /**
     * 删除
     * @param userId   用户id
     * @param shippingId 收货地址信息
     * @return
     */
    JsonResult<String> delete(Integer userId, Integer shippingId);

    /**
     * 查询
     * @param userId   用户id
     * @param shippingId 收货地址信息
     * @return
     */
    JsonResult<Shipping> select(Integer userId, Integer shippingId);

    /**
     * 分页列表
     * @param userId   用户id
     * @param pageNum  页码
     * @param pageSize 每页显示数量
     * @return
     */
    JsonResult<PageInfo> list(Integer userId, int pageNum, int pageSize);
}
