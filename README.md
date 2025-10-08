# 漫画阅读平台 (Comic Platform)

这是一个完整的漫画阅读平台，包含前端Vue 3应用和后端Spring Boot服务。

## 项目架构

项目采用前后端分离架构：
- **前端**：Vue 3 + Element Plus + Vite + Pinia + Vue Router
- **后端**：Spring Boot 3.5.6 + Java 17 + Maven

## 功能特性

### 前端功能
- 用户注册与登录
- 漫画列表浏览
- 漫画阅读功能
- 用户个人中心
- 响应式设计，适配不同设备
- 现代化UI界面

### 后端功能
- 用户认证与授权（JWT）
- 漫画数据管理
- 用户信息管理
- RESTful API设计

## 技术栈

### 前端
- **框架**：Vue 3.5.22
- **UI组件库**：Element Plus 2.11.4
- **构建工具**：Vite 7.1.7
- **状态管理**：Pinia 3.0.3
- **路由**：Vue Router 4.5.1
- **HTTP客户端**：Axios 1.12.2
- **图标**：Element Plus Icons、Font Awesome 4.7.0
- **预处理器**：Sass 1.93.2

### 后端
- **框架**：Spring Boot 3.5.6
- **编程语言**：Java 17
- **构建工具**：Maven
- **认证**：Spring Security + JWT
- **日志**：SLF4J 2.0.7 + Logback 1.5.13

## 快速开始

### 前置要求
- 前端：Node.js 16+、npm 7+
- 后端：JDK 17+、Maven 3.8+

### 前端启动

```bash
# 进入前端目录
cd comic-frontend

# 安装依赖
npm install

# 启动开发服务器
npm run dev

# 构建生产版本
npm run build

# 预览生产版本
npm run preview
```

### 后端启动

```bash
# 在项目根目录

# 构建项目
mvn clean package

# 运行Spring Boot应用
java -jar target/comic-backend-0.0.1-SNAPSHOT.jar

# 或者使用Maven直接运行
mvn spring-boot:run
```

## 项目结构

### 前端结构
```
comic-frontend/
├── src/
│   ├── api/            # API接口定义
│   ├── assets/         # 静态资源
│   ├── components/     # 通用组件
│   ├── router/         # 路由配置
│   ├── stores/         # Pinia状态管理
│   ├── views/          # 页面组件
│   ├── App.vue         # 根组件
│   ├── main.js         # 入口文件
│   └── style.css       # 全局样式
├── public/             # 静态资源（不经过打包的）
├── index.html          # HTML入口
├── package.json        # 项目配置和依赖
└── vite.config.js      # Vite配置
```

### 后端结构
```
comic-backend/
├── src/main/
│   ├── java/           # Java源代码
│   └── resources/      # 资源文件（配置文件等）
├── pom.xml             # Maven配置文件
└── upload/             # 上传文件存储目录
```

## 主要页面

1. **登录页** (/login) - 用户登录界面
2. **注册页** (/register) - 新用户注册界面
3. **首页** (/home) - 平台主页
4. **漫画列表页** (/comic-list) - 显示所有漫画
5. **漫画阅读页** (/comic-read/:id) - 在线阅读漫画
6. **用户中心** (/user-center) - 已登录用户的个人信息管理

## 用户认证流程

1. 用户在前端输入用户名和密码
2. 前端通过Axios发送请求到后端
3. 后端验证用户信息并生成JWT Token
4. 前端存储Token到localStorage并设置到Pinia状态
5. 后续请求携带Token进行身份验证

## 开发说明

### 前端开发
- 组件化设计，可重用组件放置在components目录
- API接口统一管理在api目录
- 用户状态使用Pinia全局管理
- 路由配置在router目录，支持路由守卫

### 后端开发
- RESTful API设计
- 使用Spring Security进行认证授权
- JWT用于无状态认证
- 数据库配置请修改相应的application.properties文件

## 注意事项
1. 确保Node.js和JDK版本符合要求
2. 前后端需要分别启动
3. 前端API请求地址需根据后端实际地址进行配置
4. 开发环境和生产环境配置需分别设置

## 许可证

MIT License