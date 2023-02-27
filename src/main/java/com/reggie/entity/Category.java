package com.reggie.entity;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class Category implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    //类型 1 菜品分类 2 套餐分类
    private Integer type;
    //分类名称
    private String name;
    //顺序
    private Integer sort;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private Long createUser;
    private Long updateUser;
}
