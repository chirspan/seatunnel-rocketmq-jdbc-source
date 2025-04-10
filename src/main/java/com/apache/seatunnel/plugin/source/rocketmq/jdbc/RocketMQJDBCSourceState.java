package com.apache.seatunnel.plugin.source.rocketmq.jdbc;

import org.apache.seatunnel.api.source.SourceSplit;

import java.io.Serializable;
import java.util.List;

public class RocketMQJDBCSourceState implements Serializable {
    private List<SourceSplit> splits;
    private long offset;

    public RocketMQJDBCSourceState(List<SourceSplit> splits, long offset) {
        this.splits = splits;
        this.offset = offset;
    }

    public List<SourceSplit> getSplits() {
        return splits;
    }

    public void setSplits(List<SourceSplit> splits) {
        this.splits = splits;
    }

    public long getOffset() {
        return offset;
    }

    public void setOffset(long offset) {
        this.offset = offset;
    }
} 