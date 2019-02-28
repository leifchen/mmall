package com.mmall.service;

import com.mmall.common.JsonResult;
import com.mmall.pojo.Category;

import java.util.List;

/**
 * 类别Service
 * <p>
 * @Author LeifChen
 * @Date 2019-02-28
 */
public interface CategoryService {

    /**
     * 添加类别
     * @param categoryName
     * @param parentId
     * @return
     */
    JsonResult addCategory(String categoryName, Integer parentId);

    /**
     * 更新类别名称
     * @param categoryId
     * @param categoryName
     * @return
     */
    JsonResult updateCategoryName(Integer categoryId, String categoryName);

    /**
     * 获取子节点的平级类别
     * @param categoryId
     * @return
     */
    JsonResult<List<Category>> getChildrenParallelCategory(Integer categoryId);

    /**
     * 递归获取当前节点下的所有子节点
     * @param categoryId
     * @return
     */
    JsonResult selectCategoryAndChildrenById(Integer categoryId);
}
