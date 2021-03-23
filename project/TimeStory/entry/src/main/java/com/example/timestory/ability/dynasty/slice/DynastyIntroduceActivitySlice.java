package com.example.timestory.ability.dynasty.slice;

import com.example.timestory.ResourceTable;
import com.example.timestory.constant.ServiceConfig;
import com.example.timestory.entity.Dynasty;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.Operation;
import ohos.agp.components.Button;
import ohos.agp.components.Component;
import ohos.agp.components.Text;
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
import java.util.List;

public class DynastyIntroduceActivitySlice extends AbilitySlice {
    private int dynastyId;
    private Dynasty dynasty;

    private Button back;//返回按钮
    private Text dynastyName;//朝代名称
    private Text dynastyIntroduction;//朝代简介
    private Button map;//全景图
    private Button problem;//答题
    private Button event;//事件

    private Gson gson;//json解析
    private OkHttpClient okHttpClient;//网络访问请求对象
    private TaskDispatcher parallelTaskDispatcher;//并发任务分发器
    private EventRunner eventRunner = EventRunner.create(true);//线程投递器
    private EventHandler eventHandler = new EventHandler(eventRunner){
        @Override
        protected void processEvent(InnerEvent event) {
            super.processEvent(event);
            switch (event.eventId){
                case ServiceConfig.DYNASTY_INFO_THREAD:
                    //将信息显示在页面上
                    dynastyName.setText(dynasty.getDynastyName());
                    dynastyIntroduction.setText(dynasty.getDynastyInfo());
                    break;
            }
        }
    };



    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_dynasty_introduce_activity);

        //获取朝代id的信息
        dynastyId = intent.getIntParam("dynastyId",0);

        //初始化并发任务分发器
        parallelTaskDispatcher = createParallelTaskDispatcher("dynastyIntroducePageParallelTaskDispatcher", TaskPriority.DEFAULT);

        //初始化控件
        initLayout();

        //设置监听器
        setListener();

        //初始化Gson
        initGson();

        //网络请求数据
        downloadDynastyIntroduce();

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
    private void initLayout() {
        back = (Button) findComponentById(ResourceTable.Id_back);
        dynastyName = (Text) findComponentById(ResourceTable.Id_dynasty_name);
        dynastyIntroduction = (Text) findComponentById(ResourceTable.Id_dynasty_introduction);
        map = (Button) findComponentById(ResourceTable.Id_map);
        problem = (Button) findComponentById(ResourceTable.Id_problem);
        event = (Button) findComponentById(ResourceTable.Id_event);

    }

    //设置监听器
    private void setListener(){
        MyListener myListener = new MyListener();
        back.setClickedListener(myListener);
        map.setClickedListener(myListener);
        problem.setClickedListener(myListener);
        event.setClickedListener(myListener);
    }

    //网络请求，获取朝代简介
    private void downloadDynastyIntroduce(){
        okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(ServiceConfig.SERVICE_ROOT+ServiceConfig.DYNASTY_INFO+dynastyId)
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
                    Type type = new TypeToken<List<Dynasty>>() {
                    }.getType();
                    //2.反序列化
                    dynasty = gson.fromJson(json, Dynasty.class);
                    eventHandler.sendEvent(ServiceConfig.DYNASTY_INFO_THREAD);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };

        //启动线程
        parallelTaskDispatcher.asyncDispatch(getAllDynasty);
    }

    //监听器
    private class MyListener implements Component.ClickedListener{
        @Override
        public void onClick(Component component) {
            switch (component.getId()){
                case ResourceTable.Id_back:
                    terminateAbility();
                    break;
                case ResourceTable.Id_map:
                    //跳转全景图
                    break;
                case ResourceTable.Id_problem:
                    //跳转答题页面
                    break;
                case ResourceTable.Id_event:
                    //跳转朝代事件页面
                    Intent intent = new Intent();
                    Operation operation = new Intent.OperationBuilder()
                            .withDeviceId("")
                            .withBundleName("com.example.timestory")
                            .withAbilityName("com.example.timestory.ability.dynasty.DetailsEventAbility")
                            .build();
                    intent.setParam("dynastyId",dynastyId);
                    intent.setParam("dynastyName",dynasty.getDynastyName());
                    intent.setOperation(operation);
                    startAbility(intent);
                    break;
            }
        }
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
