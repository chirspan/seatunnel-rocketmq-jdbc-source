package com.apache.seatunnel.plugin.source.rocketmq.jdbc;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.seatunnel.api.table.type.SeaTunnelRow;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.util.List;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

class RocketMQJDBCSourceIntegrationTest {

    private RocketMQJDBCSource source;
    private Properties props;
    private DataSource dataSource;
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        // 初始化H2数据库
        dataSource = new DriverManagerDataSource();
        ((DriverManagerDataSource) dataSource).setDriverClassName("org.h2.Driver");
        ((DriverManagerDataSource) dataSource).setUrl("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1");
        ((DriverManagerDataSource) dataSource).setUsername("sa");
        ((DriverManagerDataSource) dataSource).setPassword("");

        jdbcTemplate = new JdbcTemplate(dataSource);

        // 创建测试表
        jdbcTemplate.execute("CREATE TABLE test (id INT PRIMARY KEY, name VARCHAR(255))");
        jdbcTemplate.execute("INSERT INTO test (id, name) VALUES (1, 'test1')");
        jdbcTemplate.execute("INSERT INTO test (id, name) VALUES (2, 'test2')");

        // 初始化插件配置
        source = new RocketMQJDBCSource();
        props = new Properties();
        props.setProperty("rocketmq.namesrv", "localhost:9876");
        props.setProperty("rocketmq.group", "test-group");
        props.setProperty("rocketmq.topic", "test-topic");
        props.setProperty("jdbc.driver", "org.h2.Driver");
        props.setProperty("jdbc.url", "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1");
        props.setProperty("jdbc.username", "sa");
        props.setProperty("jdbc.password", "");
        props.setProperty("jdbc.query", "SELECT * FROM test WHERE id = ?");
    }

    @Test
    void testProcessMessageWithH2Database() {
        // 准备测试数据
        String message = "1";

        // 执行测试
        source.processMessage(message);

        // 验证结果
        List<SeaTunnelRow> results = jdbcTemplate.query(
            "SELECT * FROM test WHERE id = ?",
            (rs, rowNum) -> {
                SeaTunnelRow row = new SeaTunnelRow();
                row.setField(0, rs.getInt("id"));
                row.setField(1, rs.getString("name"));
                return row;
            },
            1
        );

        assertFalse(results.isEmpty());
        assertEquals(1, results.get(0).getField(0));
        assertEquals("test1", results.get(0).getField(1));
    }

    @Test
    void testProcessMessageWithInvalidId() {
        // 准备测试数据
        String message = "999"; // 不存在的ID

        // 执行测试
        source.processMessage(message);

        // 验证结果
        List<SeaTunnelRow> results = jdbcTemplate.query(
            "SELECT * FROM test WHERE id = ?",
            (rs, rowNum) -> {
                SeaTunnelRow row = new SeaTunnelRow();
                row.setField(0, rs.getInt("id"));
                row.setField(1, rs.getString("name"));
                return row;
            },
            999
        );

        assertTrue(results.isEmpty());
    }
} 