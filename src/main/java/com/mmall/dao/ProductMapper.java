package com.mmall.dao;

import com.mmall.pojo.Product;

/**
 * 商品Mapper
 * <p>
 * @Author LeifChen
 * @Date 2019-02-25
 */
public interface ProductMapper {
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
    int insert(Product record);

    /**
     * 选择性新增
     * @param record
     * @return
     */
    int insertSelective(Product record);

    /**
     * 根据主键id查询
     * @param id
     * @return
     */
    Product selectByPrimaryKey(Integer id);

    /**
     * 选择性更新
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(Product record);

    /**
     * 更新
     * @param record
     * @return
     */
    int updateByPrimaryKey(Product record);
}