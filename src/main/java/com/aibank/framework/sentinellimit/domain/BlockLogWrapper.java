package com.aibank.framework.sentinellimit.domain;

import com.alibaba.csp.sentinel.node.DefaultNode;
import com.alibaba.csp.sentinel.slotchain.ResourceWrapper;
import com.alibaba.csp.sentinel.slots.block.BlockException;

public class BlockLogWrapper {

    private BlockException blockException;
    private DefaultNode node;
    private ResourceWrapper resourceWrapper;
    private int count;

    /**
     * 请求 id
     */
    private String transId;

    public BlockLogWrapper() {
    }

    public DefaultNode getNode() {
        return node;
    }

    public ResourceWrapper getResourceWrapper() {
        return resourceWrapper;
    }

    public int getCount() {
        return count;
    }

    public BlockException getBlockException() {
        return blockException;
    }

    public void setBlockException(BlockException blockException) {
        this.blockException = blockException;
    }

    public void setNode(DefaultNode node) {
        this.node = node;
    }

    public void setResourceWrapper(ResourceWrapper resourceWrapper) {
        this.resourceWrapper = resourceWrapper;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getTransId() {
        return transId;
    }

    public void setTransId(String transId) {
        this.transId = transId;
    }

    public BlockLogWrapper(BlockException blockException, DefaultNode node, ResourceWrapper resourceWrapper, int count) {
        this.blockException = blockException;
        this.node = node;
        this.resourceWrapper = resourceWrapper;
        this.count = count;
    }
}
