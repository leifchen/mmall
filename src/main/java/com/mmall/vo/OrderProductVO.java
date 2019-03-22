package com.mmall.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.List;

/**
 * 订单商品VO
 * <p>
 * @Author LeifChen
 * @Date 2019-03-11
 */
@Getter
@Setter
@ToString
public class OrderProductVO {

    private List<OrderItemVO> orderItemVOList;
    private BigDecimal productTotalPrice;
    private String imageHost;
}
