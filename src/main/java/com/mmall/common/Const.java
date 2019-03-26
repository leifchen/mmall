package com.mmall.common;

import com.google.common.collect.Sets;

import java.util.Set;

/**
 * 常量
 * <p>
 * @Author LeifChen
 * @Date 2019-02-26
 */
public class Const {

    public static final String CURRENT_USER = "currentUser";
    public static final String TOKEN_PREFIX = "token_";

    public static final String USERNAME = "username";
    public static final String EMAIL = "email";

    public interface RedisCacheExtime {
        /**
         * 30分钟
         */
        int REDIS_SESSION_EXTIME = 60 * 30;
    }

    public interface Role {
        /**
         * 管理员
         */
        int ROLE_ADMIN = 0;
        /**
         * 普通用户
         */
        int ROLE_CUSTOMER = 1;
    }

    public interface ProductListOrderBy {
        Set<String> PRICE_ASC_DESC = Sets.newHashSet("price_desc", "price_asc");
    }

    public interface Cart {
        /**
         * 未选中状态
         */
        int UN_CHECKED = 0;
        /**
         * 选中状态
         */
        int CHECKED = 1;

        String LIMIT_NUM_FAIL = "LIMIT_NUM_FAIL";
        String LIMIT_NUM_SUCCESS = "LIMIT_NUM_SUCCESS";
    }

    public interface AlipayCallback {
        String TRADE_STATUS_TRADE_SUCCESS = "TRADE_SUCCESS";

        String RESPONSE_SUCCESS = "success";
        String RESPONSE_FAILED = "failed";
    }

    public enum ProductStatusEnum {
        /**
         * 销售在线状态
         */
        ON_SALE(1, "在线");

        private int code;
        private String value;

        ProductStatusEnum(int code, String value) {
            this.code = code;
            this.value = value;
        }

        public int getCode() {
            return code;
        }

        public String getValue() {
            return value;
        }
    }

    public enum OrderStatusEnum {
        /**
         * 已取消
         */
        CANCELED(0, "已取消"),
        /**
         * 未支付
         */
        NO_PAY(10, "未支付"),
        /**
         * 已付款
         */
        PAID(20, "已付款"),
        /**
         * 已发货
         */
        SHIPPED(40, "已发货");

        private int code;
        private String value;

        OrderStatusEnum(int code, String value) {
            this.code = code;
            this.value = value;
        }

        public static OrderStatusEnum codeOf(int code) {
            for (OrderStatusEnum orderStatusEnum : values()) {
                if (orderStatusEnum.getCode() == code) {
                    return orderStatusEnum;
                }
            }
            throw new RuntimeException("没有找到对应的枚举");
        }

        public int getCode() {
            return code;
        }

        public String getValue() {
            return value;
        }
    }

    public enum PaymentTypeEnum {
        /**
         * 在线支付
         */
        ONLINE_PAY(1, "在线支付");

        private int code;
        private String value;

        PaymentTypeEnum(int code, String value) {
            this.code = code;
            this.value = value;
        }

        public static PaymentTypeEnum codeOf(int code) {
            for (PaymentTypeEnum paymentTypeEnum : values()) {
                if (paymentTypeEnum.getCode() == code) {
                    return paymentTypeEnum;
                }
            }
            throw new RuntimeException("没有找到对应的枚举");
        }

        public int getCode() {
            return code;
        }

        public String getValue() {
            return value;
        }
    }

    public enum PayPlatformEnum {
        /**
         * 支付宝
         */
        ALIPAY(1, "支付宝");

        private int code;
        private String value;

        PayPlatformEnum(int code, String value) {
            this.code = code;
            this.value = value;
        }

        public int getCode() {
            return code;
        }

        public String getValue() {
            return value;
        }
    }
}
