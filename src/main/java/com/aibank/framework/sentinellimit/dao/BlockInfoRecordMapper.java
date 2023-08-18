package com.aibank.framework.sentinellimit.dao;

import com.aibank.framework.sentinellimit.dao.entity.BlockInfoEntity;

import java.util.List;

public interface BlockInfoRecordMapper {
    void batchInsert(List<BlockInfoEntity> blockInfoEntityList);
}
