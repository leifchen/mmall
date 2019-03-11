package com.mmall.vo;

import java.math.BigDecimal;
import java.util.List;

/**
 * 订单商品VO
 * <p>
 * @Author LeifChen
 * @Date 2019-03-11
 */
public class OrderProductVO {

    private List<OrderItemVO> orderItemVOList;
    private BigDecimal productTotalPrice;
    private String iamgeHost;

    public List<OrderItemVO> getOrderItemVOList() {
        return orderItemVOList;
    }

    public void setOrderItemVOList(List<OrderItemVO> orderItemVOList) {
        this.orderItemVOList = orderItemVOList;
    }

    public BigDecimal getProductTotalPrice() {
        return productTotalPrice;
    }

    public void setProductTotalPrice(BigDecimal productTotalPrice) {
        this.productTotalPrice = productTotalPrice;
    }

    public String getIamgeHost() {
        return iamgeHost;
    }

    public void setIamgeHost(String iamgeHost) {
        this.iamgeHost = iamgeHost;
    }

    @Override
    public String toString() {
        return "OrderProductVO{" +
                "orderItemVOList=" + orderItemVOList +
                ", productTotalPrice=" + productTotalPrice +
                ", iamgeHost='" + iamgeHost + '\'' +
                '}';
    }
}
