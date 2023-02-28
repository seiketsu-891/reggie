package com.reggie.mapper;

import com.reggie.entity.DishFlavor;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DishFlavorMapper {
    void saveBatch(List<DishFlavor> flavors);
}
