package com.aibank.framework.sentinellimit.rule;

import com.alibaba.csp.sentinel.slots.system.SystemRule;

public class GlobalOverloadConfig {

    private static volatile double maxSystemLoad = Double.MAX_VALUE;
    private static volatile double maxCpuUsage = Double.MAX_VALUE;
    private static volatile double maxQps = Double.MAX_VALUE;
    private static volatile long maxRt = Long.MAX_VALUE;
    private static volatile long maxThread = Long.MAX_VALUE;
    private static volatile boolean checkSystemStatus = false;

    public static void loadConfig(SystemRule systemRule) {
        if (systemRule == null) {
            return;
        }
        if (systemRule.getHighestSystemLoad() > 0) {
            GlobalOverloadConfig.maxSystemLoad = systemRule.getHighestSystemLoad();
            GlobalOverloadConfig.checkSystemStatus = true;
        }
        if (systemRule.getHighestCpuUsage() > 0) {
            GlobalOverloadConfig.maxCpuUsage = systemRule.getHighestCpuUsage();
            GlobalOverloadConfig.checkSystemStatus = true;
        }
        if (systemRule.getQps() > 0) {
            GlobalOverloadConfig.maxQps = systemRule.getQps();
            GlobalOverloadConfig.checkSystemStatus = true;
        }
        if (systemRule.getAvgRt() > 0) {
            GlobalOverloadConfig.maxRt = systemRule.getAvgRt();
            GlobalOverloadConfig.checkSystemStatus = true;
        }
        if (systemRule.getMaxThread() > 0) {
            GlobalOverloadConfig.maxThread = systemRule.getMaxThread();
            GlobalOverloadConfig.checkSystemStatus = true;
        }
    }

    public static double getMaxSystemLoad() {
        return maxSystemLoad;
    }

    public static double getMaxCpuUsage() {
        return maxCpuUsage;
    }


    public static double getMaxQps() {
        return maxQps;
    }


    public static long getMaxRt() {
        return maxRt;
    }

    public static long getMaxThread() {
        return maxThread;
    }

    public static boolean isCheckSystemStatus() {
        return checkSystemStatus;
    }
}
