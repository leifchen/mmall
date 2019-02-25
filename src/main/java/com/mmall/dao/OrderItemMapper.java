package com.mmall.dao;

import com.mmall.pojo.OrderItem;

/**
 * 订单明细Mapper
 * <p>
 * @Author LeifChen
 * @Date 2019-02-25
 */
public interface OrderItemMapper {
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
    int insert(OrderItem record);

    /**
     * 选择性新增
     * @param record
     * @return
     */
    int insertSelective(OrderItem record);

    /**
     * 根据主键id查询
     * @param id
     * @return
     */
    OrderItem selectByPrimaryKey(Integer id);

    /**
     * 选择性更新
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(OrderItem record);

    /**
     * 更新
     * @param record
     * @return
     */
    int updateByPrimaryKey(OrderItem record);
}