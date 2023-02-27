package com.reggie.service;

import com.reggie.mapper.DishMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DishService {
    @Autowired
    private DishMapper dishMapper;

    public boolean existsByCategoryId(Long categoryId) {
        Integer count = dishMapper.countByCategoryId(categoryId);
        return count > 0;
    }
}
