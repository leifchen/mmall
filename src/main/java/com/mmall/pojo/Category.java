package com.mmall.pojo;

import lombok.*;

import java.util.Date;

/**
 * 商品类别
 * <p>
 * @Author LeifChen
 * @Date 2019-02-25
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Category {
    private Integer id;

    private Integer parentId;

    private String name;

    private Boolean status;

    private Integer sortOrder;

    private Date createTime;

    private Date updateTime;
}