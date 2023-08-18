-- sentinel.block_info definition

CREATE TABLE `block_info` (
                              `id` bigint NOT NULL AUTO_INCREMENT,
                              `timestamp` bigint DEFAULT NULL COMMENT '时间戳',
                              `time` datetime DEFAULT NULL COMMENT '时间',
                              `trans_id` varchar(255) DEFAULT NULL COMMENT '请求 id',
                              `app` varchar(255) DEFAULT NULL COMMENT '系统编码',
                              `resource` varchar(255) DEFAULT NULL COMMENT '资源名称',
                              `entry_type` varchar(255) DEFAULT NULL COMMENT '入口类型',
                              `limit_type` varchar(255) DEFAULT NULL COMMENT '限流类型',
                              `system_limit_type` varchar(255) DEFAULT NULL COMMENT '系统限流类型',
                              `overload_config_value` double DEFAULT NULL COMMENT '系统负荷配置值',
                              `overload_value` double DEFAULT NULL COMMENT '系统负荷当前值',
                              `limit_config_value` double DEFAULT NULL COMMENT '限流配置值',
                              `limit_value` double DEFAULT NULL COMMENT '限流当前值',
                              `total_qps` double DEFAULT NULL COMMENT '每秒请求数，包括（blockQps与passQps）',
                              `pass_qps` double DEFAULT NULL COMMENT '每秒通过的请求数量',
                              `block_qps` double DEFAULT NULL COMMENT '每秒被拦截的请求数量',
                              `total_request` bigint DEFAULT NULL COMMENT '最近1分钟内的请求数',
                              `total_pass` bigint DEFAULT NULL COMMENT '最近1分钟内通过的请求数量',
                              `total_block` bigint DEFAULT NULL COMMENT '最近1分钟内被拦截的请求数量',
                              `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                              `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                              `created_by` varchar(255) DEFAULT NULL COMMENT '创建人',
                              `updated_by` varchar(255) DEFAULT NULL COMMENT '更新人',
                              PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='限流信息表';

-- sentinel.flow_rule definition

CREATE TABLE `flow_rule` (
                             `id` bigint NOT NULL AUTO_INCREMENT,
                             `app` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
                             `resource` varchar(255) DEFAULT NULL,
                             `control_behavior` int DEFAULT NULL,
                             `count` double DEFAULT NULL,
                             `grade` int DEFAULT NULL,
                             `limit_app` varchar(255) DEFAULT NULL,
                             `strategy` int DEFAULT NULL,
                             `effect_on_over_load` tinyint(1) DEFAULT NULL,
                             `open` tinyint(1) DEFAULT NULL,
                             `create_time` datetime DEFAULT NULL,
                             `update_time` datetime DEFAULT NULL,
                             `created_by` varchar(255) DEFAULT NULL,
                             `updated_by` varchar(255) DEFAULT NULL,
                             PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;



-- sentinel.system_rule definition

CREATE TABLE `system_rule` (
                               `id` bigint NOT NULL AUTO_INCREMENT,
                               `app` varchar(255) DEFAULT NULL,
                               `highest_system_load` double DEFAULT NULL,
                               `highest_cpu_usage` double DEFAULT NULL,
                               `qps` double DEFAULT NULL,
                               `avg_rt` bigint DEFAULT NULL,
                               `max_thread` bigint DEFAULT NULL,
                               `system_overload_flag` tinyint(1) DEFAULT NULL,
                               `open` tinyint(1) DEFAULT NULL,
                               `create_time` datetime DEFAULT NULL,
                               `update_time` datetime DEFAULT NULL,
                               `created_by` varchar(255) DEFAULT NULL,
                               `updated_by` varchar(255) DEFAULT NULL,
                               PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;