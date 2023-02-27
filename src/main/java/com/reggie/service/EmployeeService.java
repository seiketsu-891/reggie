package com.reggie.service;

import com.reggie.constants.EmployeeConstants;
import com.reggie.entity.Employee;
import com.reggie.mapper.EmployeeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;

@Service
public class EmployeeService {
    @Autowired
    private EmployeeMapper employeeMapper;

    public Employee login(HttpServletRequest req, Employee emp) {
        String password = emp.getPassword();
        String encodedPassword = DigestUtils.md5DigestAsHex(password.getBytes());
        Employee empDb = employeeMapper.getEmpByUsername(emp.getUsername());
        if (empDb == null || !empDb.getPassword().equals(encodedPassword) || empDb.getStatus() == 0) {
            return null;
        }
        HttpSession session = req.getSession();
        session.setAttribute(EmployeeConstants.SESSION_EMPLOYEE_ID_KEY, empDb.getId());
        return empDb;
    }

    public void logout(HttpServletRequest req) {
        req.getSession().removeAttribute(EmployeeConstants.SESSION_EMPLOYEE_ID_KEY);
    }

    public void save(HttpServletRequest req, Employee employee) {
        // 密码加密
        employee.setPassword(DigestUtils.md5DigestAsHex(EmployeeConstants.DEFAULT_PASSWORD.getBytes()));
        LocalDateTime now = LocalDateTime.now();
        // 设置属性
        employee.setCreateTime(now);
        employee.setUpdateTime(now);
        employee.setStatus(1);
        Long empId = (Long) req.getSession().getAttribute(EmployeeConstants.SESSION_EMPLOYEE_ID_KEY);
        employee.setCreateUser(empId);
        employee.setUpdateUser(empId);
        employeeMapper.save(employee);
    }
}
