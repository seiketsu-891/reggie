package com.reggie.service;

import com.reggie.entity.Employee;
import com.reggie.mapper.EmployeeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

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
        session.setAttribute("employee", emp.getId());
        return empDb;
    }

    public void logout(HttpServletRequest req) {
        req.getSession().removeAttribute("employee");
    }
}
