package com.reggie.service;

import com.reggie.dto.DishDto;
import com.reggie.entity.Category;
import com.reggie.entity.Dish;
import com.reggie.entity.DishFlavor;
import com.reggie.entity.PageResult;
import com.reggie.mapper.DishMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class DishService {
    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private CategoryService categoryService;

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

    // review
    public PageResult<DishDto> page(int page, int pageSize, String name) {
        int start = (page - 1) * pageSize;
        Map<String, Object> params = new HashMap<>();
        params.put("start", start);
        params.put("size", pageSize);
        params.put("name", name);

        List<Dish> dishes = new ArrayList();
        Integer total = dishMapper.countByName();
        if (total > 0) {
            dishes = dishMapper.getByNameWithPagination(params);
        }

        PageResult<DishDto> dishDtoPageResult = new PageResult<>();
        dishDtoPageResult.setTotal(total);

        List<DishDto> list = dishes.stream().map((item) -> {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item, dishDto);
            Long catId = item.getCategoryId();
            Category category = categoryService.getByItd(catId);
            if (category != null) {
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }
            return dishDto;
        }).collect(Collectors.toList());
        dishDtoPageResult.setRecords(list);
        return dishDtoPageResult;
    }

    public DishDto getByIdWithFlavor(Long id) {
        Dish dish = dishMapper.getById(id);
        DishDto dishDto = new DishDto();
        BeanUtils.copyProperties(dish, dishDto);

        List<DishFlavor> dishFlavors = dishFlavorService.getByDishId(id);
        dishDto.setFlavors(dishFlavors);
        return dishDto;
    }

    // review
    public void updateWithFlavor(DishDto dishDto, Long empId) {
        LocalDateTime now = LocalDateTime.now();
        dishDto.setUpdateTime(now);
        dishDto.setUpdateUser(empId);
        this.updateById(dishDto);

        dishFlavorService.removeByDishId(dishDto.getId());

        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors = flavors.stream().map((item) -> {
            item.setDishId(dishDto.getId());
            item.setUpdateTime(now);
            item.setUpdateUser(empId);
            return item;
        }).collect(Collectors.toList());
        dishFlavorService.saveBatch(flavors);
    }

    public void updateById(Dish dish) {
        dishMapper.updateById(dish);
    }
}
