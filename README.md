![logo.png](next-bi-backend%2Fimage%2Flogo.png)

# next BI
## 项目介绍

基于 Spring Boot + MQ + AIGC + React的智能数据分析平台。区别于传统 BI，用户只需要导入原始数据集、并输入分析诉求，就能自动生成可视化图表及分析结论，实现数据分析的降本增效。

## 流程图
![流程图.png](next-bi-backend%2Fimage%2F%E6%B5%81%E7%A8%8B%E5%9B%BE.png)


## 项目亮点

### 1. 智能数据分析流程：

- 后端通过自定义Prompt预设模板，封装用户输入的数据和分析诉求。
- 通过AIGC接口生成可视化图表的JSON配置和分析结论，返回给前端进行渲染。

### 2. Excel文件处理优化：

- 由于AIGC的输入Token限制，采用Easy Excel解析用户上传的XLSX表格数据文件并压缩为CSV格式。
- 实测结果显示，此优化提高了单次输入数据量约20%，同时也实现了成本的有效节约。

### 3. 安全性增强：

- 对用户上传的原始数据文件进行多重校验，包括后缀名、大小、内容等，以确保系统的安全性。
- 利用Easy Excel解析成功的文件基本可以确认其内容的合法性。

### 4. 分布式限流与资源控制：

- 基于Redisson的RateLimiter实现分布式限流，控制单用户访问频率，防止恶意占用系统资源。

### 5. 数据存储与查询性能优化：

- 使用MyBatis + 业务层构建自定义SQL，实现对每份原始数据的分表存储，提高查询性能和系统的可扩展性。
- 数据分表存储提高了查询灵活性，同时带来了性能的显著提升。

### 6. 异步化任务处理：

- 基于自定义的IO密集型线程池和任务队列，实现AIGC的并发执行和异步化。
- 提交任务后即可响应前端，大幅度提高用户体验，支持更多用户排队而不是给系统无限压力导致提交失败。

### 7. 消息队列和可靠性提升：

- 引入RabbitMQ分布式消息队列，用于接收和持久化任务消息。
- 通过Direct交换机转发给解耦的AI生成模块消费，提高系统的可靠性，确保任务的可靠处理。

## 优化和增强

### 1. 数据处理进一步优化：

- 利用AI进一步整理和压缩原始数据，提高输入的数据数量和质量。

### 2. 系统资源控制：

- 限制用户同时生成图表的数量，防止单个用户抢占系统资源。

### 3. 用户统计和积分系统：

- 统计用户生成图表的次数，考虑添加积分系统，用户可以消耗积分进行智能分析。

### 4. 缓存优化：

- 由于图表数据是静态的，考虑使用Redis缓存来提高加载速度。

### 5. 异常处理和重试机制：

- 使用死信队列处理异常情况，将图表生成任务置为失败。
- 为任务的执行增加Guava Retrying重试机制，保证系统的可靠性和稳定性。

### 6. 超时控制和反向压力：

- 为任务的执行增加超时时间，超时自动标记为失败，提高系统稳定性。
- 考虑根据调用的服务状态来选择当前系统的策略，实现反向压力控制。

### 7. 实时消息通知：

- 实现任务执行成功或失败后，通过WebSocket或Server-Sent Events给用户发送实时消息通知，提高用户体验。



## 截图
![login.png](next-bi-backend%2Fimage%2Flogin.png)
![index.png](next-bi-backend%2Fimage%2Findex.png)
![fill_index.png](next-bi-backend%2Fimage%2Ffill_index.png)
![success.png](next-bi-backend%2Fimage%2Fsuccess.png)
![sse.png](next-bi-backend%2Fimage%2Fsse.png)
![mychart.png](next-bi-backend%2Fimage%2Fmychart.png)
![detail.png](next-bi-backend%2Fimage%2Fdetail.png)
![wait.png](next-bi-backend%2Fimage%2Fwait.png)
![running.png](next-bi-backend%2Fimage%2Frunning.png)
![failed.png](next-bi-backend%2Fimage%2Ffailed.png)

## 技术栈


![Static Badge](https://img.shields.io/badge/AIGC-blue)
![Static Badge](https://img.shields.io/badge/Spring%20Boot-green)
![Static Badge](https://img.shields.io/badge/RabbitMQ-orange)
![Static Badge](https://img.shields.io/badge/Redis-red)
![Static Badge](https://img.shields.io/badge/React-skyblue)
![Static Badge](https://img.shields.io/badge/Guava%20Retrying-gray)
![Static Badge](https://img.shields.io/badge/Server%20Sent%20Events-black)
...


## 作者

- [@peiYp](https://github.com/change-everything)


## 反馈

如果你有任何反馈，请联系我：pyptsguas@163.com

