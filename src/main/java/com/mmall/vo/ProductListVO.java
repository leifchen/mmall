package com.mmall.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

/**
 * 商品VO
 * <p>
 * @Author LeifChen
 * @Date 2019-02-28
 */
@Getter
@Setter
@ToString
public class ProductListVO {

    private Integer id;
    private Integer categoryId;
    private String name;
    private String subtitle;
    private String mainImage;
    private BigDecimal price;
    private Integer status;
    private String imageHost;
}
