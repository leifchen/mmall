package com.mmall.service.impl;

import com.mmall.common.Const;
import com.mmall.common.JsonResult;
import com.mmall.common.ResponseCodeEnum;
import com.mmall.dao.UserMapper;
import com.mmall.pojo.User;
import com.mmall.service.UserService;
import com.mmall.util.Md5Utils;
import com.mmall.util.RedisPoolUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

/**
 * 用户Service的实现类
 * <p>
 * @Author LeifChen
 * @Date 2019-02-26
 */
@Service("userService")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public JsonResult<User> login(String username, String password) {
        int resultCount = userMapper.checkUsername(username);
        if (resultCount == 0) {
            return JsonResult.error("用户名不存在");
        }

        User user = userMapper.selectLogin(username, Md5Utils.encode(password));
        if (user == null) {
            return JsonResult.error("密码错误");
        }

        user.setPassword(StringUtils.EMPTY);
        return JsonResult.success("登录成功", user);
    }

    @Override
    public JsonResult<String> register(User user) {
        JsonResult<String> validResponse = checkValid(user.getUsername(), Const.USERNAME);
        if (!validResponse.isSuccess()) {
            return validResponse;
        }
        validResponse = checkValid(user.getEmail(), Const.EMAIL);
        if (!validResponse.isSuccess()) {
            return validResponse;
        }

        user.setRole(Const.Role.ROLE_CUSTOMER);
        user.setPassword(Md5Utils.encode(user.getPassword()));

        int resultCount = userMapper.insert(user);
        if (resultCount == 0) {
            return JsonResult.error("注册失败");
        }
        return JsonResult.success("注册成功");
    }

    @Override
    public JsonResult<String> checkValid(String str, String type) {
        if (StringUtils.isNotEmpty(type)) {
            // 校验
            if (Const.USERNAME.equals(type)) {
                int resultCount = userMapper.checkUsername(str);
                if (resultCount > 0) {
                    return JsonResult.error("用户名已存在");
                }
            } else if (Const.EMAIL.equals(type)) {
                int resultCount = userMapper.checkEmail(str);
                if (resultCount > 0) {
                    return JsonResult.error("email已存在");
                }
            } else {
                return JsonResult.error("参数不合法");
            }
        } else {
            return JsonResult.error(ResponseCodeEnum.ILLEGAL_ARGUMENT.getDesc());
        }
        return JsonResult.success("校验成功");
    }

    @Override
    public JsonResult<String> selectQuestion(String username) {
        JsonResult validResponse = checkValid(username, Const.USERNAME);
        if (validResponse.isSuccess()) {
            return JsonResult.error("用户不存在");
        }

        String question = userMapper.selectQuestionByUsername(username);
        if (StringUtils.isNotBlank(question)) {
            return JsonResult.success(question);
        }
        return JsonResult.error("找回密码的问题是空的");
    }

    @Override
    public JsonResult<String> checkAnswer(String username, String question, String answer) {
        int resultCount = userMapper.checkAnswer(username, question, answer);
        if (resultCount > 0) {
            String forgetToken = UUID.randomUUID().toString();
            RedisPoolUtils.setEx(Const.TOKEN_PREFIX + username, forgetToken, 60 * 60 * 12);
            return JsonResult.success(forgetToken);
        }
        return JsonResult.error("问题的答案错误");
    }

    @Override
    public JsonResult<String> forgetResetPassword(String username, String passwordNew, String forgetToken) {
        if (StringUtils.isBlank(forgetToken)) {
            return JsonResult.error("参数错误，token需要传递");
        }
        JsonResult validResponse = checkValid(username, Const.USERNAME);
        if (validResponse.isSuccess()) {
            return JsonResult.error("用户不存在");
        }

        String token = RedisPoolUtils.get(Const.TOKEN_PREFIX + username);
        if (StringUtils.isBlank(token)) {
            return JsonResult.error("token无效或者过期");
        }
        if (StringUtils.equals(forgetToken, token)) {
            String md5Password = Md5Utils.encode(passwordNew);
            int rowCount = userMapper.updatePasswordByUsername(username, md5Password);
            if (rowCount > 0) {
                return JsonResult.success("修改密码成功");
            }
        } else {
            return JsonResult.error("token错误，请重新获取重置密码的token");
        }
        return JsonResult.error("修改密码失败");
    }

    @Override
    public JsonResult<String> resetPassword(String passwordOld, String passwordNew, User user) {
        int resultCount = userMapper.checkPassword(Md5Utils.encode(passwordOld), user.getId());
        if (resultCount == 0) {
            return JsonResult.error("旧密码错误");
        }

        user.setPassword(Md5Utils.encode(passwordNew));
        int updateCount = userMapper.updateByPrimaryKeySelective(user);
        if (updateCount > 0) {
            return JsonResult.success("密码更新成功");
        }
        return JsonResult.error("密码更新失败");
    }

    @Override
    public JsonResult<User> updateInformation(User user) {
        int resultCount = userMapper.checkEmailByUserId(user.getEmail(), user.getId());
        if (resultCount > 0) {
            return JsonResult.error("email已存在，请更换email再尝试更新");
        }
        User updateUser = new User();
        updateUser.setId(user.getId());
        updateUser.setEmail(user.getEmail());
        updateUser.setPhone(user.getPhone());
        updateUser.setQuestion(user.getQuestion());
        updateUser.setAnswer(user.getAnswer());
        updateUser.setUpdateTime(new Date());
        int updateCount = userMapper.updateByPrimaryKeySelective(updateUser);
        if (updateCount > 0) {
            return JsonResult.success("更新个人信息成功", updateUser);
        }
        return JsonResult.error("更新个人信息失败");
    }

    // backend

    @Override
    public JsonResult checkAdminRole(User user) {
        if (user != null && user.getRole() == Const.Role.ROLE_ADMIN) {
            return JsonResult.success();
        }
        return JsonResult.error();
    }
}
