package com.aibank.framework.sentinellimit.task;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import com.aibank.framework.sentinellimit.dao.entity.BlockInfoEntity;
import com.aibank.framework.sentinellimit.domain.LimitConstants;
import com.aibank.framework.sentinellimit.domain.LimitData;
import com.aibank.framework.sentinellimit.dao.BlockInfoRecordMapper;
import com.aibank.framework.sentinellimit.slot.BlockLogSlot;
import com.alibaba.csp.sentinel.log.RecordLog;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class RecordsBlockTask implements Runnable {

    private BlockInfoRecordMapper blockInfoRecordMapper;

    public RecordsBlockTask(BlockInfoRecordMapper blockInfoRecordMapper) {
        this.blockInfoRecordMapper = blockInfoRecordMapper;
    }

    @Override
    public void run() {
        try {
            innerRun();
        } catch (Throwable t) {
            RecordLog.warn("[RecordsBlockTask] Write Block record error", t);
        }
    }

    private void innerRun() {
        BlockingQueue<LimitData> blockLogQueue = BlockLogSlot.BLOCK_LOG_QUEUE;
        List<BlockInfoEntity> blockInfoEntityList = new ArrayList<>();
        while (true) {
            LimitData limitData;
            try {
                limitData = blockLogQueue.poll(3000, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                return;
            }
            if ((limitData == null && !blockInfoEntityList.isEmpty()) || blockInfoEntityList.size() >= 100) {
                // 暂不聚合,只做批量插入
                blockInfoRecordMapper.batchInsert(blockInfoEntityList);
                blockInfoEntityList.clear();
                continue;
            }
            if (limitData == null) {
                return;
            }
            blockInfoEntityList.add(getBlockInfoEntity(limitData));
        }
    }

    public BlockInfoEntity getBlockInfoEntity(LimitData limitData) {
        BlockInfoEntity blockInfoEntity = new BlockInfoEntity();
        BeanUtil.copyProperties(limitData, blockInfoEntity);
        blockInfoEntity.setApp(LimitConstants.app);
        blockInfoEntity.setTime(new Date(blockInfoEntity.getTimestamp()));
        blockInfoEntity.setEntryType(limitData.getEntryType() == null ? null : limitData.getEntryType().name());
        blockInfoEntity.setLimitType(limitData.getLimitType() == null ? null : limitData.getLimitType().name());
        blockInfoEntity.setSystemLimitType(limitData.getSystemLimitType() == null ? null : limitData.getSystemLimitType().name());
        return blockInfoEntity;
    }
}