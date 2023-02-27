package com.reggie.mapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SetMealMapper {
    Integer countByCategoryId(Long categoryId);
}
