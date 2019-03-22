package com.mmall.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

/**
 * 订单明细VO
 * <p>
 * @Author LeifChen
 * @Date 2019-03-08
 */
@Getter
@Setter
@ToString
public class OrderItemVO {

    private Long orderNo;
    private Integer productId;
    private String productName;
    private String productImage;
    private BigDecimal currentUnitPrice;
    private Integer quantity;
    private BigDecimal totalPrice;
    private String createTime;
}