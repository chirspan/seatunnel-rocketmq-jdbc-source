package com.apache.seatunnel.plugin.source.rocketmq.jdbc;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.seatunnel.api.table.type.SeaTunnelRow;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RocketMQJDBCSourceTest {

    @Mock
    private DefaultMQPushConsumer consumer;

    @Mock
    private JdbcTemplate jdbcTemplate;

    private RocketMQJDBCSource source;
    private Properties props;

    @BeforeEach
    void setUp() {
        source = new RocketMQJDBCSource();
        props = new Properties();
        props.setProperty("rocketmq.namesrv", "localhost:9876");
        props.setProperty("rocketmq.group", "test-group");
        props.setProperty("rocketmq.topic", "test-topic");
        props.setProperty("jdbc.driver", "com.mysql.cj.jdbc.Driver");
        props.setProperty("jdbc.url", "jdbc:mysql://localhost:3306/test");
        props.setProperty("jdbc.username", "root");
        props.setProperty("jdbc.password", "password");
        props.setProperty("jdbc.query", "SELECT * FROM test WHERE id = ?");
    }

    @Test
    void testGetPluginName() {
        assertEquals("RocketMQ-JDBC-Source", source.getPluginName());
    }

    @Test
    void testPrepare() {
        // 这里需要mock RocketMQ和JDBC的初始化
        // 由于这些是外部依赖，我们需要使用PowerMockito或其他工具来mock静态方法
        // 这里只是一个示例，实际实现可能需要更复杂的mock设置
        assertDoesNotThrow(() -> source.prepare(props));
    }

    @Test
    void testProcessMessage() {
        // 准备测试数据
        String message = "1";
        List<SeaTunnelRow> expectedRows = new ArrayList<>();
        expectedRows.add(new SeaTunnelRow());

        // 设置mock行为
        when(jdbcTemplate.query(eq("SELECT * FROM test WHERE id = ?"), any(RowMapper.class)))
            .thenReturn(expectedRows);

        // 执行测试
        source.processMessage(message);

        // 验证调用
        verify(jdbcTemplate, times(1)).query(eq("SELECT * FROM test WHERE id = ?"), any(RowMapper.class));
    }

    @Test
    void testClose() {
        source.close();
        // 验证consumer是否被正确关闭
        // 由于consumer是private的，我们需要使用反射或其他方式来验证
    }
} 