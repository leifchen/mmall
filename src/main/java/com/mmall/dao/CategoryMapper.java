package com.mmall.dao;

import com.mmall.pojo.Category;

/**
 * 商品类别Mapper
 * <p>
 * @Author LeifChen
 * @Date 2019-02-25
 */
public interface CategoryMapper {
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
    int insert(Category record);

    /**
     * 选择性新增
     * @param record
     * @return
     */
    int insertSelective(Category record);

    /**
     * 根据主键id查询
     * @param id
     * @return
     */
    Category selectByPrimaryKey(Integer id);

    /**
     * 选择性更新
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(Category record);

    /**
     * 更新
     * @param record
     * @return
     */
    int updateByPrimaryKey(Category record);
}