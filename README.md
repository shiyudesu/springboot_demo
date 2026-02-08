# Spring Boot CRUD Demo 项目

## 项目简介

这是一个基于 Spring Boot 的数据库 CRUD（增删改查）操作示例项目，用于学习和练习 Spring Boot 与数据库交互的基本操作。

## 技术栈

- **Java**: 17+
- **Spring Boot**: 3.2.x
- **MyBatis**: 数据持久化框架
- **MySQL**: 8.0+ (可替换为其他数据库)
- **Maven**: 项目构建工具
- **Lombok**: 简化 Java 代码
- **Spring Boot Validation**: 数据验证

## 项目结构

```
src/
├── main/
│   ├── java/
│   │   └── com/
│   │       └── example/
│   │           └── demo/
│   │               ├── DemoApplication.java          # 启动类
│   │               ├── controller/                   # 控制器层
│   │               │   └── UserController.java
│   │               ├── service/                      # 服务层
│   │               │   ├── UserService.java
│   │               │   └── impl/
│   │               │       └── UserServiceImpl.java
│   │               ├── mapper/                       # MyBatis Mapper层
│   │               │   └── UserMapper.java
│   │               ├── entity/                       # 实体类
│   │               │   └── User.java
│   │               ├── dto/                          # 数据传输对象
│   │               │   ├── UserDTO.java
│   │               │   └── UserCreateDTO.java
│   │               └── exception/                    # 异常处理
│   │                   └── GlobalExceptionHandler.java
│   └── resources/
│       ├── application.yml                          # 配置文件
│       ├── mapper/                                  # MyBatis XML映射文件
│       │   └── UserMapper.xml
│       └── schema.sql                               # 数据库初始化脚本
└── test/
    └── java/
        └── com/
            └── example/
                └── demo/
                    └── UserControllerTest.java      # 测试类
```

## 数据库设计

### 用户表 (users)

| 字段名      | 类型         | 说明       | 约束        |
|----------|------------|----------|-----------|
| id       | BIGINT     | 用户ID     | 主键，自增     |
| username | VARCHAR(50)| 用户名      | 非空，唯一     |
| email    | VARCHAR(100)| 邮箱      | 非空，唯一     |
| age      | INT        | 年龄       | 非空        |
| created_at| TIMESTAMP | 创建时间     | 默认当前时间    |
| updated_at| TIMESTAMP | 更新时间     | 默认当前时间    |

### 建表 SQL

```sql
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    age INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

## API 接口说明

### 1. 创建用户
- **URL**: `POST /api/users`
- **请求体**:
```json
{
    "username": "zhangsan",
    "email": "zhangsan@example.com",
    "age": 25
}
```
- **响应**: 
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
- **URL**: `GET /api/users`
- **响应**: 
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

### 3. 根据ID查询用户
- **URL**: `GET /api/users/{id}`
- **响应**: 
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

### 4. 更新用户
- **URL**: `PUT /api/users/{id}`
- **请求体**:
```json
{
    "username": "zhangsan_new",
    "email": "zhangsan_new@example.com",
    "age": 26
}
```
- **响应**: 
```json
{
    "id": 1,
    "username": "zhangsan_new",
    "email": "zhangsan_new@example.com",
    "age": 26,
    "createdAt": "2026-02-08T10:30:00",
    "updatedAt": "2026-02-08T11:30:00"
}
```

### 5. 删除用户
- **URL**: `DELETE /api/users/{id}`
- **响应**: `204 No Content`

## 实现步骤

### 第一步：创建 Spring Boot 项目

1. 访问 [Spring Initializr](https://start.spring.io/)
2. 选择以下配置：
   - Project: Maven
   - Language: Java
   - Spring Boot: 3.2.x
   - Java: 17
3. 添加依赖：
   - Spring Web
   - MyBatis Framework
   - MySQL Driver
   - Lombok
   - Validation

或在 `pom.xml` 中添加 MyBatis 依赖：
```xml
<dependency>
    <groupId>org.mybatis.spring.boot</groupId>
    <artifactId>mybatis-spring-boot-starter</artifactId>
    <version>3.0.3</version>
</dependency>
```

### 第二步：配置数据库连接

在 `application.yml` 中配置：

```yaml
spring:
  application:
    name: spring-boot-crud-demo
  datasource:
    url: jdbc:mysql://localhost:3306/demo_db?useSSL=false&serverTimezone=UTC
    username: root
    password: your_password
    driver-class-name: com.mysql.cj.jdbc.Driver

mybatis:
  mapper-locations: classpath:mapper/*.xml
  type-aliases-package: com.example.demo.entity
  configuration:
    map-underscore-to-camel-case: true
    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl

server:
  port: 8080
```

### 第三步：创建实体类

定义 `User` 实体类，映射数据库表。

### 第四步：创建 Mapper 层

创建 `UserMapper` 接口，使用 `@Mapper` 注解，并创建对应的 XML 映射文件。

### 第五步：创建 Service 层

编写业务逻辑，处理数据转换和验证。

### 第六步：创建 Controller 层

提供 RESTful API 接口，处理 HTTP 请求。

### 第七步：异常处理

使用 `@ControllerAdvice` 统一处理异常。

### 第八步：测试

编写单元测试和集成测试。

## 运行项目

### 前置条件

1. 安装 Java 17+
2. 安装 MySQL 8.0+
3. 安装 Maven 3.6+

### 启动步骤

1. 创建数据库：
```sql
CREATE DATABASE demo_db;
```

2. 修改 `application.yml` 中的数据库连接信息

3. 运行项目：
```bash
mvn spring-boot:run
```

4. 测试接口：
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

## 扩展功能建议

1. **分页查询**: 使用 MyBatis PageHelper 实现数据分页
2. **条件查询**: 使用 MyBatis 动态SQL实现条件查询
3. **数据验证**: 使用 `@Valid` 和 Hibernate Validator
4. **日志记录**: 集成 Logback 或 Log4j2
5. **API 文档**: 集成 Swagger/OpenAPI
6. **单元测试**: 使用 JUnit 5 和 Mockito
7. **缓存**: 集成 Redis 缓存
8. **安全认证**: 集成 Spring Security

## 常见问题

### 1. 数据库连接失败
- 检查 MySQL 是否启动
- 确认数据库名称、用户名、密码是否正确
- 检查防火墙设置

### 2. 端口被占用
- 修改 `application.yml` 中的 `server.port`

### 3. 编码问题
- 确保数据库字符集为 UTF-8
- 在连接 URL 中添加 `characterEncoding=utf8`

## 学习资源

- [Spring Boot 官方文档](https://spring.io/projects/spring-boot)
- [MyBatis 官方文档](https://mybatis.org/mybatis-3/zh/index.html)
- [MyBatis-Spring-Boot-Starter](https://mybatis.org/spring-boot-starter/mybatis-spring-boot-autoconfigure/)
- [MySQL 官方文档](https://dev.mysql.com/doc/)

## 许可证

MIT License
