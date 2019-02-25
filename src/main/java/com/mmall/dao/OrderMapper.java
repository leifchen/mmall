package com.mmall.dao;

import com.mmall.pojo.Order;

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
}