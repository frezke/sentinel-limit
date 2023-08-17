package com.aibank.framework.sentinellimit.service;

import com.aibank.framework.sentinellimit.dao.entity.BlockInfoEntity;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class DefaultBlockRequestInfoRecord implements BlockRequestInfoRecord {
    private DataSource dataSource;

    private String app;

    public DefaultBlockRequestInfoRecord(DataSource dataSource, String app) {
        this.dataSource = dataSource;
        this.app = app;
    }

    public void write(DataSource dataSource, String sql) throws Exception {
        dataSource.getConnection();
        Connection connection = dataSource.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.execute();
        connection.close();
    }

    @Override
    public void blockInfoRecord(BlockInfoEntity blockInfoEntity) throws Exception {
        String sql = "insert into block_info (id,app,limitType,limitConfigValue,limitValue,systemLimitType," +
                "overloadConfigValue,sentinelCause,resource,totalRequest,totalPass,totalSuccess,totalQps,passQps,blockQps,qps,thread,rt,load,cpu) values(" +
                 + blockInfoEntity.getId() + "," + app + "," + "\"" + blockInfoEntity.getLimitType() + "\"" + "," + blockInfoEntity.getLimitConfigValue() +
                "," + blockInfoEntity.getLimitValue() + "," + blockInfoEntity.getSystemLimitType() + "," + blockInfoEntity.getOverloadConfigValue() + "," +
                "\"" + "," + "\"" + blockInfoEntity.getSentinelCause() + "\"" + "," + "\"" + blockInfoEntity.getResource() + "\"" + "," + blockInfoEntity.getTotalRequest() + "," +
                blockInfoEntity.getTotalPass() + "," + blockInfoEntity.getTotalSuccess() + "," + blockInfoEntity.getTotalQps() + "," +
                blockInfoEntity.getPassQps() + "," + blockInfoEntity.getBlockQps() + "," + blockInfoEntity.getQps() + "," + blockInfoEntity.getThread() + "," +
                blockInfoEntity.getRt() + "," + blockInfoEntity.getLoad() + "," + blockInfoEntity.getCpu() + ")";
        System.out.println(sql);
        write(dataSource,sql);
    }
}
