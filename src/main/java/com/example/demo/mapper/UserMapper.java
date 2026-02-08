package com.example.demo.mapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public class UserMapper {

    // 创建用户
    int insertUser(User user);

    // 查询所有用户
    List<User> selectAllUsers();

    // 根据ID查询用户
    User selectUserById(Long id);

    // 更新用户
    int updateUser(User user);

    // 删除用户
    int deleteUserById(Long id);

}
