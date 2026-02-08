# Spring Boot CRUD Demo

## 项目概述

本项目是一个基于 Spring Boot 4.0.2 的用户管理系统，提供完整的增删改查（CRUD）操作，包含数据验证、异常处理和单元测试。

## 技术栈

| 技术 | 版本 |
|------|------|
| Java | 17 |
| Spring Boot | 4.0.2 |
| MyBatis | 4.0.1 |
| MySQL | 8.0+ |
| Lombok | 1.18+ |
| Maven | 3.6+ |

## 项目结构

```
src/main/java/com/example/demo/
├── DemoApplication.java              # Spring Boot 启动类
├── controller/                       # 控制层
│   └── UserController.java           # REST API 控制器
├── service/                          # 业务逻辑层
│   ├── UserService.java              # 服务接口
│   └── impl/
│       └── UserServiceImpl.java       # 服务实现
├── mapper/                           # 数据访问层
│   └── UserMapper.java               # MyBatis Mapper 接口
├── entity/                           # 实体类
│   └── User.java                     # 用户实体
├── dto/                              # 数据传输对象
│   ├── UserDTO.java                  # 查询响应 DTO
│   └── UserCreateDTO.java            # 创建/更新请求 DTO
└── exception/                        # 异常处理
    └── GlobalExceptionHandler.java   # 全局异常处理器

src/main/resources/
├── application.yml                   # 应用配置文件
└── mapper/
    └── UserMapper.xml                # MyBatis XML 映射文件

src/test/java/com/example/demo/
└── UserControllerTest.java           # 集成测试
```

## 快速开始

### 前置要求

- JDK 17+
- MySQL 8.0+
- Maven 3.6+

### 安装步骤

1. **克隆或下载项目**
   ```bash
   cd ~/java/demo
   ```

2. **创建数据库**
   ```sql
   CREATE DATABASE demo_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
   ```

3. **创建用户表**
   ```sql
   USE demo_db;
   
   CREATE TABLE users (
       id BIGINT AUTO_INCREMENT PRIMARY KEY,
       username VARCHAR(50) NOT NULL UNIQUE,
       email VARCHAR(100) NOT NULL UNIQUE,
       age INT NOT NULL,
       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
       updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
   );
   ```

4. **修改数据库配置** (`application.yml`)
   ```yaml
   spring:
     datasource:
       url: jdbc:mysql://localhost:3306/demo_db?useSSL=false&serverTimezone=UTC&characterEncoding=utf8mb4
       username: root
       password: your_password
   ```

5. **启动应用**
   ```bash
   mvn spring-boot:run
   ```

   应用将在 `http://localhost:8080` 启动

## API 接口

### 1. 创建用户
```http
POST /api/users
Content-Type: application/json

{
  "username": "zhangsan",
  "email": "zhangsan@example.com",
  "age": 25
}
```

**响应** (201 Created):
```json
{
  "id": 1,
  "username": "zhangsan",
  "email": "zhangsan@example.com",
  "age": 25,
  "createdAt": "2026-02-08T10:30:00",
  "updatedAt": "2026-02-08T10:30:00"
}
```

### 2. 查询所有用户
```http
GET /api/users
```

**响应** (200 OK):
```json
[
  {
    "id": 1,
    "username": "zhangsan",
    "email": "zhangsan@example.com",
    "age": 25,
    "createdAt": "2026-02-08T10:30:00",
    "updatedAt": "2026-02-08T10:30:00"
  }
]
```

### 3. 查询单个用户
```http
GET /api/users/{id}
```

### 4. 更新用户
```http
PUT /api/users/{id}
Content-Type: application/json

{
  "username": "zhangsan_new",
  "email": "zhangsan_new@example.com",
  "age": 26
}
```

**响应** (200 OK): 返回更新后的用户信息

### 5. 删除用户
```http
DELETE /api/users/{id}
```

**响应** (204 No Content)

## 测试

### 运行所有测试
```bash
mvn test
```

### 运行特定测试类
```bash
mvn test -Dtest=UserControllerTest
```

### 使用 curl 测试接口
```bash
# 创建用户
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{"username":"zhangsan","email":"zhangsan@example.com","age":25}'

# 查询所有用户
curl http://localhost:8080/api/users

# 查询单个用户
curl http://localhost:8080/api/users/1

# 更新用户
curl -X PUT http://localhost:8080/api/users/1 \
  -H "Content-Type: application/json" \
  -d '{"username":"zhangsan_new","email":"zhangsan_new@example.com","age":26}'

# 删除用户
curl -X DELETE http://localhost:8080/api/users/1
```

## 核心功能

- RESTful API 设计
- 数据验证（使用 Spring Validation）
- 全局异常处理
- MyBatis ORM 映射
- Lombok 简化代码
- 单元测试（MockMvc）
- 驼峰命名自动转换

## 错误响应示例

**验证错误** (400 Bad Request):
```json
{
  "timestamp": "2026-02-08T10:30:00",
  "status": 400,
  "error": "验证失败",
  "message": {
    "username": "用户名不能为空",
    "email": "邮箱格式不正确"
  },
  "path": "/api/users"
}
```

**用户不存在** (404 Not Found):
```json
{
  "timestamp": "2026-02-08T10:30:00",
  "status": 404,
  "error": "业务异常",
  "message": "用户不存在",
  "path": "/api/users/999"
}
```

