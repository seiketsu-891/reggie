package com.reggie.service;

import com.reggie.mapper.SetMealMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SetMealService {
    @Autowired
    private SetMealMapper setMealMapper;

    public boolean existsByCategoryId(Long categoryId) {
        Integer count = setMealMapper.countByCategoryId(categoryId);
        return count > 0;
    }
}
