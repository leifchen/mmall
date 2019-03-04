package com.mmall.dao;

import com.mmall.pojo.Cart;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 购物车Mapper
 * <p>
 * @Author LeifChen
 * @Date 2019-02-25
 */
public interface CartMapper {
    /**
     * 根据主键id删除
     * @param id
     * @return
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * 新增
     * @param record
     * @return
     */
    int insert(Cart record);

    /**
     * 选择性新增
     * @param record
     * @return
     */
    int insertSelective(Cart record);

    /**
     * 根据主键id查询
     * @param id
     * @return
     */
    Cart selectByPrimaryKey(Integer id);

    /**
     * 选择性更新
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(Cart record);

    /**
     * 更新
     * @param record
     * @return
     */
    int updateByPrimaryKey(Cart record);

    /**
     * 根据用户id和商品id搜索
     * @param userId    用户id
     * @param productId 商品id
     * @return
     */
    Cart selectByUserIdAndProductId(@Param("userId") Integer userId, @Param("productId") Integer productId);

    /**
     * 根据用户id搜索
     * @param userId 用户id
     * @return
     */
    List<Cart> selectByUserId(Integer userId);

    /**
     * 根据用户id搜索状态为选中的商品
     * @param userId 用户id
     * @return
     */
    int selectCheckedStatusByUserId(Integer userId);

    /**
     * 根据用户id和商品id删除
     * @param userId        用户id
     * @param productIdList 商品id列表
     * @return
     */
    int deleteByUserIdAndProductIds(@Param("userId") Integer userId, @Param("productIdList") List<String> productIdList);

    /**
     * 选择/反选商品
     * @param userId    用户id
     * @param productId 商品id
     * @param checked   勾选标志
     * @return
     */
    int checkedOrUncheckedProduct(@Param("userId") Integer userId, @Param("productId") Integer productId, @Param("checked") Integer checked);

    /**
     * 返回购物车里选中商品的数量
     * @param userId 用户id
     * @return
     */
    int selectCartProductCount(Integer userId);
}