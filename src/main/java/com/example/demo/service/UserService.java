package com.example.demo.service;

import java.util.List;

import com.example.demo.dto.UserCreateDTO;
import com.example.demo.dto.UserDTO;

public interface UserService {

    // 创建用户
    UserDTO createUser(UserCreateDTO userCreateDTO);

    // 查询所有用户
    List<UserDTO> getAllUsers();

    // 根据ID查询用户
    UserDTO getUserById(Long id);

    // 更新用户
    UserDTO updateUser(Long id, UserCreateDTO userCreateDTO);

    // 删除用户
    void deleteUser(Long id);
}
