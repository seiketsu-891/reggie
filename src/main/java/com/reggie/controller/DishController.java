package com.reggie.controller;

import com.reggie.common.JsonResponse;
import com.reggie.controller.support.EmployeeSupport;
import com.reggie.dto.DishDto;
import com.reggie.service.DishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dish")
public class DishController {
    @Autowired
    private DishService dishService;

    @Autowired
    private EmployeeSupport employeeSupport;

    @PostMapping
    public JsonResponse<String> savie(@RequestBody DishDto dishDto) {
        Long empId = employeeSupport.getEmpId();
        dishService.saveWithFlavor(dishDto, empId);
        return JsonResponse.success("新增菜品成功");
    }
}
