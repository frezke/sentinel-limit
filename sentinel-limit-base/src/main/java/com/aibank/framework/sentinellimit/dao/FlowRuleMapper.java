package com.aibank.framework.sentinellimit.dao;

import com.aibank.framework.sentinellimit.dao.entity.FlowRuleEntity;

import java.sql.SQLException;
import java.util.List;

public interface FlowRuleMapper {

    /**
     * 获取所有正常限流,非高负载限流规则
     * @return
     */
    List<FlowRuleEntity> getAllFlowRule();


    /**
     * 获取所有高负载限流规则
     * @return
     */

    List<FlowRuleEntity> getAllOverloadFlowRule();
}
