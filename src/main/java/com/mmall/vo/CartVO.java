package com.mmall.vo;

import java.math.BigDecimal;
import java.util.List;

/**
 * 购物车VO
 * <p>
 * @Author LeifChen
 * @Date 2019-03-01
 */
public class CartVO {

    private List<CartProductVO> cartProductVOList;
    private BigDecimal cartTotalPrice;
    private Boolean allChecked;
    private String iamgeHost;

    public List<CartProductVO> getCartProductVOList() {
        return cartProductVOList;
    }

    public void setCartProductVOList(List<CartProductVO> cartProductVOList) {
        this.cartProductVOList = cartProductVOList;
    }

    public BigDecimal getCartTotalPrice() {
        return cartTotalPrice;
    }

    public void setCartTotalPrice(BigDecimal cartTotalPrice) {
        this.cartTotalPrice = cartTotalPrice;
    }

    public Boolean getAllChecked() {
        return allChecked;
    }

    public void setAllChecked(Boolean allChecked) {
        this.allChecked = allChecked;
    }

    public String getIamgeHost() {
        return iamgeHost;
    }

    public void setIamgeHost(String iamgeHost) {
        this.iamgeHost = iamgeHost;
    }

    @Override
    public String toString() {
        return "CartVO{" +
                "cartProductVOList=" + cartProductVOList +
                ", cartTotalPrice=" + cartTotalPrice +
                ", allChecked=" + allChecked +
                ", iamgeHost='" + iamgeHost + '\'' +
                '}';
    }
}
