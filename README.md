# Seatunnel RocketMQ-JDBC Source Plugin

这是一个Seatunnel插件，用于从RocketMQ中消费消息，并使用消息内容作为参数执行JDBC查询，将查询结果作为Seatunnel的输入数据。

## 功能特点

- 支持从RocketMQ消费消息
- 支持使用消息内容作为参数执行JDBC查询
- 支持配置批处理大小和超时时间
- 支持自定义SQL查询语句

## 配置参数

### RocketMQ配置
- `rocketmq.namesrv`: RocketMQ的NameServer地址
- `rocketmq.group`: 消费者组名称
- `rocketmq.topic`: 要订阅的主题

### JDBC配置
- `jdbc.driver`: JDBC驱动类名
- `jdbc.url`: 数据库连接URL
- `jdbc.username`: 数据库用户名
- `jdbc.password`: 数据库密码
- `jdbc.query`: SQL查询语句（支持参数占位符）

### 其他配置
- `batch.size`: 批处理大小（默认：1000）
- `poll.timeout`: 轮询超时时间（毫秒，默认：1000）

## 使用示例

```hocon
env {
  execution.parallelism = 1
  job.mode = "BATCH"
}

source {
  RocketMQ-JDBC-Source {
    rocketmq.namesrv = "localhost:9876"
    rocketmq.group = "seatunnel-group"
    rocketmq.topic = "test-topic"
    jdbc.driver = "com.mysql.cj.jdbc.Driver"
    jdbc.url = "jdbc:mysql://localhost:3306/test"
    jdbc.username = "root"
    jdbc.password = "password"
    jdbc.query = "SELECT * FROM users WHERE id = ?"
    batch.size = 1000
    poll.timeout = 1000
  }
}

transform {
  # 在这里添加转换逻辑
}

sink {
  # 在这里添加输出配置
}
```

## 构建和安装

1. 克隆项目
2. 执行Maven构建：
```bash
mvn clean package
```
3. 将生成的jar包复制到Seatunnel的plugins目录

## 注意事项
！当前内容由cursor生成，暂未进行测试完成验证

1. 确保RocketMQ服务器正在运行且可访问
2. 确保数据库服务器正在运行且可访问
3. 正确配置数据库连接信息
4. SQL查询语句中的参数占位符将使用RocketMQ消息内容替换 