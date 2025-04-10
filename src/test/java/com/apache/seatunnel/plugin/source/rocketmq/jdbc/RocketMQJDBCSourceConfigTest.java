package com.apache.seatunnel.plugin.source.rocketmq.jdbc;

import org.junit.jupiter.api.Test;
import java.util.Properties;
import static org.junit.jupiter.api.Assertions.*;

class RocketMQJDBCSourceConfigTest {

    @Test
    void testFromProperties() {
        Properties props = new Properties();
        props.setProperty("rocketmq.namesrv", "localhost:9876");
        props.setProperty("rocketmq.group", "test-group");
        props.setProperty("rocketmq.topic", "test-topic");
        props.setProperty("jdbc.driver", "com.mysql.cj.jdbc.Driver");
        props.setProperty("jdbc.url", "jdbc:mysql://localhost:3306/test");
        props.setProperty("jdbc.username", "root");
        props.setProperty("jdbc.password", "password");
        props.setProperty("jdbc.query", "SELECT * FROM test WHERE id = ?");
        props.setProperty("batch.size", "2000");
        props.setProperty("poll.timeout", "2000");

        RocketMQJDBCSourceConfig config = RocketMQJDBCSourceConfig.fromProperties(props);

        assertEquals("localhost:9876", config.getNamesrvAddr());
        assertEquals("test-group", config.getConsumerGroup());
        assertEquals("test-topic", config.getTopic());
        assertEquals("com.mysql.cj.jdbc.Driver", config.getJdbcDriver());
        assertEquals("jdbc:mysql://localhost:3306/test", config.getJdbcUrl());
        assertEquals("root", config.getJdbcUsername());
        assertEquals("password", config.getJdbcPassword());
        assertEquals("SELECT * FROM test WHERE id = ?", config.getJdbcQuery());
        assertEquals(2000, config.getBatchSize());
        assertEquals(2000L, config.getPollTimeout());
    }

    @Test
    void testDefaultValues() {
        Properties props = new Properties();
        props.setProperty("rocketmq.namesrv", "localhost:9876");
        props.setProperty("rocketmq.group", "test-group");
        props.setProperty("rocketmq.topic", "test-topic");
        props.setProperty("jdbc.driver", "com.mysql.cj.jdbc.Driver");
        props.setProperty("jdbc.url", "jdbc:mysql://localhost:3306/test");
        props.setProperty("jdbc.username", "root");
        props.setProperty("jdbc.password", "password");
        props.setProperty("jdbc.query", "SELECT * FROM test WHERE id = ?");

        RocketMQJDBCSourceConfig config = RocketMQJDBCSourceConfig.fromProperties(props);

        assertEquals(1000, config.getBatchSize());
        assertEquals(1000L, config.getPollTimeout());
    }
} 