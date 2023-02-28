package com.reggie.service;

import com.reggie.constants.EmployeeConstants;
import com.reggie.entity.Employee;
import com.reggie.mapper.EmployeeMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;

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
}