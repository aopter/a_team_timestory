package com.example.timestory.ability.dynasty.slice;

import com.example.timestory.ResourceTable;
import com.example.timestory.Utils.HmOSImageLoader;
import com.example.timestory.constant.Constant;
import com.example.timestory.constant.ServiceConfig;
import com.example.timestory.entity.Incident;
import com.example.timestory.entity.Result;
import com.example.timestory.entity.UserUnlockDynasty;
import com.example.timestory.entity.UserUnlockDynastyIncident;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.*;
import ohos.agp.window.dialog.ToastDialog;
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

public class DetailsEventIntroduceSlice extends AbilitySlice {
    private int dynastyId;//朝代Id
    private int eventId;//事件Id
    private Incident incident;//事件对象
    private String[] dialogList;//对话数组
    private int i = 0;//对话进行的个数

    private Button back;//返回按钮
    private Text eventName;//事件名称
    private Text eventIntroduce;//事件简介
    private Image picPangbai;//旁白图片
    private DirectionalLayout dialogBox;//对话框布局
    private DependentLayout dialogue;//对话进行的点击按钮
    private Image picOne;//第一个谈话者
    private Image picTwo;//第二个谈话者

    private Gson gson;//json解析
    private OkHttpClient okHttpClient;//网络访问请求对象
    private TaskDispatcher parallelTaskDispatcher;//并发任务分发器
    private EventRunner eventRunner = EventRunner.create(true);//线程投递器
    private EventHandler eventHandler = new EventHandler(eventRunner){
        @Override
        protected void processEvent(InnerEvent event) {
            super.processEvent(event);
            switch (event.eventId){
                case ServiceConfig.INCIDENT_DETAILS_URL_THREAD:
                   setLayout();
                    break;
            }
        }
    };

    @Override
    protected void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_details_event_introduce);

        //获取信息
        dynastyId = intent.getIntParam("dynastyId",1);
        eventId = intent.getIntParam("eventId",1);

        //初始化并发任务分发器
        parallelTaskDispatcher = createParallelTaskDispatcher("detailsEventIntroducePageParallelTaskDispatcher", TaskPriority.DEFAULT);

        //初始化Gson
        initGson();

        //初始化页面控件
        initLayout();
        //设置监听器
        setListener();

        downloadDynastyIncident();

    }

    //初始化Gson
    private void initGson(){
        gson = new GsonBuilder()
                .setPrettyPrinting()
                .serializeNulls()
                .setDateFormat("yy:mm:dd")
                .create();
    }

    //初始化页面控件
    private void initLayout() {
        back = (Button) findComponentById(ResourceTable.Id_back);
        eventName = (Text) findComponentById(ResourceTable.Id_event_name);
        eventIntroduce = (Text) findComponentById(ResourceTable.Id_event_introduce);
        picPangbai = (Image) findComponentById(ResourceTable.Id_pic_pangbai);
        dialogBox = (DirectionalLayout) findComponentById(ResourceTable.Id_dialog_box);
        dialogue = (DependentLayout) findComponentById(ResourceTable.Id_dialogue);
        picOne = (Image) findComponentById(ResourceTable.Id_pic_one);
        picTwo = (Image) findComponentById(ResourceTable.Id_pic_two);
    }

    //设置监听器
    private void setListener(){
        MyListener myListener = new MyListener();
        back.setClickedListener(myListener);
        dialogue.setClickedListener(myListener);
    }

    //监听器
    private class MyListener implements Component.ClickedListener{
        @Override
        public void onClick(Component component) {
            switch (component.getId()){
                case ResourceTable.Id_back:
                    //关闭页面
                    terminate();
                    break;
                case ResourceTable.Id_dialogue:

                    if(i % 2 == 0){
                        addDialogLayout(ResourceTable.Layout_dialog_text_left_item);
                    }else {
                        addDialogLayout(ResourceTable.Layout_dialog_text_right_item);
                    }
                    break;
            }
        }
    }

    //网络获取朝代事件详情
    public void downloadDynastyIncident(){
        okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(ServiceConfig.SERVICE_ROOT+ServiceConfig.INCIDENT_DETAILS_URL+eventId)
                .build();
        Call call = okHttpClient.newCall(request);

        Runnable getAllDynasty = new Runnable() {
            @Override
            public void run() {
                Response response = null;
                try {
                    response = call.execute();
                    String json = response.body().string();

                    //获取对象
                    incident = gson.fromJson(json,Incident.class);
                    //获取list集合
                    dialogList = incident.getIncidentDialog().split(Constant.DELIMITER);
                    eventHandler.sendEvent(ServiceConfig.INCIDENT_DETAILS_URL_THREAD);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };

        //启动线程
        parallelTaskDispatcher.asyncDispatch(getAllDynasty);
    }

    //设置页面
    public void setLayout(){
        //设置标题
        eventName.setText(incident.getIncidentName());
        //设置内容
        eventIntroduce.setText(incident.getIncidentInfo());
        //加载旁白图片
        String[] urls = incident.getIncidentPicture().split(Constant.DELIMITER);
        String url = urls[urls.length - 2];
        HmOSImageLoader.with(this).load(ServiceConfig.SERVICE_ROOT+"/img/"+url).into(picPangbai);

        //添加对话布局
        addDialogLayout(ResourceTable.Layout_dialog_text_left_item);


        //设置两个小人的GIF
//        HmOSImageLoader.with(this).load(ServiceConfig.SERVICE_ROOT+"/img/"+urls[urls.length - 3]).into(picOne);
//        HmOSImageLoader.with(this).load(ServiceConfig.SERVICE_ROOT+"/img/"+urls[urls.length - 4]).into(picTwo);
    }

    //添加对话布局
    public void addDialogLayout(int layoutItem){
        if(i < dialogList.length){
            Component component = LayoutScatter.getInstance(getContext()).parse(layoutItem,null,false);
            Text text = (Text) component.findComponentById(ResourceTable.Id_sentence);
            text.setText(dialogList[i]);
            i++;
            dialogBox.addComponent(component);
        }else{
            new ToastDialog(getContext()).setText("事件完成").show();
            //事件完成，监察之前是否已经解锁，如果没有解锁，那就解锁
            addUnlockIncidents();
            //取消监听器
            dialogue.setClickedListener(null);
            //判断是否可以解锁下个朝代
            isPass(dynastyId);

        }
    }

    /**
     * 添加看过的事件
     */
    private void addUnlockIncidents() {
        Request request = new Request.Builder()
                .url(ServiceConfig.SERVICE_ROOT + ServiceConfig.UNLOCK_INCIDENT_ADD + Constant.User.getUserId() + "/" + dynastyId + "/" + eventId)
                .build();
        Call call = okHttpClient.newCall(request);
        new Thread(){
            @Override
            public void run() {
                try {
                    Response response = call.execute();
                    String json = response.body().string();
                    System.out.println("____json____"+json);
                    Result result = gson.fromJson(json, Result.class);
                    String isAdd = result.getResult();
                    System.out.println("____isadd____"+isAdd);
                    if ("true".equals(isAdd)){
                        long experience = Constant.User.getUserExperience();
                        experience = experience + 15;
                        Constant.User.setUserExperience(experience);
                    }
                    //添加到解锁事件列表
                    UserUnlockDynastyIncident userUnlockDynastyIncident = new UserUnlockDynastyIncident();
                    userUnlockDynastyIncident.setIncidentId(incident.getIncidentId());
                    userUnlockDynastyIncident.setIncidentName(incident.getIncidentName());
                    userUnlockDynastyIncident.setIncidentPicture(incident.getIncidentPicture());
                    Constant.UnlockDynastyIncident.add(userUnlockDynastyIncident);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    /**
     * 判断下一个朝代是否可以解锁
     *
     * @param dynastyId
     */
    //放到对话中
    private void isPass(int dynastyId) {
        //Constant.User.getUserId()
        Request request = new Request.Builder()
                .url(ServiceConfig.SERVICE_ROOT + ServiceConfig.DYNASTY_ISPASS + Constant.User.getUserId() + "/" + dynastyId)
                .build();

        Call call = okHttpClient.newCall(request);
        new Thread(() -> {
            try {
                Response response = call.execute();
                String json = response.body().string();

                Result result = gson.fromJson(json, Result.class);
                String isPass =  result.getResult();
                if ("true".equals(isPass)) {
                    //先判断下一朝代是否解锁，后解锁下一朝代
                    addUnlockDynasty(dynastyId);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

    }

    /**
     * 解锁下一个朝代
     */
    //放到对话中
    private void addUnlockDynasty(int dynastyId) {
        int dynastyID = dynastyId + 1;
        Request request = new Request.Builder()
                .url(ServiceConfig.SERVICE_ROOT + ServiceConfig.UNLOCK_DYNASTY_ADD + Constant.User.getUserId() + "/" + dynastyID)
                .build();
        Call call = okHttpClient.newCall(request);
        new Thread(() -> {
            try {
                Response response = call.execute();
                String json = response.body().string();
                Result result = gson.fromJson(json, Result.class);
                String isPass = result.getResult();
                if ("true".equals(isPass)) {
                    UserUnlockDynasty unlockDynasty = new UserUnlockDynasty();
                    unlockDynasty.setDynastyId(dynastyID + "");
                    unlockDynasty.setDynastyName(Constant.dynastiesName.get(dynastyID - 1).getDynastyName());
                    unlockDynasty.setProgress(0);
                    unlockDynasty.setUserId(Constant.User.getUserId());
                    Constant.UnlockDynasty.add(unlockDynasty);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    @Override
    public void onActive() {
        super.onActive();
    }

    @Override
    public void onForeground(Intent intent) {
        super.onForeground(intent);
    }
}
