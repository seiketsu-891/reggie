package com.reggie.mapper;

import com.reggie.entity.Category;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface CategoryMapper {
    void save(Category category);

    Integer getTotal();

    List<Category> getAllWithPagination(Map<String, Object> params);

    void delById(Long id);

    void updateById(Category category);

    List<Category> getByType(Integer type);

    Category getById(Long id);
}
