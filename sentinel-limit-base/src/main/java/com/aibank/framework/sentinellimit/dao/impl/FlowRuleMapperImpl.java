package com.aibank.framework.sentinellimit.dao.impl;

import cn.hutool.db.Db;
import com.aibank.framework.sentinellimit.dao.FlowRuleMapper;
import com.aibank.framework.sentinellimit.dao.entity.FlowRuleEntity;
import com.aibank.framework.sentinellimit.domain.LimitConstants;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;

public class FlowRuleMapperImpl implements FlowRuleMapper {

    private DataSource dataSource;

    public FlowRuleMapperImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List<FlowRuleEntity> getAllFlowRule() {
        String app = LimitConstants.app;
        String sql = "SELECT id, app, resource, control_behavior,max_queueing_time_ms, count, grade, limit_app, strategy, effect_on_over_load, open, create_time," +
                "update_time, created_by, updated_by FROM flow_rule where open = 1 and effect_on_over_load = 0 and app='" + app + "' ";
        try {
            return Db.use(dataSource).query(sql, FlowRuleEntity.class);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<FlowRuleEntity> getAllOverloadFlowRule() {
        String app = LimitConstants.app;
        String sql = "SELECT id, app, resource, control_behavior,max_queueing_time_ms, count, grade, limit_app, strategy, effect_on_over_load, open, create_time," +
                "update_time, created_by, updated_by FROM flow_rule where open = 1 and effect_on_over_load = 1 and app='" + app + "' ";
        try {
            return Db.use(dataSource).query(sql, FlowRuleEntity.class);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
