package com.mmall.service;

import com.mmall.common.JsonResult;
import com.mmall.vo.CartVO;

/**
 * 购物车Service
 * <p>
 * @Author LeifChen
 * @Date 2019-03-01
 */
public interface CartService {

    /**
     * 添加商品到购物车
     * @param userId    用户id
     * @param productId 商品id
     * @param count     商品数量
     * @return
     */
    JsonResult<CartVO> add(Integer userId, Integer productId, Integer count);

    /**
     * 更新购物车的商品数量
     * @param userId    用户id
     * @param productId 商品id
     * @param count     商品数量
     * @return
     */
    JsonResult<CartVO> update(Integer userId, Integer productId, Integer count);

    /**
     * 根据用户id和商品id删除
     * @param userId     用户id
     * @param productIds 商品id列表
     * @return
     */
    JsonResult<CartVO> deleteProduct(Integer userId, String productIds);

    /**
     * 根据用户id查询
     * @param userId 用户id
     * @return
     */
    JsonResult<CartVO> list(Integer userId);

    /**
     * 全选/全反选商品
     * @param userId    用户id
     * @param productId 商品id
     * @param checked   勾选标志
     * @return
     */
    JsonResult<CartVO> selectOrUnselect(Integer userId, Integer productId, Integer checked);

    /**
     * 返回购物车里选中商品的数量
     * @param userId 用户id
     * @return
     */
    JsonResult<Integer> getCartProductCount(Integer userId);
}
