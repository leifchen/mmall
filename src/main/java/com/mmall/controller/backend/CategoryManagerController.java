package com.mmall.controller.backend;

import com.mmall.common.Const;
import com.mmall.common.JsonResult;
import com.mmall.common.ResponseCodeEnum;
import com.mmall.pojo.User;
import com.mmall.service.CategoryService;
import com.mmall.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

/**
 * 后台管理-类别Controller
 * <p>
 * @Author LeifChen
 * @Date 2019-02-28
 */
@RestController
@RequestMapping("/admin/category")
public class CategoryManagerController {

    @Autowired
    private UserService userService;
    @Autowired
    private CategoryService categoryService;

    @RequestMapping("/add_category.do")
    public JsonResult addCategory(HttpSession session, String categoryName, @RequestParam(value = "parentId", defaultValue = "0") Integer parentId) {
        JsonResult response = checkAdminRoleLogin(session);
        if (response.isSuccess()) {
            return categoryService.addCategory(categoryName, parentId);
        } else {
            return response;
        }
    }

    @RequestMapping("/set_category_name.do")
    public JsonResult setCategoryName(HttpSession session, Integer categoryId, String categoryName) {
        JsonResult response = checkAdminRoleLogin(session);
        if (response.isSuccess()) {
            return categoryService.updateCategoryName(categoryId, categoryName);
        } else {
            return response;
        }
    }

    @RequestMapping("/get_category.do")
    public JsonResult getChildrenParallelCategory(HttpSession session, @RequestParam(value = "categoryId", defaultValue = "0") Integer categoryId) {
        JsonResult response = checkAdminRoleLogin(session);
        if (response.isSuccess()) {
            return categoryService.getChildrenParallelCategory(categoryId);
        } else {
            return response;
        }
    }

    @RequestMapping("/get_deep_category.do")
    public JsonResult getCategoryAndDeepChildrenCategory(HttpSession session, @RequestParam(value = "categoryId", defaultValue = "0") Integer categoryId) {
        JsonResult response = checkAdminRoleLogin(session);
        if (response.isSuccess()) {
            return categoryService.selectCategoryAndChildrenById(categoryId);
        } else {
            return response;
        }
    }

    /**
     * 校验管理员角色是否登录
     * @param session
     * @return
     */
    private JsonResult checkAdminRoleLogin(HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return JsonResult.error(ResponseCodeEnum.NEED_LOGIN.getCode(), "用户未登录，请登录");
        }
        // 校验是否管理员
        if (userService.checkAdminRole(user).isSuccess()) {
            return JsonResult.success();
        } else {
            return JsonResult.error("无权限操作，需要管理员权限");
        }
    }
}
