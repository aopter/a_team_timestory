package com.example.timestory.constant;

/**
 * 存放服务器相关常量 （路径）
 */
public class ServiceConfig {
    //历史上的今天接口
    public static String HISTORY_TODAY = "http://api.juheapi.com/japi/toh";

    public static String SERVICE_ROOT = "http://47.101.157.246:8889";

    public static String DYNASTY_LIST = "/dynasty/list";
    public static final int DYNASTY_LIST_THREAD = 1;

    public static final String UNLOCK_DYNASTY_LIST = "/userunlockdynasty/list/";
    public static final int UNLOCK_DYNASTY_LIST_THREAD = 2;

    public static final String DYNASTY_INFO = "/dynasty/details/";
    public static final int DYNASTY_INFO_THREAD = 3;

    public static final String INCIDENT_URL = "/incident/list/";
    public static final int INCIDENT_URL_THREAD = 4;

    public static final String UNLOCK_INCIDENT_URL = "/userincident/list/";
    public static final int UNLOCK_INCIDENT_URL_THREAD = 5;

    public static final String INCIDENT_DETAILS_URL = "/incident/details/";
    public static final int INCIDENT_DETAILS_URL_THREAD = 6;

    public static final String UNLOCK_DYNASTY_ADD = "/userunlockdynasty/addunlockdynasty/";
    public static final int UNLOCK_DYNASTY_ADD_THREAD = 7;

    public static final String DYNASTY_ISPASS = "/userunlockdynasty/ispass/";
    public static final int DYNASTY_ISPASS_THREAD = 8;

    public static final String UNLOCK_INCIDENT_ADD = "/userincident/unlock/";
    public static final int UNLOCK_INCIDENT_ADD_THREAD = 9;

}
