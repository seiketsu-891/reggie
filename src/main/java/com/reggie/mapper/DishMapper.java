package com.reggie.mapper;

import com.reggie.entity.Dish;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DishMapper {
    Integer countByCategoryId(Long categoryId);

    void save(Dish dish);
}
