package com.reggie.mapper;

import com.reggie.entity.Employee;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface EmployeeMapper {
    Employee getEmpByUsername(String username);

    void save(Employee employee);

    Integer getTotalByName(String name);

    List<Employee> getEmpByNameWithPagination(Map<String, Object> params);

    void updateStatusById(Employee employee);
}
