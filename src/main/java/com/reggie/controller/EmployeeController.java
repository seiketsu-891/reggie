package com.reggie.controller;

import com.reggie.common.JsonResponse;
import com.reggie.constants.EmployeeConstants;
import com.reggie.controller.support.EmployeeSupport;
import com.reggie.entity.Employee;
import com.reggie.entity.PageResult;
import com.reggie.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/employee")
@Slf4j
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private EmployeeSupport employeeSupport;

    @PostMapping("/login")
    public JsonResponse<Employee> login(HttpServletRequest req, @RequestBody Employee emp) {
        HttpSession session = req.getSession();
        Employee empLogin = employeeService.login(emp, session);
        if (empLogin == null) {
            return JsonResponse.error("用户名密码不匹配");
        }
        return JsonResponse.success(empLogin);
    }

    @PostMapping("/logout")
    public JsonResponse<String> logout(HttpServletRequest req) {
        req.getSession().removeAttribute(EmployeeConstants.SESSION_EMPLOYEE_ID_KEY);
        return JsonResponse.success("退出成功");
    }

    @PostMapping
    public JsonResponse<String> save(@RequestBody Employee employee) {
        Long empId = employeeSupport.getEmpId();
        employeeService.save(employee, empId);
        return JsonResponse.success("新增员工成功");
    }

    @GetMapping("/page")
    public JsonResponse<PageResult<Employee>> page(int page, int pageSize, String name) {
        PageResult<Employee> res = employeeService.page(page, pageSize, name);
        return JsonResponse.success(res);
    }

    @PutMapping
    public JsonResponse<String> update(@RequestBody Employee employee) {
        Long empId = employeeSupport.getEmpId();
        employeeService.updateById(employee, empId);
        return JsonResponse.success("员工信息修改成功");
    }

    @GetMapping("/{id}")
    public JsonResponse<Employee> getById(@PathVariable Long id) {
        Employee employee = employeeService.getById(id);
        if (employee == null) {
            return JsonResponse.error("没有查询到该员工的信息");
        }
        return JsonResponse.success(employee);
    }
}
