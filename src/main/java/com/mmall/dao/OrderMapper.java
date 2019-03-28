package com.mmall.dao;

import com.mmall.pojo.Order;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 订单Mapper
 * <p>
 * @Author LeifChen
 * @Date 2019-02-25
 */
public interface OrderMapper {
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
    int insert(Order record);

    /**
     * 选择性新增
     * @param record
     * @return
     */
    int insertSelective(Order record);

    /**
     * 根据主键id查询
     * @param id
     * @return
     */
    Order selectByPrimaryKey(Integer id);

    /**
     * 选择性更新
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(Order record);

    /**
     * 更新
     * @param record
     * @return
     */
    int updateByPrimaryKey(Order record);

    /**
     * 根据用户id和订单号查询
     * @param userId  用户id
     * @param orderNo 订单号
     * @return
     */
    Order selectByUserIdAndOrderNo(@Param("userId") Integer userId, @Param("orderNo") Long orderNo);

    /**
     * 根据用户id查询
     * @param userId 用户id
     * @return
     */
    List<Order> selectByUserId(Integer userId);

    /**
     * 根据订单号查询
     * @param orderNo 订单号
     * @return
     */
    Order selectByOrderNo(Long orderNo);

    /**
     * 查询所有订单
     * @return
     */
    List<Order> selectAll();

    /**
     * 根据创建时间、状态查询订单
     * @param status 状态
     * @param date   创建时间
     * @return
     */
    List<Order> selectOrderStatusByCreateTime(@Param("status") Integer status, @Param("date") String date);

    /**
     * 根据id关闭订单
     * @param id 订单id
     * @return
     */
    int closeOrderById(Integer id);
}