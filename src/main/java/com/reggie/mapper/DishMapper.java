package com.reggie.mapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DishMapper {
    Integer countByCategoryId(Long categoryId);
}
