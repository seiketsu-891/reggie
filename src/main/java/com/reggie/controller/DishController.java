package com.reggie.controller;

import com.reggie.common.JsonResponse;
import com.reggie.controller.support.EmployeeSupport;
import com.reggie.dto.DishDto;
import com.reggie.entity.PageResult;
import com.reggie.service.DishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/dish")
public class DishController {
    @Autowired
    private DishService dishService;

    @Autowired
    private EmployeeSupport employeeSupport;

    @PostMapping
    public JsonResponse<String> save(@RequestBody DishDto dishDto) {
        Long empId = employeeSupport.getEmpId();
        dishService.saveWithFlavor(dishDto, empId);
        return JsonResponse.success("新增菜品成功");
    }

    // 3.1
    @GetMapping("/page")
    public JsonResponse<PageResult<DishDto>> page(int page, int pageSize, String name) {
        PageResult<DishDto> res = dishService.page(page, pageSize, name);
        return JsonResponse.success(res);
    }

    // 3.1
    @GetMapping("/{id}")
    public JsonResponse<DishDto> getByIdWithFlavor(@PathVariable Long id) {
        DishDto dishDto = dishService.getByIdWithFlavor(id);
        return JsonResponse.success(dishDto);
    }

    // 3.1
    @PutMapping
    public JsonResponse<String> update(@RequestBody DishDto dishDto) {
        Long empId = employeeSupport.getEmpId();
        dishService.updateWithFlavor(dishDto, empId);
        return JsonResponse.success("修改菜品成功");
    }


}
