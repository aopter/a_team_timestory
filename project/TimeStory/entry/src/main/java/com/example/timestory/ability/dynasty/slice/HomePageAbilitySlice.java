package com.example.timestory.ability.dynasty.slice;

import com.example.timestory.ResourceTable;
import com.example.timestory.Utils.HmOSImageLoader;
import com.example.timestory.ability.dynasty.HomePageAbility;
import com.example.timestory.constant.Constant;
import com.example.timestory.constant.ServiceConfig;
import com.example.timestory.entity.Dynasty;
import com.example.timestory.entity.User;
import com.example.timestory.entity.UserStatus;
import com.example.timestory.entity.UserUnlockDynasty;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.Operation;
import ohos.agp.colors.RgbColor;
import ohos.agp.components.*;
import ohos.agp.components.element.ShapeElement;
import ohos.agp.utils.TextAlignment;
import ohos.agp.window.dialog.ToastDialog;
import ohos.app.dispatcher.TaskDispatcher;
import ohos.app.dispatcher.task.TaskPriority;
import ohos.eventhandler.EventHandler;
import ohos.eventhandler.EventRunner;
import ohos.eventhandler.InnerEvent;
import ohos.media.image.PixelMap;
import okhttp3.*;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomePageAbilitySlice extends AbilitySlice {
    //标识显示的是等级还是经验，0等级，1经验
    int flag = 0;
    private Image userHeader;//用户头像
    private ProgressBar progressBar;//用户经验条
    private Text experience;//用户经验等级
    private Text drawCard;//用户抽卡
    private Text userCard;//用户卡片
    private Text userCollection;//用户收藏
    private Text userCount;//用户积分
    private Image recharge;//充值
    private Map<String,Image> textImage = new HashMap<>();

    private DirectionalLayout dynastyTop;//上层朝代父布局
    private DirectionalLayout dynastyBottom;//下层朝代父布局
    private Gson gson;//Json转换框架
    private OkHttpClient okHttpClient;//网络访问请求对象
    private TaskDispatcher parallelTaskDispatcher;//并发任务分发器
    private EventRunner eventRunner = EventRunner.create(true);//线程投递器
    private EventHandler eventHandler = new EventHandler(eventRunner){
        @Override
        protected void processEvent(InnerEvent event) {
            super.processEvent(event);
            switch (event.eventId){
                case ServiceConfig.DYNASTY_LIST_THREAD:
                    downloadUnlockDynastyList();
                    break;
                case ServiceConfig.UNLOCK_DYNASTY_LIST_THREAD:
                    if(Constant.dynastiesName.size() <= 0 || Constant.UnlockDynasty.size() <= 0){
                        //报错

                        //跳出
                        break;
                    }
                    for(int i = 0;i < Constant.dynastiesName.size();i++){
                        addDynasty(i,Constant.dynastiesName.get(i).getDynastyName());
                    }
                    break;
            }
        }
    };//线程通讯工具

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_home_page);

        //TODO 假定设置用户
        User user = new User();
        user.setUserId(8);
        user.setUserAccount("13722328172");
        user.setUserPassword("1234567");
        user.setUserHeader("user/us-10.jpg");
        user.setUserNickname("超爱吃荔枝");
        user.setUserExperience(800);
        user.setUserCount(1000);
        user.setUserSignature("我是你爸爸");
        UserStatus userStatus = new UserStatus();
        userStatus.setStatusId(2);
        userStatus.setStatusExperienceLow(500);
        userStatus.setStatusExperienceTop(3200);
        userStatus.setStatusInfo("会试取中者");
        userStatus.setStatusName("秀才");
        user.setUserStatus(userStatus);
        Constant.User = user;



        //初始化并发任务分发器
        parallelTaskDispatcher = createParallelTaskDispatcher("homePageParallelTaskDispatcher", TaskPriority.DEFAULT);

        //获取控件
        initLayout();
        //设置监听器
        setListener();

        //初始化等级
        initStatus();

        //初始化用户信息
        initUserMessage();

        //初始化GSON
        initGson();
        //获取所有朝代的数据,并显示
        downAllDynasty();
    }

    //获取控件
    private void initLayout(){
        userHeader = (Image) findComponentById(ResourceTable.Id_user_header);
        progressBar = (ProgressBar) findComponentById(ResourceTable.Id_progress_bar);
        experience = (Text) findComponentById(ResourceTable.Id_experience);
        drawCard = (Text) findComponentById(ResourceTable.Id_draw_card);
        userCard = (Text) findComponentById(ResourceTable.Id_user_card);
        userCollection = (Text) findComponentById(ResourceTable.Id_user_collection);
        userCount = (Text) findComponentById(ResourceTable.Id_user_count);
        recharge = (Image) findComponentById(ResourceTable.Id_recharge);

        dynastyTop = (DirectionalLayout) findComponentById(ResourceTable.Id_dynasty_top);
        dynastyBottom = (DirectionalLayout) findComponentById(ResourceTable.Id_dynasty_bottom);
    }

    //下载所有的等级
    private void initStatus(){
        okHttpClient = new OkHttpClient();
        //加载地位
        if (Constant.userStatuses.size() < 1) {
            //请求
            Request.Builder builder1 = new Request.Builder();
            builder1.url(ServiceConfig.SERVICE_ROOT + "/status/list");
            //构造请求类
            Request request1 = builder1.build();
            final Call call1 = okHttpClient.newCall(request1);
            call1.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String jsonData = response.body().string();
                    Constant.userStatuses = gson.fromJson(jsonData, new TypeToken<List<UserStatus>>() {
                    }.getType());
                }
            });
        }
    }

    //初始化用户信息
    private void initUserMessage(){
        //初始化进度条
        initProgress();
        //初始化头像
        initHeader();
        //初始化用户积分
        userCount.setText(Constant.User.getUserCount() + "");
        //初始化经验等级
        initExperience();
    }

    //初始化经验等级
    private void initExperience(){
        if (Constant.User.getUserExperience() == Constant.User.getUserStatus().getStatusExperienceTop()) {
            Constant.User.setUserStatus(Constant.userStatuses.get(Constant.User.getUserStatus().getStatusId()));
        }
        experience.setText(Constant.User.getUserStatus().getStatusName());
        experience.setTextSize(35);
    }

    //初始化头像
    public void initHeader(){
        if (Constant.User.getFlag() == 0) {
            //手机号登录
            if (Constant.User.getUserHeader() != null){
                if (Constant.ChangeHeader == 0) {
                    //未修改头像
                    HmOSImageLoader.with(this).load(ServiceConfig.SERVICE_ROOT + "/img/" + Constant.User.getUserHeader()).into(userHeader);
                } else if (Constant.ChangeHeader == 1) {
                    //修改头像
                    Constant.Random = System.currentTimeMillis();
                    Constant.eventPics.remove(ServiceConfig.SERVICE_ROOT + "/img/" + Constant.User.getUserHeader());
                    HmOSImageLoader.with(this).load(ServiceConfig.SERVICE_ROOT + "/img/" + Constant.User.getUserHeader()).into(userHeader);
                    Constant.ChangeHeader = 0;
                }
            }
        } else if (Constant.User.getFlag() == 1) {
            //QQ登录
            HmOSImageLoader.with(this).load(Constant.User.getUserHeader()).into(userHeader);
        }
    }

    //初始化进度条
    private void initProgress(){
        long userExperience = Constant.User.getUserExperience();
        UserStatus userStatus = Constant.User.getUserStatus();
        long experMax = userStatus.getStatusExperienceTop();
        long experMin = userStatus.getStatusExperienceLow();
        long experOnStatus = experMax - experMin;
        DecimalFormat df = new DecimalFormat("0.00");
        String rate = df.format((float) (userExperience - experMin) / experOnStatus);
        double exRate = Double.parseDouble(rate);
        int progress = (int) (exRate * 100);
        progressBar.setProgressValue(progress);
    }

    //设置监听器
    private void setListener(){
        //创建监听器对象
        MyListener myListener = new MyListener();
        userHeader.setClickedListener(myListener);
        experience.setClickedListener(myListener);
        drawCard.setClickedListener(myListener);
        userCard.setClickedListener(myListener);
        userCollection.setClickedListener(myListener);
        recharge.setClickedListener(myListener);
    }

    //将数据添加到朝代布局中
    private void addDynasty(int position,String dynastyName){
        DependentLayout dependentLayout = new DependentLayout(getContext());
        Image image = new Image(getContext());
        image.setScaleMode(Image.ScaleMode.INSIDE);

        Text text = new Text(getContext());
        text.setText(dynastyName);
        text.setTextSize(60);
        text.setTextAlignment(TextAlignment.CENTER);
        text.setLayoutConfig(new DependentLayout.LayoutConfig(DependentLayout.LayoutConfig.MATCH_PARENT, DependentLayout.LayoutConfig.MATCH_PARENT));

        DirectionalLayout.LayoutConfig layoutConfig = new DirectionalLayout.LayoutConfig(380,380);
        if(isUnlockDynasty(position)){
            image.setPixelMap(ResourceTable.Media_shan);
            text.setClickedListener(new Component.ClickedListener() {
                @Override
                public void onClick(Component component) {
                    //页面跳转，跳转到指定的朝代信息页面
//                    ToastDialog toastDialog = new ToastDialog(getContext());
//                    toastDialog.setText("点击了"+Constant.dynastiesName.get(position).getDynastyName()+"-dynastyId为"+Constant.dynastiesName.get(position).getDynastyId());
//                    toastDialog.show();

                    Intent intent = new Intent();
                    Operation operation = new Intent.OperationBuilder()
                            .withDeviceId("")
                            .withBundleName("com.example.timestory")
                            .withAbilityName("com.example.timestory.ability.dynasty.DynastyIntroduceActivity")
                            .build();
                    intent.setParam("dynastyId",Constant.dynastiesName.get(position).getDynastyId());
                    intent.setOperation(operation);
                    startAbility(intent);

                }
            });
        }else{
            image.setPixelMap(ResourceTable.Media_shanhui);
            text.setClickedListener(new Component.ClickedListener() {
                @Override
                public void onClick(Component component) {
                    //提示未解锁
                    ToastDialog toastDialog = new ToastDialog(getContext());
                    toastDialog.setText("点击了"+Constant.dynastiesName.get(position).getDynastyName()+"-dynastyId为"+Constant.dynastiesName.get(position).getDynastyId()+"未解锁");
                    toastDialog.show();
                }
            });
        }
        if(position % 2 == 0){
            layoutConfig.setMargins(150,10,50,0);
            dependentLayout.setLayoutConfig(layoutConfig);
            dependentLayout.addComponent(image);
            dependentLayout.addComponent(text);
            dynastyTop.addComponent(dependentLayout);
        }
        else {
            if(position == 1){
                layoutConfig.setMargins(300,0,150,0);
                dependentLayout.setLayoutConfig(layoutConfig);
                dependentLayout.addComponent(image);
                dependentLayout.addComponent(text);
            }else{
                layoutConfig.setMargins(50,0,150,0);
                dependentLayout.setLayoutConfig(layoutConfig);
                dependentLayout.addComponent(image);
                dependentLayout.addComponent(text);
            }
            dynastyBottom.addComponent(dependentLayout);
        }
        textImage.put(dynastyName,image);
    }

    //判断是否解锁此朝代
    private boolean isUnlockDynasty(int position){
        boolean flag = false;
        for(int i = 0;i < Constant.UnlockDynasty.size();i++){
            if(Constant.dynastiesName.get(position).getDynastyId().toString().equals(Constant.UnlockDynasty.get(i).getDynastyId())){
                flag = true;
            }
        }
        return flag;
    }

    //初始化Gson
    private void initGson(){
        gson = new GsonBuilder()
                .setPrettyPrinting()
                .serializeNulls()
                .setDateFormat("yy:mm:dd")
                .create();
    }

    //获取全部的朝代名称
    private void downAllDynasty(){
        okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(ServiceConfig.SERVICE_ROOT+ServiceConfig.DYNASTY_LIST)
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
                    List<Dynasty> dynasty = gson.fromJson(json, type);
                    Constant.dynastiesName = dynasty;
                    eventHandler.sendEvent(ServiceConfig.DYNASTY_LIST_THREAD);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };

//        Runnable getAllDynasty = new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    URL url = new URL(ServiceConfig.SERVICE_ROOT  + ServiceConfig.DYNASTY_LIST);
//                    HttpURLConnection connection = (HttpURLConnection)url.openConnection();
//                    connection.setRequestMethod("GET");
//                    InputStream in = connection.getInputStream();
//                    BufferedReader reader = new BufferedReader(new InputStreamReader(in, "utf-8"));
//                    String json1 = reader.readLine();
////                    JSONObject obj = new JSONObject(json1);
////                    String json = obj.getJSONArray("Value").toString();
//
//                    //1.得到集合类型
//                    Type type = new TypeToken<List<Dynasty>>(){}.getType();
//                    //2.反序列化
//                    List<Dynasty> dynasties = gson.fromJson(json1, type);
//
//                    eventHandler.sendEvent(ServiceConfig.DYNASTY_LIST_THREAD);
//                    System.out.println("________________----------------------____________ss");
//                } catch (MalformedURLException e) {
//                    e.printStackTrace();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//
//            }
//        };

//        ZZRHttp.get(ServiceConfig.SERVICE_ROOT  + ServiceConfig.DYNASTY_LIST, new ZZRCallBack.CallBackString() {
//            @Override
//            public void onFailure(int code, String errorMessage) {
//                //http访问出错，此部分在主线程中工作,可以更新UI等操做。
//            }
//            @Override
//            public void onResponse(String response) {
//                //http访问成功，此部分在主线程中工作，可以更新UI等操作。
//
//                eventHandler.sendEvent(1);
//            }
//        });

        //启动线程
        parallelTaskDispatcher.asyncDispatch(getAllDynasty);
    }

    //获取用户已解锁的朝代信息
    private void downloadUnlockDynastyList(){
        okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(ServiceConfig.SERVICE_ROOT+ServiceConfig.UNLOCK_DYNASTY_LIST+Constant.User.getUserId())
                .build();
        Call call = okHttpClient.newCall(request);

        Runnable getUnlockDynasty = new Runnable() {
            @Override
            public void run() {
                Response response = null;
                try {
                    response = call.execute();
                    String json = response.body().string();
                    //1.得到集合类型
                    Type type = new TypeToken<List<UserUnlockDynasty>>() {
                    }.getType();
                    //2.反序列化
                    List<UserUnlockDynasty> unlockDynasty = gson.fromJson(json, type);
                    Constant.UnlockDynasty = unlockDynasty;
                    eventHandler.sendEvent(ServiceConfig.UNLOCK_DYNASTY_LIST_THREAD);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };

        //分发启动任务
        parallelTaskDispatcher.asyncDispatch(getUnlockDynasty);
    }

    //内部监听器类
    private class MyListener implements Component.ClickedListener{
        @Override
        public void onClick(Component component) {
            switch (component.getId()){
                case ResourceTable.Id_user_header://用户头像

                    break;
                case ResourceTable.Id_experience://用户经验
                    if(flag == 0){
                        experience.setText("" + Constant.User.getUserExperience() + "/" + Constant.User.getUserStatus().getStatusExperienceTop());
                        experience.setTextSize(30);
                        flag = 1;
                    }else {
                        experience.setText(Constant.User.getUserStatus().getStatusName());
                        experience.setTextSize(35);
                        flag = 0;
                    }
                    break;
                case ResourceTable.Id_draw_card://用户抽卡

                    break;
                case ResourceTable.Id_user_card://用户卡片

                    break;
                case ResourceTable.Id_user_collection://用户收藏

                    break;
                case ResourceTable.Id_recharge://用户充值

                    break;
            }
        }
    }

    @Override
    public void onActive() {
        super.onActive();
        flag = 0;
        initUserMessage();
        List<String> unlockDynastyIds = new ArrayList<>();
        for (int j = 0; j < Constant.UnlockDynasty.size(); j++) {
            unlockDynastyIds.add(Constant.UnlockDynasty.get(j).getDynastyName());
        }
        for(int i = 0;i < unlockDynastyIds.size();i++){
            if(textImage.containsKey(unlockDynastyIds.get(i))){
                Image image = textImage.get(unlockDynastyIds.get(i));
                image.setPixelMap(ResourceTable.Media_shan);
            }
        }
    }

    @Override
    public void onForeground(Intent intent) {
        super.onForeground(intent);
    }
}
