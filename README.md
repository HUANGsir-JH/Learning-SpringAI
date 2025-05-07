# Learning-SpringAI

## 项目介绍
Learning-SpringAI 是一个学习和实践 Spring 与人工智能（AI）技术相结合的项目。旨在帮助开发者快速了解如何使用SpringAI提供的api在Spring 框架中构建 AI 驱动的 Web 应用程序的相关知识与技能。
重点在于应用，而不是原理。

## 项目结构
本项目主要由以下两部分组成：
- **HTML**: 用于构建前端页面，提供基本的用户交互界面。
- **Java**: 后端核心逻辑的实现，基于 Spring 框架。

## 项目内容
- `chat`：利用SpringAI提供的api，基于deepseek进行web接口的编写，并且使用websocket实现简单的多轮对话
- `image`：基于智谱AI的文生图模型，编写可以用于调用AI生成图像的接口
- `voice`：基于阿里云的sdk进行语音的识别和翻译（由于SpringAI仅支持ChatGPT，但受限于付费方式，因而以此作为替代）
- `ollama`：展示如何编写可以与本地模型交互的web接口（需要在本地/服务器运行ollama）

## 项目运行
### 环境要求
- JDK 17 或更高版本
- 各种依赖，详见pom.xml文件
- Maven 构建工具
- 一个现代的 Web 浏览器

### 快速启动
1. 克隆项目代码：
   ```bash
   git clone https://github.com/HUANGsir-JH/Learning-SpringAI.git
   ```
2. 填写application.yml中的api-key
3. 进入项目目录：
   ```bash
   cd Learning-SpringAI
   ```
4. 使用 Maven 构建并运行项目：
   ```bash
   mvn spring-boot:run
   ```
   或者使用 Gradle：
   ```bash
   gradle bootRun
   ```
5. 在浏览器中访问 [http://localhost:8080](http://localhost:8080)。或者在resources目录下打开html文件进行使用

## 学习资源
以下是一些可能对您有帮助的学习资源：
- [Spring 官方文档](https://spring.io/projects/spring-boot)
- [Spring 与 AI 的融合案例](https://spring.io/guides)
- [阿里云sdk文档](https://bailian.console.aliyun.com/?apiKey=1&accounttraceid=3ef7718bbb9640cab7e2c6765f2fabf6ptxb&tab=doc#/doc/?type=model&url=https%3A%2F%2Fhelp.aliyun.com%2Fdocument_detail%2F2842554.html&renderType=iframe)
