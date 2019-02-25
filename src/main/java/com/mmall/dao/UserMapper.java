package com.mmall.dao;

import com.mmall.pojo.User;

/**
 * 用户Mapper
 * <p>
 * @Author LeifChen
 * @Date 2019-02-25
 */
public interface UserMapper {
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
    int insert(User record);

    /**
     * 选择性新增
     * @param record
     * @return
     */
    int insertSelective(User record);

    /**
     * 根据主键id查询
     * @param id
     * @return
     */
    User selectByPrimaryKey(Integer id);

    /**
     * 选择性更新
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(User record);

    /**
     * 更新
     * @param record
     * @return
     */
    int updateByPrimaryKey(User record);
}