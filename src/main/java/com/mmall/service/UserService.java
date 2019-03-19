package com.mmall.service;

import com.mmall.common.JsonResult;
import com.mmall.pojo.User;

/**
 * 用户Service
 * <p>
 * @Author LeifChen
 * @Date 2019-02-26
 */
public interface UserService {

    /**
     * 登录
     * @param username
     * @param password
     * @return
     */
    JsonResult<User> login(String username, String password);

    /**
     * 注册
     * @param user
     * @return
     */
    JsonResult<String> register(User user);

    /**
     * 检查参数（用户名、email）是否有效
     * @param str
     * @param type
     * @return
     */
    JsonResult<String> checkValid(String str, String type);

    /**
     * 查询找回密码的问题
     * @param str
     * @return
     */
    JsonResult<String> selectQuestion(String str);

    /**
     * 检验找回密码问题的答案
     * @param username
     * @param question
     * @param answer
     * @return
     */
    JsonResult<String> checkAnswer(String username, String question, String answer);

    /**
     * 忘记密码的重置密码
     * @param username
     * @param passwordNew
     * @param forgetToken
     * @return
     */
    JsonResult<String> forgetResetPassword(String username, String passwordNew, String forgetToken);

    /**
     * 登录状态的重置密码
     * @param passwordOld
     * @param passwordNew
     * @param user
     * @return
     */
    JsonResult<String> resetPassword(String passwordOld, String passwordNew, User user);

    /**
     * 更新用户信息
     * @param user
     * @return
     */
    JsonResult<User> updateInformation(User user);

    /**
     * 校验是否管理员角色
     * @param user
     * @return
     */
    JsonResult checkAdminRole(User user);
}
