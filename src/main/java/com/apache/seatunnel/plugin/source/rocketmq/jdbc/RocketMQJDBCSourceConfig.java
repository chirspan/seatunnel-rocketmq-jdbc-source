package com.apache.seatunnel.plugin.source.rocketmq.jdbc;

import lombok.Data;

@Data
public class RocketMQJDBCSourceConfig {
    // RocketMQ配置
    private String namesrvAddr;
    private String consumerGroup;
    private String topic;
    
    // JDBC配置
    private String jdbcDriver;
    private String jdbcUrl;
    private String jdbcUsername;
    private String jdbcPassword;
    private String jdbcQuery;
    
    // 其他配置
    private int batchSize = 1000;
    private long pollTimeout = 1000;
    
    public static RocketMQJDBCSourceConfig fromProperties(java.util.Properties props) {
        RocketMQJDBCSourceConfig config = new RocketMQJDBCSourceConfig();
        config.setNamesrvAddr(props.getProperty("rocketmq.namesrv"));
        config.setConsumerGroup(props.getProperty("rocketmq.group"));
        config.setTopic(props.getProperty("rocketmq.topic"));
        config.setJdbcDriver(props.getProperty("jdbc.driver"));
        config.setJdbcUrl(props.getProperty("jdbc.url"));
        config.setJdbcUsername(props.getProperty("jdbc.username"));
        config.setJdbcPassword(props.getProperty("jdbc.password"));
        config.setJdbcQuery(props.getProperty("jdbc.query"));
        
        if (props.containsKey("batch.size")) {
            config.setBatchSize(Integer.parseInt(props.getProperty("batch.size")));
        }
        if (props.containsKey("poll.timeout")) {
            config.setPollTimeout(Long.parseLong(props.getProperty("poll.timeout")));
        }
        
        return config;
    }
} 