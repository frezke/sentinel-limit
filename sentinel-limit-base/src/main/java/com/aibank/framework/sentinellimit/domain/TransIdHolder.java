package com.aibank.framework.sentinellimit.domain;

public class TransIdHolder {
    private static final ThreadLocal<String> transIdHolder = new ThreadLocal<>();

    public static String getTransId() {
        return transIdHolder.get();
    }

    public static void setTransId(String transId) {
        transIdHolder.set(transId);
    }

    public static void clearTransId() {
        transIdHolder.remove();
    }
}