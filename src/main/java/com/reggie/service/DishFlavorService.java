package com.reggie.service;

import com.reggie.entity.DishFlavor;
import com.reggie.mapper.DishFlavorMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DishFlavorService {
    @Autowired
    private DishFlavorMapper dishFlavorMapper;

    public void saveBatch(List<DishFlavor> flavors) {
        dishFlavorMapper.saveBatch(flavors);
    }

    public List<DishFlavor> getByDishId(Long dishId) {
        return dishFlavorMapper.getByDishId(dishId);
    }

    public void removeByDishId(Long dishId) {
        dishFlavorMapper.removeByDishId(dishId);
    }
}
