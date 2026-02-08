package com.example.demo;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.example.demo.controller.UserController;
import com.example.demo.dto.UserCreateDTO;
import com.example.demo.dto.UserDTO;
import com.example.demo.service.UserService;

import tools.jackson.databind.ObjectMapper;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    private UserDTO userDTO;
    private UserCreateDTO userCreateDTO;

    @BeforeEach
    public void setup() {
        userDTO = new UserDTO();
        userDTO.setId(1L);
        userDTO.setUsername("zhangsan");
        userDTO.setEmail("zhangsan@example.com");
        userDTO.setAge(25);
        userDTO.setCreatedAt(LocalDateTime.now());
        userDTO.setUpdatedAt(LocalDateTime.now());

        userCreateDTO = new UserCreateDTO();
        userCreateDTO.setUsername("zhangsan");
        userCreateDTO.setEmail("zhangsan@example.com");
        userCreateDTO.setAge(25);
    }

    /**
     * 测试创建用户
     */
    @Test
    public void testCreateUser() throws Exception {
        when(userService.createUser(any(UserCreateDTO.class)))
                .thenReturn(userDTO);

        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userCreateDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.username").value("zhangsan"))
                .andExpect(jsonPath("$.email").value("zhangsan@example.com"))
                .andExpect(jsonPath("$.age").value(25));

        verify(userService, times(1)).createUser(any(UserCreateDTO.class));
    }

    /**
     * 测试查询所有用户
     */
    @Test
    public void testGetAllUsers() throws Exception {
        List<UserDTO> userList = Arrays.asList(userDTO);
        when(userService.getAllUsers()).thenReturn(userList);

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].username").value("zhangsan"));

        verify(userService, times(1)).getAllUsers();
    }

    /**
     * 测试根据ID查询用户
     */
    @Test
    public void testGetUserById() throws Exception {
        when(userService.getUserById(1L)).thenReturn(userDTO);

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.username").value("zhangsan"));

        verify(userService, times(1)).getUserById(1L);
    }

    /**
     * 测试更新用户
     */
    @Test
    public void testUpdateUser() throws Exception {
        UserDTO updatedUser = new UserDTO();
        updatedUser.setId(1L);
        updatedUser.setUsername("zhangsan_new");
        updatedUser.setEmail("zhangsan_new@example.com");
        updatedUser.setAge(26);
        updatedUser.setCreatedAt(LocalDateTime.now());
        updatedUser.setUpdatedAt(LocalDateTime.now());

        UserCreateDTO updateDTO = new UserCreateDTO();
        updateDTO.setUsername("zhangsan_new");
        updateDTO.setEmail("zhangsan_new@example.com");
        updateDTO.setAge(26);

        when(userService.updateUser(eq(1L), any(UserCreateDTO.class)))
                .thenReturn(updatedUser);

        mockMvc.perform(put("/api/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("zhangsan_new"))
                .andExpect(jsonPath("$.age").value(26));

        verify(userService, times(1)).updateUser(eq(1L), any(UserCreateDTO.class));
    }

    /**
     * 测试删除用户
     */
    @Test
    public void testDeleteUser() throws Exception {
        doNothing().when(userService).deleteUser(1L);

        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isNoContent());

        verify(userService, times(1)).deleteUser(1L);
    }

    /**
     * 测试创建用户 - 验证失败（用户名为空）
     */
    @Test
    public void testCreateUserValidationFailed() throws Exception {
        UserCreateDTO invalidUser = new UserCreateDTO();
        invalidUser.setUsername(""); // 为空
        invalidUser.setEmail("test@example.com");
        invalidUser.setAge(25);

        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidUser)))
                .andExpect(status().isBadRequest());
    }

    /**
     * 测试查询不存在的用户
     */
    @Test
    public void testGetUserNotFound() throws Exception {
        when(userService.getUserById(999L))
                .thenThrow(new RuntimeException("用户不存在"));

        mockMvc.perform(get("/api/users/999"))
                .andExpect(status().isNotFound());

        verify(userService, times(1)).getUserById(999L);
    }
}