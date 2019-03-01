package com.mmall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.mmall.common.Const;
import com.mmall.common.JsonResult;
import com.mmall.common.ResponseCodeEnum;
import com.mmall.dao.CategoryMapper;
import com.mmall.dao.ProductMapper;
import com.mmall.pojo.Category;
import com.mmall.pojo.Product;
import com.mmall.service.CategoryService;
import com.mmall.service.ProductService;
import com.mmall.util.DateTimeUtils;
import com.mmall.util.PropertiesUtils;
import com.mmall.vo.ProductDetailVO;
import com.mmall.vo.ProductListVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 产品Service的实现类
 * <p>
 * @Author LeifChen
 * @Date 2019-02-28
 */
@Service("productService")
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private CategoryService categoryService;

    @Override
    public JsonResult saveOrUpdateProduct(Product product) {
        if (product != null) {
            if (StringUtils.isNotBlank(product.getSubImages())) {
                String[] subImageArray = product.getSubImages().split(",");
                if (subImageArray.length > 0) {
                    product.setMainImage(subImageArray[0]);
                }
            }

            if (product.getId() != null) {
                int rowCount = productMapper.updateByPrimaryKey(product);
                if (rowCount > 0) {
                    return JsonResult.success("更新产品成功");
                }
                return JsonResult.success("更新产品失败");
            } else {
                int rowCount = productMapper.insert(product);
                if (rowCount > 0) {
                    return JsonResult.success("新增产品成功");
                }
                return JsonResult.success("新增产品失败");
            }
        }
        return JsonResult.error("新增或更新产品参数不正确");
    }

    @Override
    public JsonResult<String> setSaleStatus(Integer productId, Integer status) {
        if (productId == null || status == null) {
            return JsonResult.error(ResponseCodeEnum.ILLEGAL_ARGUMENT.getCode(), ResponseCodeEnum.ILLEGAL_ARGUMENT.getDesc());
        }
        Product product = new Product();
        product.setId(productId);
        product.setStatus(status);
        int rowCount = productMapper.updateByPrimaryKeySelective(product);
        if (rowCount > 0) {
            return JsonResult.success("修改产品销售状态成功");
        }
        return JsonResult.error("修改产品销售状态失败");
    }

    @Override
    public JsonResult<ProductDetailVO> manageProductDetail(Integer productId) {
        if (productId == null) {
            return JsonResult.error(ResponseCodeEnum.ILLEGAL_ARGUMENT.getCode(), ResponseCodeEnum.ILLEGAL_ARGUMENT.getDesc());
        }
        Product product = productMapper.selectByPrimaryKey(productId);
        if (product == null) {
            return JsonResult.error("产品已下架或者删除");
        }
        ProductDetailVO productDetailVO = assembleProductDetailVO(product);
        return JsonResult.success(productDetailVO);
    }

    /**
     * 填充产品详情
     * @param product
     * @return
     */
    private ProductDetailVO assembleProductDetailVO(Product product) {
        ProductDetailVO productDetailVO = new ProductDetailVO();
        productDetailVO.setId(product.getId());
        productDetailVO.setSubtitle(product.getSubtitle());
        productDetailVO.setPrice(product.getPrice());
        productDetailVO.setMainImage(product.getMainImage());
        productDetailVO.setSubImages(product.getSubImages());
        productDetailVO.setCategoryId(product.getCategoryId());
        productDetailVO.setDetail(product.getDetail());
        productDetailVO.setName(product.getName());
        productDetailVO.setStatus(product.getStatus());
        productDetailVO.setStock(product.getStock());

        productDetailVO.setImageHost(PropertiesUtils.getProperty("ftp.server.http.prefix", "http://image.mall.com/"));

        Category category = categoryMapper.selectByPrimaryKey(product.getCategoryId());
        if (category == null) {
            // 默认根节点
            productDetailVO.setParentCategoryId(0);
        } else {
            productDetailVO.setParentCategoryId(category.getParentId());
        }

        productDetailVO.setCreateTime(DateTimeUtils.dateToStr(product.getCreateTime()));
        productDetailVO.setUpdateTime(DateTimeUtils.dateToStr(product.getUpdateTime()));
        return productDetailVO;
    }

    @Override
    public JsonResult<PageInfo> getProductList(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Product> productList = productMapper.selectList();
        PageInfo pageInfo = assemblePageInfo(productList);
        return JsonResult.success(pageInfo);
    }

    @Override
    public JsonResult<PageInfo> searchProduct(String productName, Integer productId, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        if (StringUtils.isNotBlank(productName)) {
            productName = "%" + productName + "%";
        }
        List<Product> productList = productMapper.selectByNameAndProductId(productName, productId);
        PageInfo pageInfo = assemblePageInfo(productList);
        return JsonResult.success(pageInfo);
    }

    @Override
    public JsonResult<ProductDetailVO> getProductDetail(Integer productId) {
        if (productId == null) {
            return JsonResult.error(ResponseCodeEnum.ILLEGAL_ARGUMENT.getCode(), ResponseCodeEnum.ILLEGAL_ARGUMENT.getDesc());
        }
        Product product = productMapper.selectByPrimaryKey(productId);
        if (product == null) {
            return JsonResult.error("产品已下架或者删除");
        }

        if (product.getStatus() != Const.ProductStatusEnum.ON_SALE.getCode()) {
            return JsonResult.error("产品已下架或者删除");
        }
        ProductDetailVO productDetailVO = assembleProductDetailVO(product);
        return JsonResult.success(productDetailVO);
    }

    @Override
    public JsonResult<PageInfo> getProductByKeywordCategory(String keyword, Integer categoryId, int pageNum, int pageSize, String orderBy) {
        if (StringUtils.isBlank(keyword) && categoryId == null) {
            return JsonResult.error(ResponseCodeEnum.ILLEGAL_ARGUMENT.getCode(), ResponseCodeEnum.ILLEGAL_ARGUMENT.getDesc());
        }
        List<Integer> categoryIdList = Lists.newArrayList();

        if (categoryId != null) {
            Category category = categoryMapper.selectByPrimaryKey(categoryId);
            if (category == null && StringUtils.isBlank(keyword)) {
                // 没有该分类,并且还没有关键字,这个时候返回一个空的结果集,不报错
                PageHelper.startPage(pageNum, pageSize);
                List<ProductListVO> productListVO = Lists.newArrayList();
                PageInfo pageInfo = new PageInfo(productListVO);
                return JsonResult.success(pageInfo);
            }
            if (category != null) {
                categoryIdList = categoryService.selectCategoryAndChildrenById(category.getId()).getData();
            }
        }
        if (StringUtils.isNotBlank(keyword)) {
            keyword = "%" + keyword + "%";
        }

        PageHelper.startPage(pageNum, pageSize);
        // 排序处理
        if (StringUtils.isNotBlank(orderBy)) {
            if (Const.ProductListOrderBy.PRICE_ASC_DESC.contains(orderBy)) {
                String[] orderByArray = orderBy.split("_");
                PageHelper.orderBy(orderByArray[0] + " " + orderByArray[1]);
            }
        }
        List<Product> productList = productMapper.selectByNameAndCategoryIds(StringUtils.isBlank(keyword) ? null : keyword, categoryIdList.size() == 0 ? null : categoryIdList);
        PageInfo pageInfo = assemblePageInfo(productList);
        return JsonResult.success(pageInfo);
    }

    /**
     * 组装产品列表的分页信息
     * @param productList 产品列表
     * @return
     */
    private PageInfo assemblePageInfo(List<Product> productList) {
        List<ProductListVO> productListVOList = Lists.newArrayList();
        for (Product productItem : productList) {
            ProductListVO productListVO = assembleProductListVO(productItem);
            productListVOList.add(productListVO);
        }
        return new PageInfo(productListVOList);
    }

    /**
     * 组装产品的VO视图数据
     * @param product 产品
     * @return
     */
    private ProductListVO assembleProductListVO(Product product) {
        ProductListVO productListVO = new ProductListVO();
        productListVO.setId(product.getId());
        productListVO.setName(product.getName());
        productListVO.setCategoryId(product.getCategoryId());
        productListVO.setImageHost(PropertiesUtils.getProperty("ftp.server.http.prefix", "http://image.mall.com/"));
        productListVO.setMainImage(product.getMainImage());
        productListVO.setPrice(product.getPrice());
        productListVO.setSubtitle(product.getSubtitle());
        productListVO.setStatus(product.getStatus());
        return productListVO;
    }
}
