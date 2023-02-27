package com.reggie.controller;

import com.reggie.common.JsonResponse;
import com.reggie.entity.Employee;
import com.reggie.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/employee")
@Slf4j
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    @PostMapping("/login")
    public JsonResponse<Employee> login(HttpServletRequest req, @RequestBody Employee emp) {
        Employee empLogin = employeeService.login(req, emp);
        return JsonResponse.success(empLogin);
    }

    @PostMapping("/logout")
    public JsonResponse<String> logout(HttpServletRequest req) {
        employeeService.logout(req);
        return JsonResponse.success("退出成功");
    }
}
