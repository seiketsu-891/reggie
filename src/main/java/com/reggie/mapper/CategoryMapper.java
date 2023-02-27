package com.reggie.mapper;

import com.reggie.entity.Category;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CategoryMapper {
    void save(Category category);
}
