package com.apache.seatunnel.plugin.source.rocketmq.jdbc;

import org.apache.seatunnel.api.source.SeaTunnelSource;
import org.apache.seatunnel.api.source.SourceSplit;
import org.apache.seatunnel.api.table.type.SeaTunnelRow;
import org.apache.seatunnel.api.table.type.SeaTunnelRowType;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.util.List;
import java.util.Properties;

public class RocketMQJDBCSource implements SeaTunnelSource<SeaTunnelRow, SourceSplit, RocketMQJDBCSourceState> {
    private DefaultMQPushConsumer consumer;
    private JdbcTemplate jdbcTemplate;
    private Properties config;
    private SeaTunnelRowType rowType;

    @Override
    public String getPluginName() {
        return "RocketMQ-JDBC-Source";
    }

    @Override
    public void prepare(Properties props) {
        this.config = props;
        initializeRocketMQConsumer();
        initializeJdbcTemplate();
    }

    private void initializeRocketMQConsumer() {
        try {
            consumer = new DefaultMQPushConsumer(config.getProperty("rocketmq.group"));
            consumer.setNamesrvAddr(config.getProperty("rocketmq.namesrv"));
            consumer.subscribe(config.getProperty("rocketmq.topic"), "*");
            
            consumer.registerMessageListener(new MessageListenerConcurrently() {
                @Override
                public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
                    for (MessageExt msg : msgs) {
                        String message = new String(msg.getBody());
                        processMessage(message);
                    }
                    return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                }
            });
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize RocketMQ consumer", e);
        }
    }

    private void initializeJdbcTemplate() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(config.getProperty("jdbc.driver"));
        dataSource.setUrl(config.getProperty("jdbc.url"));
        dataSource.setUsername(config.getProperty("jdbc.username"));
        dataSource.setPassword(config.getProperty("jdbc.password"));
        
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    private void processMessage(String message) {
        // 使用消息内容作为参数执行JDBC查询
        String sql = config.getProperty("jdbc.query");
        List<SeaTunnelRow> results = jdbcTemplate.query(sql, 
            (rs, rowNum) -> {
                // 将查询结果转换为SeaTunnelRow
                // 这里需要根据实际的列类型进行转换
                return new SeaTunnelRow();
            });
        
        // 处理查询结果
        for (SeaTunnelRow row : results) {
            // 将结果传递给SeaTunnel的数据流
        }
    }

    @Override
    public void close() {
        if (consumer != null) {
            consumer.shutdown();
        }
    }
} 