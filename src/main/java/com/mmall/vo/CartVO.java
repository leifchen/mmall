package com.mmall.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.List;

/**
 * 购物车VO
 * <p>
 * @Author LeifChen
 * @Date 2019-03-01
 */
@Getter
@Setter
@ToString
public class CartVO {

    private List<CartProductVO> cartProductVOList;
    private BigDecimal cartTotalPrice;
    private Boolean allChecked;
    private String iamgeHost;
}
