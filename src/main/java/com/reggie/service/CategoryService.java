package com.reggie.service;

import com.reggie.constants.EmployeeConstants;
import com.reggie.entity.Category;
import com.reggie.mapper.CategoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

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
}