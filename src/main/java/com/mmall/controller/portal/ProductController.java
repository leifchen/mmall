package com.mmall.controller.portal;

import com.github.pagehelper.PageInfo;
import com.mmall.common.JsonResult;
import com.mmall.service.ProductService;
import com.mmall.vo.ProductDetailVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 商品Controller
 * <p>
 * @Author LeifChen
 * @Date 2019-03-01
 */
@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @RequestMapping("/detail.do")
    public JsonResult<ProductDetailVO> detail(Integer productId) {
        return productService.getProductDetail(productId);
    }

    @RequestMapping("/list.do")
    public JsonResult<PageInfo> list(@RequestParam(value = "keyword", required = false) String keyword,
                                     @RequestParam(value = "categoryId", required = false) Integer categoryId,
                                     @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                     @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                     @RequestParam(value = "orderBy", defaultValue = "") String orderBy) {

        return productService.getProductByKeywordCategory(keyword, categoryId, pageNum, pageSize, orderBy);
    }
}
