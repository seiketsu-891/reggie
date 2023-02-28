package com.reggie.mapper;

import com.reggie.entity.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

//@MybatisTest这个注解在spring boot 2.1及以上中已经被自动整合
@SpringBootTest
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class EmployeeMapperTest {
    @Autowired
    private EmployeeMapper mapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @DisplayName("测试根据用户名获取用户信息")
    @Test
    void testGetByUsername() {
        Employee actual = mapper.getByUsername("admin");
        assertNotNull(actual);
        assertEquals(1L, actual.getId());
        assertEquals("管理员", actual.getName());
        assertEquals("admin", actual.getUsername());
    }

    @Test
    void save() {
        Employee employee = new Employee();
        employee.setUsername("admin--test");
        employee.setName("管理员test");
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
        employee.setStatus(1);
        LocalDateTime now = LocalDateTime.now();
        employee.setCreateUser(1L);
        employee.setUpdateUser(1L);
        employee.setCreateTime(now);
        employee.setUpdateTime(now);
        employee.setIdNumber("1");
        employee.setPhone("12233333333");
        employee.setSex("1");
        assertEquals(1, mapper.save(employee));

        employee.setUsername("admin");
        /*
        DuplicateKeyException 是 MyBatis 对于 SQLIntegrityConstraintViolationException 的封装，所以当 MyBatis 检测到数据库抛出了 SQLIntegrityConstraintViolationException 异常时，它会将其转换为 DuplicateKeyException 异常并抛出，这就是为什么你的测试用例中需要使用 DuplicateKeyException。
        在实际的开发中，应该捕获 DuplicateKeyException 异常，因为它是 MyBatis 的规范异常，而不是特定数据库驱动程序引发的异常。这样可以使代码更加具有可移植性。
         */
        assertThrows(DuplicateKeyException.class, () -> {
            mapper.save(employee);
        });
    }

    @DisplayName("mapper: 测试根据name获取总数")
    @Test
    void getTotalByName() {
        for (int i = 0; i < 10; i++) {
            Employee employee = new Employee();
            employee.setUsername("test-" + i);
            employee.setName("管理员");
            employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
            employee.setStatus(1);
            LocalDateTime now = LocalDateTime.now();
            employee.setCreateUser(1L);
            employee.setUpdateUser(1L);
            employee.setCreateTime(now);
            employee.setUpdateTime(now);
            employee.setIdNumber("1");
            employee.setPhone("12233333333");
            employee.setSex("1");
            mapper.save(employee);
        }
        Integer count = mapper.getTotalByName("");
        assertEquals(35, count);
    }

    @Test
    void getByNameWithPagination() {
        for (int i = 0; i < 10; i++) {
            Employee employee = new Employee();
            employee.setUsername("test" + i);
            employee.setName("管理员");
            employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
            employee.setStatus(1);
            LocalDateTime now = LocalDateTime.now();
            employee.setCreateUser(1L);
            employee.setUpdateUser(1L);
            employee.setCreateTime(now);
            employee.setUpdateTime(now);
            employee.setIdNumber("1");
            employee.setPhone("12233333333");
            employee.setSex("1");
            mapper.save(employee);
        }
        String name = "";
        Map<String, Object> params = new HashMap<>();
        params.put("start", 0);
        params.put("size", 5);
        params.put(name, name);
        List<Employee> actualPage1 = mapper.getByNameWithPagination(params);
        assertEquals(5, actualPage1.size());

        params.put("start", 10);
        params.put("size", 5);
        List<Employee> actualPage2 = mapper.getByNameWithPagination(params);
        assertEquals(4, actualPage2.size());

        name = "管理员";
        params.put("name", name);
        params.put("start", 0);
        params.put("size", 14);
        List<Employee> actualPage1WithNameFilter = mapper.getByNameWithPagination(params);
        for (Employee employee : actualPage1WithNameFilter) {
            System.out.println(employee);
        }
        assertEquals(13, actualPage1WithNameFilter.size());
    }

    @Test
    void getById() {
        Employee employee = new Employee();
        employee.setId(1L);
        employee.setUsername("admin");
        employee.setName("管理员");
        Employee actual = mapper.getById(employee.getId());
        assertEquals(employee.getId(), actual.getId());
        assertEquals(employee.getUsername(), actual.getUsername());
        assertEquals(employee.getName(), actual.getName());
    }

    @Test
    void updateById() {
        Employee employee = new Employee();
        employee.setId(1L);
        employee.setUsername("admin");
        employee.setName("管理员");
        employee.setStatus(0);
        Integer count = mapper.updateById(employee);
        assertEquals(1, count);
        assertEquals(0, mapper.getById(1L).getStatus());
    }
}
