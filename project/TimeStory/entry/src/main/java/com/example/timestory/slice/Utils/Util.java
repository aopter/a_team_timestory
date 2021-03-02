package com.example.timestory.slice.Utils;

import ohos.aafwk.content.Intent;
import ohos.aafwk.content.Operation;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;

/**
 * @author PengHuAnZhi
 * @createTime 2021/3/2 18:32
 * @projectName TimeStory
 * @className Util.java
 * @description TODO
 */
public class Util {

    private static final String BUNDLE_NAME = "com.example.timestory";

    // HiLogLabel定义了域信息和标签等
    private static final HiLogLabel LOG_LABEL = new HiLogLabel(HiLog.LOG_APP, 0x00101, "timeStory");

    /**
     * HiLog输出
     *
     * @param logStr 输出字符串
     */
    public static void log(String logStr) {
        HiLog.info(LOG_LABEL, logStr);
    }

    /**
     * 生成Intent对象用于Page之间的跳转
     *
     * @param pagename Page Ability的全名称
     * @return Intent对象
     */
    public static Intent generatePageNavigationIntent(String pagename) {
        // 创建Intent对象
        Intent intent = new Intent();
        // 创建Operation对象
        Operation operation = new Intent.OperationBuilder() // 创建Operation对象
                .withDeviceId("")  // 目标设备，空字符串代表本设备
                .withBundleName(BUNDLE_NAME) // 通过BundleName指定应用程序
                .withAbilityName(pagename) // 通过Ability的全名称（包名+类名）指定启动的Ability
                .build();
        // 设置Intent对象的operation属性
        intent.setOperation(operation);
        return intent;
    }
}