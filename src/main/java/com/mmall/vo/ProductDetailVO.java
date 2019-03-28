package com.mmall.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

/**
 * 商品详情VO
 * <p>
 * @Author LeifChen
 * @Date 2019-02-28
 */
@Getter
@Setter
@ToString
public class ProductDetailVO {

    private Integer id;
    private Integer categoryId;
    private String name;
    private String subtitle;
    private String mainImage;
    private String subImages;
    private String detail;
    private BigDecimal price;
    private Integer stock;
    private Integer status;
    private String createTime;
    private String updateTime;
    private String imageHost;
    private Integer parentCategoryId;
}
