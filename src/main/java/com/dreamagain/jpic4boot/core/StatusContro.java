package com.dreamagain.jpic4boot.core;

/**
 * 开启状态控制类
 *
 * @author jax
 */
public class StatusContro {
    private static boolean start = false;

    public static boolean isStart() {
        return start;
    }

    public static void setStart(boolean start) {
        StatusContro.start = start;
    }
}
