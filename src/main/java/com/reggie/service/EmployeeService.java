package com.reggie.service;

import com.reggie.constants.EmployeeConstants;
import com.reggie.entity.Employee;
import com.reggie.entity.PageResult;
import com.reggie.mapper.EmployeeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EmployeeService {
    @Autowired
    private EmployeeMapper employeeMapper;

    public Employee login(Employee emp, HttpSession session) {
        String password = emp.getPassword();
        String encodedPassword = DigestUtils.md5DigestAsHex(password.getBytes());
        Employee empDb = employeeMapper.getByUsername(emp.getUsername());
        if (empDb == null || !empDb.getPassword().equals(encodedPassword) || empDb.getStatus() == 0) {
            return null;
        }
        session.setAttribute(EmployeeConstants.SESSION_EMPLOYEE_ID_KEY, empDb.getId());
        return empDb;
    }

    // 这里只涉及到req的处理，没有业务逻辑，可以直接放到controller中
    //    public void logout(HttpServletRequest req) {
    //        req.getSession().removeAttribute(EmployeeConstants.SESSION_EMPLOYEE_ID_KEY);
    //    }

    public void save(Employee employee, Long empId) {
        // 密码加密
        employee.setPassword(DigestUtils.md5DigestAsHex(EmployeeConstants.DEFAULT_PASSWORD.getBytes()));
        LocalDateTime now = LocalDateTime.now();
        // 设置属性
        employee.setCreateTime(now);
        employee.setUpdateTime(now);
        employee.setStatus(1);
        employee.setCreateUser(empId);
        employee.setUpdateUser(empId);
        employeeMapper.save(employee);
    }

    public PageResult<Employee> page(int page, int pageSize, String name) {
        Map<String, Object> params = new HashMap<>();
        int start = (page - 1) * pageSize;
        params.put("start", start);
        params.put("size", pageSize);
        params.put("name", name);

        List<Employee> list = new ArrayList<>();
        Integer total = employeeMapper.getTotalByName(name);
        if (total > 0) {
            list = employeeMapper.getByNameWithPagination(params);
        }

        PageResult<Employee> pageResult = new PageResult<>(total, list);
        return pageResult;
    }

    public void updateById(Employee employee, Long empId) {
        employee.setUpdateUser(empId);
        employee.setUpdateTime(LocalDateTime.now());
        employeeMapper.updateById(employee);
    }

    public Employee getById(Long id) {
        return employeeMapper.getById(id);
    }
}
