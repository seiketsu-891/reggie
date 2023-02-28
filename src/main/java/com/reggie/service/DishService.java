package com.reggie.service;

import com.reggie.dto.DishDto;
import com.reggie.entity.DishFlavor;
import com.reggie.mapper.DishMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class DishService {
    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private DishFlavorService dishFlavorService;

    public boolean existsByCategoryId(Long categoryId) {
        Integer count = dishMapper.countByCategoryId(categoryId);
        return count > 0;
    }

    public void saveWithFlavor(DishDto dishDto, Long empId) {
        // save dish
        LocalDateTime now = LocalDateTime.now();
        dishDto.setCreateTime(now);
        dishDto.setUpdateTime(now);
        dishDto.setCreateUser(empId);
        dishDto.setUpdateUser(empId);
        // dto继承了dish，这里可以直接save传参
        dishMapper.save(dishDto);

        Long dishId = dishDto.getId();
        List<DishFlavor> flavors = dishDto.getFlavors();
        // 这里要用batch！
        // 这里需要做的就是给每个flavor设置一个dish id
        flavors = flavors.stream().map((item) -> {
            item.setCreateTime(now);
            item.setUpdateTime(now);
            item.setCreateUser(empId);
            item.setUpdateUser(empId);
            item.setDishId(dishId);
            return item;
        }).collect(Collectors.toList());
        dishFlavorService.saveBatch(flavors);
    }
}
