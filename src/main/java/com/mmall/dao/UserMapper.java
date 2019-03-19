package com.mmall.dao;

import com.mmall.pojo.User;
import org.apache.ibatis.annotations.Param;

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

    /**
     * 检查用户名是否存在
     * @param username
     * @return
     */
    int checkUsername(String username);

    /**
     * 检查邮箱是否存在
     * @param email
     * @return
     */
    int checkEmail(String email);

    /**
     * 获取当前登录用户信息
     * @param username
     * @param password
     * @return
     */
    User selectLogin(@Param("username") String username, @Param("password") String password);

    /**
     * 根据用户名查询找回密码的问题
     * @param username
     * @return
     */
    String selectQuestionByUsername(String username);

    /**
     * 检验找回密码问题的答案
     * @param username
     * @param question
     * @param answer
     * @return
     */
    int checkAnswer(@Param("username") String username, @Param("question") String question, @Param("answer") String answer);

    /**
     * 更新密码
     * @param username
     * @param passwordNew
     * @return
     */
    int updatePasswordByUsername(@Param("username") String username, @Param("passwordNew") String passwordNew);

    /**
     * 校验密码
     * @param password
     * @param userId
     * @return
     */
    int checkPassword(@Param("password") String password, @Param("userId") Integer userId);

    /**
     * 根据用户id校验email是否重复
     * @param email
     * @param userId
     * @return
     */
    int checkEmailByUserId(@Param("email") String email, @Param("userId") Integer userId);
}