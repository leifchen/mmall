package com.mmall.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.List;

/**
 * 订单VO
 * <p>
 * @Author LeifChen
 * @Date 2019-03-08
 */
@Getter
@Setter
@ToString
public class OrderVO {

    private Long orderNo;
    private BigDecimal payment;
    private Integer paymentType;
    private String paymentTypeDesc;
    private Integer postage;
    private Integer status;
    private String statusDesc;
    private String paymentTime;
    private String sendTime;
    private String endTime;
    private String closeTime;
    private String createTime;

    private List<OrderItemVO> orderItemVOList;

    private String imageHost;
    private Integer shippingId;
    private String receiverName;

    private ShippingVO shippingVO;
}