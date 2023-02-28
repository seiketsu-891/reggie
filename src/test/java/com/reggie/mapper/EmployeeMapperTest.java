package com.reggie.mapper;

import com.reggie.entity.Employee;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

//@MybatisTest这个注解在spring boot 2.1及以上中已经被自动整合
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class EmployeeMapperTest {
    @Autowired
    private EmployeeMapper mapper;

    @DisplayName("测试根据用户名获取用户信息")
    @Test
    void testGetByUsername() {
        Employee actual = mapper.getByUsername("admin");
        assertNotNull(actual);
        assertEquals(1L, actual.getId());
        assertEquals("管理员", actual.getName());
        assertEquals("admin", actual.getUsername());
    }
}
