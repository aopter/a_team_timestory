package com.example.timestory.constant;


import com.example.timestory.entity.*;
import ohos.distributedschedule.interwork.DeviceInfo;
import ohos.distributedschedule.interwork.DeviceManager;
import ohos.media.image.PixelMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 安卓用到的常量 全局对象等等
 */
public class Constant {
    //分隔符
    public static String DELIMITER = "&&&";
    //当前登录的用户
    public static com.example.timestory.entity.User User = new User();
    //历史上的今天事件集合
    public static List<HistoryDay> historyDays = new ArrayList<>();
    //当前登录用户解锁的朝代
    public static List<UserUnlockDynasty> UnlockDynasty = new ArrayList<>();
    //规则详情
    public static Rule rule;
    //当前登录用户解锁的某朝代的事件
    public static List<UserUnlockDynastyIncident> UnlockDynastyIncident = new ArrayList<>();
    //规则详情
    public static Rule Rule = new Rule();

    public static List<User> UserRankList;

    public static List<Problem> userProblems;//题目列表

    public static UserDetails UserDetails = new UserDetails();

    public static List<Dynasty> dynastiesName = new ArrayList<>();

//    public static Bitmap shareBitmap;//图像

    public static List<UserStatus> userStatuses = new ArrayList<>();
    //用户是否更改图片，未更改为0，更改后为1
    public static int ChangeHeader;

    public static long Random;
    /**
     * 手机号长度
     */
    public static final int PHONE_LENGTH = 11;


    public static int descCount = 60;

    public static Map<String, PixelMap> eventPics = new HashMap<>();
    public static String[] SEXARR = new String[]{"男", "女"};

    /**
     * 获得所有已经连接的所有设备ID
     * @return 设备ID列表
     */
    public static List<String> getAvailableDeviceIds() {
        // 获得DeviceInfo列表，包含了已经连接的所有设备信息
        List<DeviceInfo> deviceInfoList = DeviceManager.getDeviceList(DeviceInfo.FLAG_GET_ONLINE_DEVICE);
        // 如果DeviceInfo列表为空则返回
        if (deviceInfoList == null || deviceInfoList.size() == 0) {
            return null;
        }
        // 遍历DeviceInfo列表，获得所有的设备ID
        List<String> deviceIds = new ArrayList<>();
        for (DeviceInfo deviceInfo : deviceInfoList) {
            deviceIds.add(deviceInfo.getDeviceId());
        }
        // 返回所有的设备ID
        return deviceIds;
    }

////    积分不足提示
//    public static void showCountDialog(Context context) {
//        CustomDialog.Builder builder = new CustomDialog.Builder(context);
//        builder.setTitle("积分不足");
//        builder.setMessage("您的积分不足，快去选择朝代进行答题来赚取积分吧~");
//
//        CustomDialog customDialog = builder.create();
//        customDialog.setCancelable(false);
//        customDialog.setCanceledOnTouchOutside(false);
//        customDialog.show();
//        builder.setButtonConfirm("确定", new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                customDialog.dismiss();
//            }
//        });
//    }
}
