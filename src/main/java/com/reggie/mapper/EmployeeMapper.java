package com.reggie.mapper;

import com.reggie.entity.Employee;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface EmployeeMapper {
    Employee getByUsername(String username);

    void save(Employee employee);

    Integer getTotalByName(String name);

    List<Employee> getByNameWithPagination(Map<String, Object> params);

//    void updateStatusById(Employee employee);

    Employee getById(Long id);

    void update(Employee employee);
}
