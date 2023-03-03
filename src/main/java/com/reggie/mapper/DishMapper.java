package com.reggie.mapper;

import com.reggie.entity.Dish;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface DishMapper {
    Integer countByCategoryId(Long categoryId);

    void save(Dish dish);

    Integer countByName();

    List<Dish> getByNameWithPagination(Map<String, Object> params);

    Dish getById(Long id);

    void updateById(Dish dish);
}
