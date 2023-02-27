package com.reggie.controller;

import com.reggie.common.JsonResponse;
import com.reggie.entity.Category;
import com.reggie.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public JsonResponse<String> save(HttpServletRequest req, @RequestBody Category category) {
        categoryService.save(req, category);
        return JsonResponse.success("新增分类成功");
    }
}
