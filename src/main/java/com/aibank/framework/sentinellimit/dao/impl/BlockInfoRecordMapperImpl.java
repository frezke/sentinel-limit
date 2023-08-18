package com.aibank.framework.sentinellimit.dao.impl;

import cn.hutool.db.Db;
import cn.hutool.db.Entity;
import com.aibank.framework.sentinellimit.dao.BlockInfoRecordMapper;
import com.aibank.framework.sentinellimit.dao.entity.BlockInfoEntity;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class BlockInfoRecordMapperImpl implements BlockInfoRecordMapper {

    private DataSource dataSource;

    public BlockInfoRecordMapperImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void batchInsert(List<BlockInfoEntity> blockInfoEntityList) {
        if (blockInfoEntityList.isEmpty()) {
            return;
        }
        List<Entity> blockInfo = blockInfoEntityList.stream().map(blockInfoEntity -> Entity.create("block_info").parseBean(blockInfoEntity, true, true)).collect(Collectors.toList());
        try {
            Db.use(dataSource).insert(blockInfo);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
