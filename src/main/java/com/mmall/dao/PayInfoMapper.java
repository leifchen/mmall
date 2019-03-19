package com.mmall.dao;

import com.mmall.pojo.PayInfo;

/**
 * 付款信息Mapper
 * <p>
 * @Author LeifChen
 * @Date 2019-02-25
 */
public interface PayInfoMapper {
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
    int insert(PayInfo record);

    /**
     * 选择性新增
     * @param record
     * @return
     */
    int insertSelective(PayInfo record);

    /**
     * 根据主键id查询
     * @param id
     * @return
     */
    PayInfo selectByPrimaryKey(Integer id);

    /**
     * 选择性更新
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(PayInfo record);

    /**
     * 更新
     * @param record
     * @return
     */
    int updateByPrimaryKey(PayInfo record);
}