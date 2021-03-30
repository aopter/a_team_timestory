package com.example.timestory.ability.user.slice;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.timestory.ResourceTable;
import com.example.timestory.Utils.HmOSImageLoader;
import com.example.timestory.ability.dynasty.slice.HomePageAbilitySlice;
import com.example.timestory.ability.user.adapter.HistoryTodayAdapter;
import com.example.timestory.ability.user.adapter.UserRankingAdapter;
import com.example.timestory.constant.Constant;
import com.example.timestory.constant.ServiceConfig;
import com.example.timestory.entity.HistoryDay;
import com.example.timestory.entity.User;
import com.example.timestory.entity.UserStatus;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.*;
import ohos.agp.window.dialog.ToastDialog;
import ohos.eventhandler.EventHandler;
import ohos.eventhandler.EventRunner;
import ohos.eventhandler.InnerEvent;
import okhttp3.*;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

public class UserCenterAbilitySlice extends AbilitySlice implements Component.ClickedListener {
    private Image mUserCenterHeader;
    private ProgressBar mUserCenterPb;
    private Text mUserCenterTxLevel;
    private DependentLayout mDlLevel;
    private DependentLayout mUserCenterCardDl;
    private DependentLayout mUserCenterGoDynastyDl;
    private Text mUserCenterTxPoint;
    private DependentLayout mUserCenterPointDl;
    private DependentLayout mUserCenterSettingDl;
    private ListContainer mUserCenterRankingLc;
    private ListContainer mUserCenterHistoryTodayLc;
    private Text mUserCenterMyCard;
    private Text mUserCenterMyCollections;
    private OkHttpClient okHttpClient = new OkHttpClient();
    private boolean flag;
    private EventRunner eventRunner = EventRunner.create(true);
    private EventHandler eventHandler = new EventHandler(eventRunner) {
        @Override
        protected void processEvent(InnerEvent event) {
            super.processEvent(event);
            switch (event.eventId) {
                //加载用户榜单
                case 1:
                    UserRankingAdapter userRankingAdapter = new UserRankingAdapter(Constant.UserRankList, UserCenterAbilitySlice.this, UserCenterAbilitySlice.this.getContext());
                    mUserCenterRankingLc.setItemProvider(userRankingAdapter);
                    break;
                case 2:
                    HistoryTodayAdapter historyTodayAdapter = new HistoryTodayAdapter(Constant.historyDays, UserCenterAbilitySlice.this, UserCenterAbilitySlice.this.getContext());
                    mUserCenterHistoryTodayLc.setItemProvider(historyTodayAdapter);
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_user_center);
        initView();
        setDate();
        setClickedListener();
    }

    /**
     * 初始化进度条
     */
    private void initProgress() {
        long userExperience = Constant.User.getUserExperience();
        UserStatus userStatus = Constant.User.getUserStatus();
        long experMax = userStatus.getStatusExperienceTop();
        long experMin = userStatus.getStatusExperienceLow();
        long experOnStatus = experMax - experMin;
        DecimalFormat df = new DecimalFormat("0.00");
        String rate = df.format((float) (userExperience - experMin) / experOnStatus);
        double exRate = Double.parseDouble(rate);
        int progress = (int) (exRate * 100);
        mUserCenterPb.setProgressValue(progress);
    }

    private void setDate() {
        //头像
        HmOSImageLoader.with(UserCenterAbilitySlice.this).load(ServiceConfig.SERVICE_ROOT + "/img/" + Constant.User.getUserHeader()).into(mUserCenterHeader);
        //进度条
        initProgress();
        //等级
        mUserCenterTxLevel.setText(Constant.User.getUserStatus().getStatusName());
        //积分
        mUserCenterTxPoint.setText(Constant.User.getUserCount());
        //排行榜
        getUserRank();
        //历史上的今天
        getHistoryToday();
        flag = true;
    }

    //获取历史上的今天
    private void getHistoryToday() {
        //直接去提交
        if (Constant.historyDays.size() > 0) {
            eventHandler.sendEvent(2);
            return;
        }
        Request.Builder builder = new Request.Builder();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getDefault());
        //月份前面加1，是因为从0开始计算，需要加1操作
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        builder.url(ServiceConfig.HISTORY_TODAY + "?v=1.0&month=" + month + "&day=" + day + "&key=7a9cf9c5a9ff6338f5484d484ba51587");
        //构造请求类
        Request request = builder.build();
        final Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                assert response.body() != null;
                String jsonData = response.body().string();
                try {
                    JSONObject jsonObject = JSONObject.parseObject(jsonData);
                    JSONArray jsonArray = jsonObject.getJSONArray("result");
                    for (int i = 0; i < jsonArray.size(); ++i) {
                        JSONObject his = jsonArray.getJSONObject(i);
                        HistoryDay historyDay = new HistoryDay();
                        historyDay.setDes(his.getString("des"));
                        historyDay.setTitle(his.getString("title"));
                        historyDay.setLunar(his.getString("lunar"));
                        historyDay.setYear(his.getInteger("year"));
                        Constant.historyDays.add(historyDay);
                    }
                    eventHandler.sendEvent(3);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void getUserRank() {
        Request.Builder builder = new Request.Builder();
        builder.url(ServiceConfig.SERVICE_ROOT + "/user/list");
        //构造请求类
        Request request = builder.build();
        final Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                assert response.body() != null;
                String jsonData = response.body().string();
                Constant.UserRankList = new Gson().fromJson(jsonData, new TypeToken<List<User>>() {
                }.getType());
                eventHandler.sendEvent(1);
            }
        });

    }

    private void setClickedListener() {
        mUserCenterHeader.setClickedListener(this);
        mDlLevel.setClickedListener(this);
        mUserCenterCardDl.setClickedListener(this);
        mUserCenterGoDynastyDl.setClickedListener(this);
        mUserCenterPointDl.setClickedListener(this);
        mUserCenterSettingDl.setClickedListener(this);
        mUserCenterMyCard.setClickedListener(this);
        mUserCenterMyCollections.setClickedListener(this);
    }

    private void initView() {
        mUserCenterHeader = (Image) findComponentById(ResourceTable.Id_user_center_header);
        mUserCenterPb = (ProgressBar) findComponentById(ResourceTable.Id_user_center_pb);
        mUserCenterTxLevel = (Text) findComponentById(ResourceTable.Id_user_center_tx_level);
        mDlLevel = (DependentLayout) findComponentById(ResourceTable.Id_dl_level);
        mUserCenterCardDl = (DependentLayout) findComponentById(ResourceTable.Id_user_center_card_dl);
        mUserCenterGoDynastyDl = (DependentLayout) findComponentById(ResourceTable.Id_user_center_go_dynasty_dl);
        mUserCenterTxPoint = (Text) findComponentById(ResourceTable.Id_user_center_tx_point);
        mUserCenterPointDl = (DependentLayout) findComponentById(ResourceTable.Id_user_center_point_dl);
        mUserCenterSettingDl = (DependentLayout) findComponentById(ResourceTable.Id_user_center_setting_dl);
        mUserCenterRankingLc = (ListContainer) findComponentById(ResourceTable.Id_user_center_ranking_lc);
        mUserCenterHistoryTodayLc = (ListContainer) findComponentById(ResourceTable.Id_user_center_history_today_lc);
        mUserCenterMyCard = (Text) findComponentById(ResourceTable.Id_user_center_my_card);
        mUserCenterMyCollections = (Text) findComponentById(ResourceTable.Id_user_center_my_collections);
    }

    @Override
    public void onActive() {
        super.onActive();
    }

    @Override
    public void onForeground(Intent intent) {
        super.onForeground(intent);
    }

    @Override
    public void onClick(Component component) {
        switch (component.getId()) {
            //头像
            case ResourceTable.Id_user_center_header:
                Intent intent = new Intent();
                intent.setFlags(Intent.FLAG_ABILITY_CLEAR_MISSION | Intent.FLAG_ABILITY_NEW_MISSION);
                present(new HomePageAbilitySlice(), intent);
                this.terminate();
                break;
            //等级
            case ResourceTable.Id_dl_level:
                if (flag) {
                    mUserCenterTxLevel.setText("" + Constant.User.getUserExperience() + "/" + Constant.User.getUserStatus().getStatusExperienceTop());
                    flag = false;
                } else {
                    mUserCenterTxLevel.setText(Constant.User.getUserStatus().getStatusName());
                    flag = true;
                }
                break;
            //抽卡
            case ResourceTable.Id_user_center_card_dl:
                //抽卡
                //TODO
                break;
            //出征
            case ResourceTable.Id_user_center_go_dynasty_dl:
                present(new HomePageAbilitySlice(), new Intent());
                break;
            //积分
            case ResourceTable.Id_user_center_point_dl:
                present(new RechargeAbilitySlice(), new Intent());
                break;
            //设置
            case ResourceTable.Id_user_center_setting_dl:
                present(new SettingAbilitySlice(), new Intent());
                break;
            case ResourceTable.Id_user_center_my_card:
                //跳转到我的卡片页面
                //TODO
                break;
            case ResourceTable.Id_user_center_my_collections:
                //跳转到收藏页面
                //TODO
                break;
            default:
                break;
        }
    }
}