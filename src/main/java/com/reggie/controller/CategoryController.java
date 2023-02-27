package com.reggie.controller;

import com.reggie.common.JsonResponse;
import com.reggie.entity.Category;
import com.reggie.entity.PageResult;
import com.reggie.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/page")
    public JsonResponse<PageResult<Category>> page(int page, int pageSize) {
        PageResult<Category> res = categoryService.page(page, pageSize);
        return JsonResponse.success(res);
    }

    @DeleteMapping
    public JsonResponse<String> delete(Long id) {
        categoryService.delById(id);
        return JsonResponse.success("分类信息删除成功");
    }
}
