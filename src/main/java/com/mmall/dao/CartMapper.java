package com.mmall.dao;

import com.mmall.pojo.Cart;

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
}