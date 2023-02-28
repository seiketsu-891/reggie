package com.reggie.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.reggie.common.JsonResponse;
import com.reggie.constants.EmployeeConstants;
import com.reggie.entity.Employee;
import com.reggie.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.servlet.http.HttpSession;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
class EmployeeControllerTest {
    @Mock
    private EmployeeService service;
    @InjectMocks
    private EmployeeController controller;
    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @DisplayName("测试登录成功")
    @Test
    void loginSuccessfully() throws Exception {
        // mock请求数据
        Employee employee = new Employee();
        employee.setUsername("admin");
        employee.setPassword("123456");

        // mock 返回的emp数据
        Employee empMock = new Employee();
        empMock.setUsername("admin");
        empMock.setPassword("123456");
        empMock.setId(1L);
        empMock.setStatus(1);

        when(service.login(any(Employee.class), any(HttpSession.class))).thenReturn(empMock);

        // 这里的session添加在service已经测试过，在这里仍然需要测试
        // 这里的session是模拟的，所以需要自己手动设置值
        // 还是忽略了一点是：这里的service是模拟的，它不可能帮你添加属性
        MockHttpSession session = new MockHttpSession();
        session.setAttribute(EmployeeConstants.SESSION_EMPLOYEE_ID_KEY, empMock.getId());

        MvcResult result = mockMvc.perform(post("/employee/login")
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(employee)))
                .andExpect(request().sessionAttribute(EmployeeConstants.SESSION_EMPLOYEE_ID_KEY, empMock.getId()))
                .andReturn();
        String jsonResponse = result.getResponse().getContentAsString();
        JsonResponse<Employee> actual = new ObjectMapper().readValue(jsonResponse, new TypeReference<JsonResponse<Employee>>() {
        });

        assertEquals(1, actual.getCode());
        Employee actualEmp = actual.getData();
        assertNotNull(actualEmp);
        assertEquals(empMock.getId(), actualEmp.getId());
        assertEquals(empMock.getUsername(), actualEmp.getUsername());
        assertEquals(empMock.getStatus(), actualEmp.getStatus());

        verify(service, times(1)).login(any(Employee.class), any(HttpSession.class));
    }


    @DisplayName("测试登录失败")
    @Test
    void loginFailed() throws Exception {
        // mock请求数据
        Employee employee = new Employee();
        employee.setUsername("admin");
        employee.setPassword("1234567");

        mockMvc.perform(post("/employee/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(employee)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.msg").value("用户名密码不匹配"));
    }

    @DisplayName("测试退出登录")
    @Test
    void logout() throws Exception {
        // 这里不能使用new来创建， 如果不调用verify验证session的方法， 是可以的。
//        MockHttpSession mockSession = new MockHttpSession();
        MockHttpSession mockSession = mock(MockHttpSession.class);
        mockMvc.perform(post("/employee/logout")
                        .session(mockSession))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(1))
                .andExpect(jsonPath("$.data").value("退出成功"));
        verify(mockSession, times(1)).removeAttribute(EmployeeConstants.SESSION_EMPLOYEE_ID_KEY);
    }
}