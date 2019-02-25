package com.mmall.dao;

import com.mmall.pojo.Shipping;

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
}