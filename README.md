# 论文查重系统

基于多种相似度算法的智能文本查重工具，支持独立前端页面和命令行两种使用方式。

## 功能特性

- **多种算法支持**: 实现余弦相似度、编辑距离、Jaccard相似度等多种算法
- **独立前端页面**: 提供独立的HTML页面，支持跨域调用后端API
- **命令行工具**: 支持命令行参数进行批量处理
- **性能优化**: 使用缓存和优化算法提高计算效率
- **完整测试**: 包含单元测试、集成测试和性能测试
- **异常处理**: 完善的异常处理机制
- **CORS支持**: 配置跨域支持，解决前端调用后端API的问题

## 技术栈

- **后端**: Java 8, Spring Boot 2.7.0
- **前端**: HTML5, CSS3, JavaScript, Bootstrap 5
- **构建工具**: Maven 3.6+
- **测试框架**: JUnit 5, Mockito
- **代码覆盖率**: JaCoCo

## 项目结构

```
软工个人项目/
├── main.jar                      # 可执行jar文件（符合作业要求）
├── frontend.html                 # 独立前端页面（主要使用）
├── pom.xml                       # Maven配置文件
├── PSP.md                        # PSP表格
├── README.md                     # 项目说明
├── 使用说明.md                   # 使用说明
├── src/                          # 源代码
│   ├── main/java/com/plagiarism/
│   │   ├── algorithm/            # 相似度算法
│   │   │   ├── SimilarityAlgorithm.java
│   │   │   └── impl/
│   │   │       ├── CosineSimilarity.java
│   │   │       ├── LevenshteinSimilarity.java
│   │   │       ├── JaccardSimilarity.java
│   │   │       └── OptimizedCosineSimilarity.java
│   │   ├── service/              # 业务服务
│   │   │   └── PlagiarismDetectionService.java
│   │   ├── controller/           # Web控制器
│   │   │   ├── PlagiarismController.java
│   │   │   └── PerformanceController.java
│   │   ├── config/               # 配置类
│   │   │   └── CorsConfig.java
│   │   ├── exception/            # 异常处理
│   │   │   └── GlobalExceptionHandler.java
│   │   ├── util/                 # 工具类
│   │   │   └── PerformanceMonitor.java
│   │   ├── SimpleCommandLineMain.java  # 命令行入口（独立版本）
│   │   ├── CommandLineMain.java        # 命令行入口（Spring Boot版本）
│   │   ├── Main.java                   # 原始命令行入口
│   │   └── PlagiarismDetectorApplication.java
│   └── resources/
│       └── application.properties
├── test/                         # 测试代码
│   └── java/com/plagiarism/
│       ├── algorithm/impl/       # 算法测试
│       ├── service/             # 服务测试
│       ├── controller/          # 控制器测试
│       └── integration/         # 集成测试
├── target/                      # 编译输出目录
└── 测试文本/                    # 测试数据
    ├── orig.txt
    ├── orig_0.8_add.txt
    ├── orig_0.8_del.txt
    ├── orig_0.8_dis_1.txt
    ├── orig_0.8_dis_10.txt
    └── orig_0.8_dis_15.txt
```

## 快速开始

### 环境要求

- Java 8 或更高版本
- Maven 3.6 或更高版本

### 构建项目

```bash
# 构建项目
mvn clean package

# 创建可执行jar文件
jar -cfm main.jar MANIFEST.MF -C target/classes .
```

### 运行方式

#### 1. 命令行版本（符合作业要求）

```bash
# 基本用法
java -jar main.jar <原文文件> <抄袭文件> <输出文件>

# 示例
java -jar main.jar 测试文本/orig.txt 测试文本/orig_0.8_add.txt result.txt
```

**命令行版本特点：**
- ✅ 符合作业要求的标准格式
- ✅ 5秒内完成计算
- ✅ 内存使用极低（<2048MB）
- ✅ 无网络连接依赖
- ✅ 只读写指定文件
- ✅ 无异常退出风险

#### 2. 独立前端页面（推荐）

```bash
# 启动后端服务
mvn spring-boot:run

# 在浏览器中打开 frontend.html 文件
# 或者通过HTTP服务器访问 http://localhost:8080
```

**使用步骤：**
1. 启动后端服务：`mvn spring-boot:run`
2. 打开 `frontend.html` 文件
3. 在"服务器配置"区域确认服务器地址为 `http://localhost:8080`
4. 点击"测试连接"验证连接
5. 选择文本输入或文件上传模式进行查重

#### 3. Spring Boot版本命令行

```bash
# 使用Spring Boot版本（较慢，不推荐）
java -jar target/plagiarism-detector-1.0.0.jar <原文文件> <抄袭文件> <输出文件>
```

### 测试

```bash
# 运行所有测试
mvn test

# 生成测试覆盖率报告
mvn jacoco:report

# 查看覆盖率报告
# 打开 target/site/jacoco/index.html
```

## 算法说明

### 1. 余弦相似度 (Cosine Similarity)
- 将文本转换为词频向量
- 计算两个向量的余弦值
- 适合处理词汇级别的相似度

### 2. 编辑距离 (Levenshtein Distance)
- 计算两个字符串之间的最小编辑距离
- 适合处理字符级别的相似度
- 对字符顺序敏感

### 3. Jaccard相似度 (Jaccard Similarity)
- 基于字符集合的交集和并集
- 计算两个集合的Jaccard系数
- 适合处理字符级别的相似度

### 4. 优化版余弦相似度 (Optimized Cosine Similarity)
- 使用缓存机制提高性能
- 优化内存使用
- 适合大规模文本处理

## API接口

### 文本相似度计算
```http
POST /api/similarity
Content-Type: application/json

{
    "originalText": "原文内容",
    "plagiarizedText": "待检测文本"
}
```

### 指定算法计算
```http
POST /api/similarity/algorithm
Content-Type: application/json

{
    "originalText": "原文内容",
    "plagiarizedText": "待检测文本",
    "algorithmName": "Cosine Similarity"
}
```

### 文件上传计算
```http
POST /api/similarity/upload
Content-Type: multipart/form-data

originalFile: <文件>
plagiarizedFile: <文件>
```

### 获取算法列表
```http
GET /api/algorithms
```

### 性能统计
```http
GET /api/performance/stats
```

## 性能优化

1. **缓存机制**: 使用ConcurrentHashMap缓存预处理结果
2. **算法优化**: 优化字符串处理和数学计算
3. **内存管理**: 及时释放不需要的对象
4. **并发处理**: 使用线程安全的数据结构

## 测试用例

项目包含以下测试用例：

1. **单元测试**: 测试各个算法和服务的功能
2. **集成测试**: 测试整个系统的功能
3. **性能测试**: 测试算法执行时间和内存使用
4. **边界测试**: 测试各种边界情况

## 异常处理

系统包含完善的异常处理机制：

1. **文件操作异常**: 文件不存在、权限不足等
2. **参数异常**: 非法参数、空值等
3. **算法异常**: 计算失败、内存不足等
4. **网络异常**: 请求超时、连接失败等

## 部署说明

### 开发环境
```bash
# 启动后端服务
mvn spring-boot:run

# 打开前端页面
# 直接双击 frontend.html 文件
```

### 生产环境
```bash
# 构建JAR包
mvn clean package

# 运行JAR包
java -jar target/plagiarism-detector-1.0.0.jar

# 打开前端页面
# 直接双击 frontend.html 文件
```

### 作业提交
```bash
java -jar main.jar <原文文件> <抄袭文件> <输出文件>
```

### 注意事项
- 确保后端服务运行在 `http://localhost:8080`
- 前端页面支持跨域调用后端API
- 如果端口被占用，可以修改 `application.properties` 中的端口配置
- 命令行版本不依赖Spring Boot，启动速度快

## 联系方式

- 项目链接: [https://github.com/xingchen-boot/3123004795](https://github.com/xingchen-boot/3123004795)
- 问题反馈: [https://github.com/xingchen-boot/3123004795/issues](https://github.com/xingchen-boot/3123004795/issues)

## 更新日志

### v1.0.0 (2024-01-01)
- 初始版本发布
- 实现三种相似度算法
- 提供独立前端页面和命令行工具
- 配置CORS跨域支持
- 完整的测试覆盖
- 性能优化和监控

### v1.1.0 (2024-01-01)
- 添加独立命令行版本
- 优化性能，5秒内完成计算
- 降低内存使用，符合<2048MB要求
- 修复CORS配置问题
- 完善测试用例

## 使用说明

详细的使用说明请参考 [使用说明.md](使用说明.md) 文件。