package com.mmall.controller.backend;

import com.mmall.common.JsonResult;
import com.mmall.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 后台管理-类别Controller
 * <p>
 * @Author LeifChen
 * @Date 2019-02-28
 */
@RestController
@RequestMapping("/admin/category")
public class CategoryManageController {

    @Autowired
    private CategoryService categoryService;

    @RequestMapping("/add_category.do")
    public JsonResult addCategory(String categoryName, @RequestParam(value = "parentId", defaultValue = "0") Integer parentId) {
        return categoryService.addCategory(categoryName, parentId);
    }

    @RequestMapping("/set_category_name.do")
    public JsonResult setCategoryName(Integer categoryId, String categoryName) {
        return categoryService.updateCategoryName(categoryId, categoryName);
    }

    @RequestMapping("/get_category.do")
    public JsonResult getChildrenParallelCategory(@RequestParam(value = "categoryId", defaultValue = "0") Integer categoryId) {
        return categoryService.getChildrenParallelCategory(categoryId);
    }

    @RequestMapping("/get_deep_category.do")
    public JsonResult getCategoryAndDeepChildrenCategory(@RequestParam(value = "categoryId", defaultValue = "0") Integer categoryId) {
        return categoryService.selectCategoryAndChildrenById(categoryId);
    }
}
