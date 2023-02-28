package com.reggie.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.reggie.common.JsonResponse;
import com.reggie.constants.EmployeeConstants;
import com.reggie.controller.support.EmployeeSupport;
import com.reggie.entity.Employee;
import com.reggie.entity.PageResult;
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
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
class EmployeeControllerTest {
    @Mock
    private EmployeeService service;
    @Mock
    private EmployeeSupport support;
    @InjectMocks
    private EmployeeController controller;
    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        when(support.getEmpId()).thenReturn(1L);
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

    @Test
    void save() throws Exception {
        Employee employee = new Employee();
        employee.setUsername("test");
        employee.setName("admin");
        employee.setPassword("123456");
        employee.setIdNumber("1");
        employee.setPhone("12233333333");
        employee.setSex("1");

        mockMvc.perform(post("/employee")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(employee)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(1))
                .andExpect(jsonPath("$.data").value("新增员工成功"));
        verify(service, times(1)).save(employee, 1L);
    }

    @Test
    void page() throws Exception {
        ArrayList<Employee> list = new ArrayList<>();
        PageResult<Employee> pageResult = new PageResult<>(5, list);
        when(service.page(1, 5, "a")).thenReturn(pageResult);

        // 报错1：忘记最开头写/
        MvcResult mvcResult = mockMvc.perform(get("/employee/page?page=1&pageSize=5&name=a"))
                .andExpect(status().isOk())
                .andExpect((jsonPath("$.code").value(1)))
                .andReturn();
        String jsonResponses = mvcResult.getResponse().getContentAsString();
        // 报错2： com.fasterxml.jackson.databind.exc.InvalidDefinitionException: Cannot construct instance of `com.reggie.entity.PageResult` (no Creators, like default constructor, exist): cannot deserialize from Object value (no delegate- or property-based Creator)
        // 这个报错意思是说不能构建PageResult，因为没有无参构造。
        JsonResponse<PageResult<Employee>> actual = new ObjectMapper().readValue(jsonResponses, new TypeReference<JsonResponse<PageResult<Employee>>>() {
        });
        PageResult<Employee> actualPageResult = actual.getData();
        assertNotNull(actualPageResult.getTotal());
        assertEquals(5, actualPageResult.getTotal());
        assertNotNull(actualPageResult.getRecords());
        assertEquals(list, actualPageResult.getRecords());
        verify(service, times(1)).page(1, 5, "a");

    }

    @Test
    void update() throws Exception {
        Employee employee = new Employee();
        mockMvc.perform(put("/employee")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(employee)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(1))
                .andExpect(jsonPath("$.data").value("员工信息修改成功"));
        // 报错说argument不同，原因： emp id写错
        verify(service, times(1)).updateById(employee, 1L);
    }

    @Test
    void getById() throws Exception {
        Employee mockEmp = new Employee();
        mockEmp.setId(1L);
        mockEmp.setName("admin");
        mockEmp.setUsername("name");
        when(service.getById(mockEmp.getId())).thenReturn(mockEmp);
        // 报错1： 404，我忘记写/employee
        MvcResult mvcResult = mockMvc.perform(get("/employee/" + mockEmp.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(1))
                .andReturn();
        String jsonString = mvcResult.getResponse().getContentAsString();

        // 报错2： 我这里一开始直接映射为了employee类型
        Employee actual = new ObjectMapper().readValue(jsonString, new TypeReference<JsonResponse<Employee>>() {
        }).getData();
        assertNotNull(actual);
        assertEquals(mockEmp.getId(), actual.getId());
        assertEquals(mockEmp.getName(), actual.getName());
        assertEquals(mockEmp.getUsername(), actual.getUsername());

        verify(service, times(1)).getById(mockEmp.getId());
    }

    @Test
    void getByIdGetNull() throws Exception {
        when(service.getById(1L)).thenReturn(null);
        mockMvc.perform(get("/employee/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.msg").value("没有查询到该员工的信息"));
    }
}