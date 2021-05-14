package com.example.timesequence.ability.dynasty.slice;

import com.example.timesequence.ResourceTable;
import com.example.timesequence.Utils.ToastUtil;
import com.example.timesequence.ability.problem.slice.SelectProblemSlice;
import com.example.timesequence.constant.ServiceConfig;
import com.example.timesequence.entity.Dynasty;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.Operation;
import ohos.agp.components.Button;
import ohos.agp.components.Component;
import ohos.agp.components.Text;
import ohos.ai.tts.TtsClient;
import ohos.ai.tts.TtsListener;
import ohos.ai.tts.TtsParams;
import ohos.ai.tts.constants.TtsEvent;
import ohos.app.dispatcher.TaskDispatcher;
import ohos.app.dispatcher.task.TaskPriority;
import ohos.eventhandler.EventHandler;
import ohos.eventhandler.EventRunner;
import ohos.eventhandler.InnerEvent;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.utils.PacMap;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.UUID;

public class DynastyIntroduceActivitySlice extends AbilitySlice {
    private static final HiLogLabel LABEL_LOG = new HiLogLabel(3, 0xD001100, "MainAbilitySlice");
    private int dynastyId;
    private Dynasty dynasty;

    private Button back;//返回按钮
    private Text dynastyName;//朝代名称
    private Text dynastyIntroduction;//朝代简介
    private Button map;//全景图
    private Button problem;//答题
    private Button event;//事件
    private Button speech;//语言播报
    private boolean initItsResult;

    private Gson gson;//json解析
    private OkHttpClient okHttpClient;//网络访问请求对象
    private TaskDispatcher parallelTaskDispatcher;//并发任务分发器


    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_dynasty_introduce_activity);

        //获取朝代id的信息
        dynastyId = intent.getIntParam("dynastyId", 0);

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
        initTtsEngine();
    }

    //初始化Gson
    private void initGson() {
        gson = new GsonBuilder()
                .setPrettyPrinting()
                .serializeNulls()
                .setDateFormat("yy:mm:dd")
                .create();
    }

    private void initTtsEngine() {
        TtsClient.getInstance().create(this, ttsListener);
    }

    private void readText(Component component) {
        if (initItsResult) {
            TtsClient.getInstance().speakText(dynastyIntroduction.getText(), null);
        } else {
            HiLog.error(LABEL_LOG, "initItsResult is false");
        }
    }

    private final TtsListener ttsListener = new TtsListener() {
        @Override
        public void onStart(String s) {

        }

        @Override
        public void onProgress(String s, byte[] bytes, int i) {

        }

        @Override
        public void onFinish(String s) {

        }

        @Override
        public void onError(String s, String s1) {

        }

        @Override
        public void onEvent(int eventType, PacMap pacMap) {
            // 定义TTS客户端创建成功的回调函数
            if (eventType == TtsEvent.CREATE_TTS_CLIENT_SUCCESS) {
                TtsParams ttsParams = new TtsParams();
                ttsParams.setDeviceId(UUID.randomUUID().toString());
                initItsResult = TtsClient.getInstance().init(ttsParams);
            }
        }

        @Override
        public void onSpeechStart(String s) {

        }

        @Override
        public void onSpeechProgressChanged(String s, int i) {

        }

        @Override
        public void onSpeechFinish(String s) {

        }
    };


    //初始化控件
    private void initLayout() {
        back = (Button) findComponentById(ResourceTable.Id_back);
        dynastyName = (Text) findComponentById(ResourceTable.Id_dynasty_name);
        dynastyIntroduction = (Text) findComponentById(ResourceTable.Id_dynasty_introduction);
        map = (Button) findComponentById(ResourceTable.Id_map);
        problem = (Button) findComponentById(ResourceTable.Id_problem);
        event = (Button) findComponentById(ResourceTable.Id_event);
        speech = (Button) findComponentById(ResourceTable.Id_speech);
    }

    //设置监听器
    private void setListener() {
        MyListener myListener = new MyListener();
        back.setClickedListener(myListener);
        map.setClickedListener(myListener);
        problem.setClickedListener(myListener);
        event.setClickedListener(myListener);
        speech.setClickedListener(this::readText);
    }

    //网络请求，获取朝代简介
    private void downloadDynastyIntroduce() {
        okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(ServiceConfig.SERVICE_ROOT + ServiceConfig.DYNASTY_INFO + dynastyId)
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
                    getMainTaskDispatcher().syncDispatch(() -> dynastyName.setText(dynasty.getDynastyName()));
                    getMainTaskDispatcher().syncDispatch(() -> dynastyIntroduction.setText(dynasty.getDynastyInfo()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };

        //启动线程
        parallelTaskDispatcher.asyncDispatch(getAllDynasty);
    }

    //监听器
    private class MyListener implements Component.ClickedListener {
        @Override
        public void onClick(Component component) {
            switch (component.getId()) {
                case ResourceTable.Id_back:
                    terminateAbility();
                    break;
                case ResourceTable.Id_map:
                    //跳转全景图
                    Intent intent1 = new Intent();
                    Operation operation1 = new Intent.OperationBuilder()
                            .withDeviceId("")
                            .withBundleName("com.example.timesequence")
                            .withAbilityName("com.example.timesequence.ability.dynasty.AllScreenImgAbility")
                            .build();
                    HiLog.info(SelectProblemSlice.LABEL_LOG, "跳转时的dynastyId：" + dynastyId);
                    intent1.setOperation(operation1);
                    startAbility(intent1);
                    break;
                case ResourceTable.Id_problem:
                    if (dynastyId == 11) {
                        //跳转答题页面
                        Intent intent2 = new Intent();
                        Operation operation2 = new Intent.OperationBuilder()
                                .withDeviceId("")
                                .withBundleName("com.example.timesequence")
                                .withAbilityName("com.example.timesequence.ability.problem.ProblemInfoAbility")
                                .build();
                        HiLog.info(SelectProblemSlice.LABEL_LOG, "跳转时的dynastyId：" + dynastyId);
                        intent2.setParam("dynastyId", dynastyId + "");
                        intent2.setParam("before", "types");
                        intent2.setOperation(operation2);
                        startAbility(intent2);
                    } else {
                        ToastUtil.showEncourageToast(getContext(), "此朝代暂时未设置题目，去其他朝代看看吧~");
                    }
                    break;
                case ResourceTable.Id_event:
                    if (dynastyId == 11) {
                        //跳转朝代事件页面
                        Intent intent = new Intent();
                        Operation operation = new Intent.OperationBuilder()
                                .withDeviceId("")
                                .withBundleName("com.example.timesequence")
                                .withAbilityName("com.example.timesequence.ability.dynasty.DetailsEventAbility")
                                .build();
                        intent.setParam("dynastyId", dynastyId);
                        intent.setParam("dynastyName", dynasty.getDynastyName());
                        intent.setOperation(operation);
                        startAbility(intent);
                    } else {
                        ToastUtil.showEncourageToast(getContext(), "此朝代还没有事件，去唐朝看看吧~");
                    }
                    break;
                case ResourceTable.Id_speech:

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
