package com.reggie.entity;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class Dish implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    //菜品名称
    private String name;
    private Long categoryId;
    private BigDecimal price;
    //商品码
    private String code;
    private String image;
    private String description;
    //0 停售 1 起售
    private Integer status;
    private Integer sort;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private Long createUser;
    private Long updateUser;
}
