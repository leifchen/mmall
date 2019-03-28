package com.mmall.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 收货地址VO
 * <p>
 * @Author LeifChen
 * @Date 2019-03-08
 */
@Getter
@Setter
@ToString
public class ShippingVO {

    private String receiverName;
    private String receiverPhone;
    private String receiverMobile;
    private String receiverProvince;
    private String receiverCity;
    private String receiverDistrict;
    private String receiverAddress;
    private String receiverZip;
}
