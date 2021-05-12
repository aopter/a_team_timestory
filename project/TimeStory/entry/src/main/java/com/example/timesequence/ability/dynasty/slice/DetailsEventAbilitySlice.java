package com.example.timesequence.ability.dynasty.slice;

import com.example.timesequence.ResourceTable;
import com.example.timesequence.Utils.HmOSImageLoader;
import com.example.timesequence.constant.Constant;
import com.example.timesequence.constant.ServiceConfig;
import com.example.timesequence.entity.Incident;
import com.example.timesequence.entity.UserUnlockDynastyIncident;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.Operation;
import ohos.agp.components.*;
import ohos.agp.utils.Color;
import ohos.app.dispatcher.TaskDispatcher;
import ohos.app.dispatcher.task.TaskPriority;
import ohos.eventhandler.EventHandler;
import ohos.eventhandler.EventRunner;
import ohos.eventhandler.InnerEvent;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DetailsEventAbilitySlice extends AbilitySlice {
    private Map<Integer,Text> textMap = new HashMap<>();
    private AbilitySlice abilitySlice = this;

    private DirectionalLayout directionalLayout;
    private Text dialog;

    private int dynastyId;
    private String dynastyNameStr;

    private List<Incident> incidentList = new ArrayList<>();

    private Text dynastyName;
    private ListContainer eventList;

    private Gson gson;//json解析
    private OkHttpClient okHttpClient;//网络访问请求对象
    private TaskDispatcher parallelTaskDispatcher;//并发任务分发器
    private TaskDispatcher uiTaskDispatcher;//ui线程任务分发器
    private EventRunner eventRunner = EventRunner.create(true);//线程投递器
    private EventHandler eventHandler = new EventHandler(eventRunner){
        @Override
        protected void processEvent(InnerEvent event) {
            super.processEvent(event);
            switch (event.eventId){
                case ServiceConfig.INCIDENT_URL_THREAD:
                    //获取已经解锁的事件信息
                    downloadUnlockDynastyIncidents();
                    break;
                case ServiceConfig.UNLOCK_INCIDENT_URL_THREAD:
                    //有事件
                    if(incidentList.size() != 0){
                        //加载页面
                        for(int i=0;i < incidentList.size();i++){
                            int finalI = i;
                            uiTaskDispatcher.syncDispatch(new Runnable() {
                                @Override
                                public void run() {
                                    addIncident(incidentList.get(finalI));
                                }
                            });
                        }
                    }else{
                        //无事件
                        showToast();
                    }

                    break;
            }
        }
    };

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_details_event);

        //获取数据
        dynastyId = intent.getIntParam("dynastyId",1);
        dynastyNameStr = intent.getStringParam("dynastyName");

        //初始化并发任务分发器
        parallelTaskDispatcher = createParallelTaskDispatcher("detailsEventPageParallelTaskDispatcher", TaskPriority.DEFAULT);
        uiTaskDispatcher = getUITaskDispatcher();

        //初始化Gson
        initGson();

        //初始化控件
        initLayout();

        //设置名称
        dynastyName.setText("历史上的" + dynastyNameStr);

        //网络获取朝代全部的事件
        downloadDynastyIncidents();

    }

    public void showToast(){
//        Text text = new Text(getContext());
//        text.setText("暂未添加事件，先去唐朝看看吧");
//        text.setTextSize(50);
//        text.setTextAlignment(TextAlignment.CENTER);
//        text.setLayoutConfig(new DirectionalLayout.LayoutConfig(DirectionalLayout.LayoutConfig.MATCH_PARENT, DirectionalLayout.LayoutConfig.MATCH_PARENT));
//        directionalLayout.addComponent(text);

        dialog.setText("暂未添加事件，先去唐朝看看吧");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        terminate();
    }


    //初始化Gson
    private void initGson(){
        gson = new GsonBuilder()
                .setPrettyPrinting()
                .serializeNulls()
                .setDateFormat("yy:mm:dd")
                .create();
    }

    //初始化控件
    private void initLayout(){
        dynastyName = (Text) findComponentById(ResourceTable.Id_dynasty_name);
//        eventList = (ListContainer) findComponentById(ResourceTable.Id_event_list);
        directionalLayout = (DirectionalLayout) findComponentById(ResourceTable.Id_incidents_list);
        dialog = (Text) findComponentById(ResourceTable.Id_dialog);
    }

    //网络获取朝代全部的事件
    public void downloadDynastyIncidents(){
        okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(ServiceConfig.SERVICE_ROOT+ServiceConfig.INCIDENT_URL+dynastyId)
                .build();
        Call call = okHttpClient.newCall(request);

        Runnable getAllDynasty = new Runnable() {
            @Override
            public void run() {
                Response response = null;
                try {
                    response = call.execute();
                    String json = response.body().string();
                    //1.得到集合类型
                    Type type = new TypeToken<List<Incident>>() {
                    }.getType();
                    //2.反序列化
                    incidentList = gson.fromJson(json, type);

                    eventHandler.sendEvent(ServiceConfig.INCIDENT_URL_THREAD);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };

        //启动线程
        parallelTaskDispatcher.asyncDispatch(getAllDynasty);
    }

    //网络获取用户解锁的朝代的事件
    private void downloadUnlockDynastyIncidents(){
        okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(ServiceConfig.SERVICE_ROOT+ServiceConfig.UNLOCK_INCIDENT_URL + Constant.User.getUserId() + "/" + dynastyId)
                .build();
        Call call = okHttpClient.newCall(request);

        Runnable getAllDynasty = new Runnable() {
            @Override
            public void run() {
                Response response = null;
                try {
                    response = call.execute();
                    String json = response.body().string();
                    //1.得到集合类型
                    Type type = new TypeToken<List<UserUnlockDynastyIncident>>() {
                    }.getType();
                    //2.反序列化
                    List<UserUnlockDynastyIncident> unlockDynastyIncidentList = gson.fromJson(json, type);
                    Constant.UnlockDynastyIncident = unlockDynastyIncidentList;
                    eventHandler.sendEvent(ServiceConfig.UNLOCK_INCIDENT_URL_THREAD);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };

        //启动线程
        parallelTaskDispatcher.asyncDispatch(getAllDynasty);
    }

    //添加朝代事件
    public void addIncident(Incident incident){
        Component component = LayoutScatter.getInstance(getContext()).parse(ResourceTable.Layout_event_item_layout,null,false);
        Text eventName = (Text) component.findComponentById(ResourceTable.Id_event_name);
        Image eventImg = (Image) component.findComponentById(ResourceTable.Id_event_img);

        //加入map
        textMap.put(incident.getIncidentId(),eventName);

        if(isUnlock(incident.getIncidentId())){
            //放入深颜色
            eventName.setTextColor(Color.BLACK);
        }else{
            //放入浅颜色
            eventName.setTextColor(Color.GRAY);
        }
        //放入名称
        eventName.setText(incident.getIncidentName());
        eventName.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                //跳转到指定的事件详情页面
                Intent intent = new Intent();
                Operation operation = new Intent.OperationBuilder()
                        .withDeviceId("")
                        .withBundleName("com.example.timesequence")
                        .withAbilityName("com.example.timesequence.ability.dynasty.DetailsEventIntroduceAbility")
                        .build();
                intent.setParam("dynastyId",dynastyId);
                intent.setParam("eventId",incident.getIncidentId());
                intent.setOperation(operation);
                startAbility(intent,0);
            }
        });

        //获取网络图片放入eventImg
        String[] urls = incident.getIncidentPicture().split(Constant.DELIMITER);
        String url = urls[urls.length - 1];
        HmOSImageLoader.with(abilitySlice).load(ServiceConfig.SERVICE_ROOT+"/img/"+url).into(eventImg);

        directionalLayout.addComponent(component);
    }

    //判断事件是否解锁
    private boolean isUnlock(int incidentId){
        for(int i = 0;i < Constant.UnlockDynastyIncident.size();i++){
            if(Constant.UnlockDynastyIncident.get(i).getIncidentId().intValue() == incidentId){
                return true;
            }
        }
        return false;
    }

    @Override
    public void onActive() {
        super.onActive();
        if(incidentList != null){
            //加载页面
            for(int i=0;i < incidentList.size();i++){
                if(isUnlock(incidentList.get(i).getIncidentId())){
                    textMap.get(incidentList.get(i).getIncidentId()).setTextColor(Color.BLACK);
                }
            }
        }
    }

    @Override
    public void onForeground(Intent intent) {
        super.onForeground(intent);
    }
}
