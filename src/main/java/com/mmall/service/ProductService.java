package com.mmall.service;

import com.github.pagehelper.PageInfo;
import com.mmall.common.JsonResult;
import com.mmall.pojo.Product;
import com.mmall.vo.ProductDetailVO;

/**
 * 产品Service
 * <p>
 * @Author LeifChen
 * @Date 2019-02-28
 */
public interface ProductService {

    /**
     * 新增/修改产品
     * @param product 产品
     * @return
     */
    JsonResult saveOrUpdateProduct(Product product);

    /**
     * 修改产品的销售状态
     * @param productId 产品id
     * @param status    产品的销售状态
     * @return
     */
    JsonResult<String> setSaleStatus(Integer productId, Integer status);

    /**
     * 管理产品详情
     * @param productId 产品id
     * @return
     */
    JsonResult<ProductDetailVO> manageProductDetail(Integer productId);

    /**
     * 获取产品列表
     * @param pageNum  页码
     * @param pageSize 每页显示数量
     * @return
     */
    JsonResult<PageInfo> getProductList(int pageNum, int pageSize);

    /**
     * 检索产品
     * @param productName 产品名称
     * @param productId   产品id
     * @param pageNum     页码
     * @param pageSize    每页显示数量
     * @return
     */
    JsonResult<PageInfo> searchProduct(String productName, Integer productId, int pageNum, int pageSize);

    /**
     * 获取产品详情
     * @param productId 产品id
     * @return
     */
    JsonResult<ProductDetailVO> getProductDetail(Integer productId);

    /**
     * 根据类别关键字查找产品
     * @param keyword    关键字
     * @param categoryId 类别id
     * @param pageNum    页码
     * @param pageSize   每页显示数量
     * @param orderBy    排序
     * @return
     */
    JsonResult<PageInfo> getProductByKeywordCategory(String keyword, Integer categoryId, int pageNum, int pageSize, String orderBy);
}
