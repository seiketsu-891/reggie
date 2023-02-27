package com.reggie.service;

import com.reggie.constants.EmployeeConstants;
import com.reggie.entity.Category;
import com.reggie.entity.PageResult;
import com.reggie.mapper.CategoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;

    public void save(HttpServletRequest req, Category category) {
        Long empId = (Long) req.getSession().getAttribute(EmployeeConstants.SESSION_EMPLOYEE_ID_KEY);
        LocalDateTime now = LocalDateTime.now();
        category.setCreateUser(empId);
        category.setUpdateUser(empId);
        category.setCreateTime(now);
        category.setUpdateTime(now);
        categoryMapper.save(category);
    }

    public PageResult<Category> page(int page, int pageSize) {
        Map<String, Object> params = new HashMap<>();
        int start = (page - 1) * pageSize;
        params.put("start", start);
        params.put("size", pageSize);

        List<Category> list = new ArrayList<>();
        Integer total = categoryMapper.getTotal();
        if (total > 0) {
            list = categoryMapper.getAllWithPagination(params);
        }

        PageResult<Category> pageResult = new PageResult<>(total, list);
        return pageResult;
    }
}