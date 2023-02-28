package com.reggie.controller.support;

import com.reggie.common.CustomException;
import com.reggie.constants.EmployeeConstants;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpSession;

@Component
public class EmployeeSupport {
    public Long getEmpId() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpSession session = requestAttributes.getRequest().getSession();
        Long userId = (Long) session.getAttribute(EmployeeConstants.SESSION_EMPLOYEE_ID_KEY);
        if (userId == null) {
            throw new CustomException("NOTLOGIN");
        }
        return userId;
    }

}
