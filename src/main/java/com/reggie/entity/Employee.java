package com.reggie.entity;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class Employee implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    private String username;
    private String name;
    private String password;
    private String phone;
    private String sex;
    private String idNumber;
    private Integer status;
    // LocalDateTime是java8中新增，不包含时区信息
   /*
     比Date好的地方在于：
      1. 年月日等 每个字段都有对应的方法进行设置和获取
      2. 不可变,线程安全；
      3. 可以与更多java8新特性兼容使用;
      4. 时区转化方便；
    */
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private Long createUser;
    private Long updateUser;
}
