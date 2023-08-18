package com.aibank.framework.sentinellimit.dao.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.db.Db;
import cn.hutool.db.Entity;
import com.aibank.framework.sentinellimit.dao.SystemRuleMapper;
import com.aibank.framework.sentinellimit.dao.entity.FlowRuleEntity;
import com.aibank.framework.sentinellimit.dao.entity.SystemRuleEntity;
import com.aibank.framework.sentinellimit.domain.LimitConstants;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class SystemRuleMapperImpl implements SystemRuleMapper {

    private DataSource dataSource;

    public SystemRuleMapperImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List<SystemRuleEntity> getAllRule() {
        String app = LimitConstants.app;
        String sql = "select id, app, highest_system_load, highest_cpu_usage, qps, avg_rt, max_thread,system_overload_flag, open, create_time, update_time, created_by, updated_by from system_rule where open = 1 and app='" + app + "'";
        try {
            return Db.use(dataSource).query(sql, SystemRuleEntity.class);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
