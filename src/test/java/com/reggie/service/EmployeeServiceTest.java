package com.reggie.service;

import com.reggie.constants.EmployeeConstants;
import com.reggie.entity.Employee;
import com.reggie.entity.PageResult;
import com.reggie.mapper.EmployeeMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.util.DigestUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class EmployeeServiceTest {
    MockHttpSession session;
    @Mock
    private EmployeeMapper mapper;
    @InjectMocks
    private EmployeeService service;


    @BeforeEach
    void setup() {
        // 这句的作用是执行上面那些注释，比如mock， inject mocks，
        // 在新版本中，不调用这句也可以
        /*
        In general, it's good practice to call MockitoAnnotations.openMocks(this) in your test class to ensure that your mock objects are properly initialized, even if it's not strictly necessary. This helps to ensure that your tests are consistent and predictable, and makes it easier for other developers to understand your code.
         */
        MockitoAnnotations.openMocks(this);
        /*
          在这里进行session的初始化的话，每个test case会有一个新的session实例。
          如果在上面直接加@Mock， 所有test case共享一个session
          此外，当用@Mock时，它并不具备MockHttpSession的真正功能；
          就像上面mapper是虚假的，它需要我们自己去指定它的返回值
          所以，如果使用@Mock， login方法不会真正给session添加属性值;
        */
        session = new MockHttpSession();
    }

    @DisplayName("service: 测试登录成功")
    @Test
    void loginSuccessfully() {
        // 模拟参数
        Employee emp = new Employee();
        emp.setUsername("admin");
        emp.setPassword("123456");

        // 模拟mapper返回值
        Employee mockEmp = new Employee();
        mockEmp.setId(1L);
        mockEmp.setUsername("admin");
        mockEmp.setStatus(1);
        mockEmp.setPassword("e10adc3949ba59abbe56e057f20f883e");
        when(mapper.getByUsername("admin")).thenReturn(mockEmp);

        Employee actual = service.login(emp, session);

        assertNotNull(actual);
        assertEquals(mockEmp.getUsername(), actual.getUsername());
        assertEquals(mockEmp.getId(), actual.getId());
        assertEquals(mockEmp.getId(), session.getAttribute(EmployeeConstants.SESSION_EMPLOYEE_ID_KEY));

        verify(mapper, times(1)).getByUsername(any(String.class));
    }

    @DisplayName("service: 测试登录失败")
    @Test
    void testLoginFailed() {
        Employee emp = new Employee();
        emp.setUsername("admin");
        emp.setPassword("1234567");

        Employee actual = service.login(emp, session);
        assertNull(actual);
        // 这里编辑器会提示：The call to 'assertNull' always fails, according to its method contracts
        // 应该是跟assertNull的行为有关，可以忽略这个提示
        assertNull(session.getAttribute(EmployeeConstants.SESSION_EMPLOYEE_ID_KEY));
        verify(mapper, times(1)).getByUsername(any(String.class));
    }

    @Test
    void save() {
        Employee employee = new Employee();
        employee.setUsername("test");
        employee.setName("admin");
        employee.setPassword("123456");
        employee.setIdNumber("1");
        employee.setPhone("12233333333");
        employee.setSex("1");

        service.save(employee, 1L);
        // 这里employee传入service的save方法就会被改变;
        assertEquals(DigestUtils.md5DigestAsHex("123456".getBytes()), employee.getPassword());
        assertNotNull(employee.getCreateTime());
        assertNotNull(employee.getUpdateTime());
        assertEquals(1, employee.getStatus());
        assertEquals(1L, employee.getCreateUser());
        assertEquals(1L, employee.getUpdateUser());
        when(mapper.save(any(Employee.class))).thenReturn(1);
    }

    @Test
    void page() {
        /*
         *  这里一开始的疑问是我要如何确定params是对的？
         *  后来自己想的是：我手动建一个param，然后确保调用这个param的方法被执行
         */

        /*
         *  一开始发现我忘记加total的then return
         *  后来补上发现结果还是【】
         */

        /*
         * 我一开始甚至怀疑是否我的map内存地址和调用参数的map内存地址不是同一个，所以验证失败
         * 后来发现只要key-value一样就可以
         */
        ArrayList<Employee> emps = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Employee employee = new Employee();
            employee.setUsername("test");
            employee.setName("admin");
            emps.add(employee);
        }

        Map<String, Object> params = new HashMap<>();
        params.put("start", 5);
        params.put("size", 5);
        params.put("name", "admin");
        when(mapper.getTotalByName("admin")).thenReturn(emps.size());
        when(mapper.getByNameWithPagination(params)).thenReturn(emps);
        PageResult<Employee> actual = service.page(2, 5, "admin");
        assertEquals(emps, actual.getRecords());
        assertEquals(5, actual.getTotal());

        verify(mapper, times(1)).getTotalByName("admin");
        verify(mapper, times(1)).getByNameWithPagination(params);
    }

    @Test
    void updateById() {
        Employee employee = new Employee();
        employee.setUsername("test");
        employee.setName("admin");
        employee.setIdNumber("1");
        employee.setPhone("12233333333");
        employee.setSex("1");
        LocalDateTime updateTime = LocalDateTime.now();
        employee.setUpdateTime(updateTime);

        when(mapper.updateById(any(Employee.class))).thenReturn(1);

        service.updateById(employee, 2L);

        assertNotEquals(updateTime, employee.getUpdateTime());
        assertEquals(2L, employee.getUpdateUser());
    }

    @Test
    void getById() {
        Employee employee = new Employee();
        employee.setUsername("test");
        employee.setName("admin");
        employee.setId(1L);

        when(mapper.getById(employee.getId())).thenReturn(employee);
        Employee actual = service.getById(employee.getId());

        assertNotNull(actual);
        assertEquals(employee.getId(), actual.getId());
        assertEquals(employee.getUsername(), actual.getUsername());
        assertEquals(employee.getName(), actual.getName());
        verify(mapper, times(1)).getById(any(Long.class));
    }
}