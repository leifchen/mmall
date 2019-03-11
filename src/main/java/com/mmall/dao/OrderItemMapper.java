package com.mmall.dao;

import com.mmall.pojo.OrderItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;

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


    /**
     * 根据订单号和用户id查询订单明细
     * @param orderNo 订单号
     * @param userId  用户id
     * @return
     */
    List<OrderItem> getByOrderNoAndUserId(@Param("orderNo") Long orderNo, @Param("userId") Integer userId);

    /**
     * 根据订单号查询订单明细
     * @param orderNo 订单号
     * @return
     */
    List<OrderItem> getByOrderNo(Long orderNo);

    /**
     * 批量插入
     * @param orderItemList 订单商品列表
     */
    void batchInsert(@Param("orderItemList") List<OrderItem> orderItemList);
}