package com.mmall.controller.backend;

import com.mmall.common.JsonResult;
import com.mmall.controller.common.UserCheck;
import com.mmall.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

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
    private CategoryService categoryService;
    @Autowired
    private UserCheck userCheck;

    @RequestMapping("/add_category.do")
    public JsonResult addCategory(HttpServletRequest httpServletRequest, String categoryName, @RequestParam(value = "parentId", defaultValue = "0") Integer parentId) {
        JsonResult response = userCheck.checkAdminRoleLogin(httpServletRequest);
        if (response.isSuccess()) {
            return categoryService.addCategory(categoryName, parentId);
        } else {
            return response;
        }
    }

    @RequestMapping("/set_category_name.do")
    public JsonResult setCategoryName(HttpServletRequest httpServletRequest, Integer categoryId, String categoryName) {
        JsonResult response = userCheck.checkAdminRoleLogin(httpServletRequest);
        if (response.isSuccess()) {
            return categoryService.updateCategoryName(categoryId, categoryName);
        } else {
            return response;
        }
    }

    @RequestMapping("/get_category.do")
    public JsonResult getChildrenParallelCategory(HttpServletRequest httpServletRequest, @RequestParam(value = "categoryId", defaultValue = "0") Integer categoryId) {
        JsonResult response = userCheck.checkAdminRoleLogin(httpServletRequest);
        if (response.isSuccess()) {
            return categoryService.getChildrenParallelCategory(categoryId);
        } else {
            return response;
        }
    }

    @RequestMapping("/get_deep_category.do")
    public JsonResult getCategoryAndDeepChildrenCategory(HttpServletRequest httpServletRequest, @RequestParam(value = "categoryId", defaultValue = "0") Integer categoryId) {
        JsonResult response = userCheck.checkAdminRoleLogin(httpServletRequest);
        if (response.isSuccess()) {
            return categoryService.selectCategoryAndChildrenById(categoryId);
        } else {
            return response;
        }
    }
}
