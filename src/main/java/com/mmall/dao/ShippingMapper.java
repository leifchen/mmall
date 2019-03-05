package com.mmall.dao;

import com.mmall.pojo.Shipping;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 物流信息Mapper
 * <p>
 * @Author LeifChen
 * @Date 2019-02-25
 */
public interface ShippingMapper {
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
    int insert(Shipping record);

    /**
     * 选择性新增
     * @param record
     * @return
     */
    int insertSelective(Shipping record);

    /**
     * 根据主键id查询
     * @param id
     * @return
     */
    Shipping selectByPrimaryKey(Integer id);

    /**
     * 选择性更新
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(Shipping record);

    /**
     * 更新
     * @param record
     * @return
     */
    int updateByPrimaryKey(Shipping record);

    /**
     * 更新收货地址信息
     * @param shipping 收货地址信息
     * @return
     */
    int updateByShipping(Shipping shipping);

    /**
     * 根据收货地址id和用户id查询
     * @param shippingId 收货地址id
     * @param userId     用户id
     * @return
     */
    Shipping selectByShippingIdAndUserId(@Param("shippingId") Integer shippingId, @Param("userId") Integer userId);

    /**
     * 根据用户id查询
     * @param userId 用户id
     * @return
     */
    List<Shipping> selectByUserId(Integer userId);
}