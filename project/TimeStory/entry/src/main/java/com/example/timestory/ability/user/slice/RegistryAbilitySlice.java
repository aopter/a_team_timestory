package com.example.timestory.ability.user.slice;

import com.example.timestory.ResourceTable;
import com.example.timestory.constant.ServiceConfig;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Button;
import ohos.agp.components.Text;
import ohos.agp.window.dialog.ToastDialog;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import okhttp3.*;

import java.io.IOException;

/**
 * @author PengHuAnZhi
 * @createTime 2021/3/25 14:51
 * @projectName TimeStory
 * @className RegistryAbilitySlice.java
 * @description 注册页面
 */
public class RegistryAbilitySlice extends AbilitySlice {
    private Text phoneTx;
    private Text passwordTx;
    private Button registryBtn;
    private static HiLogLabel label = new HiLogLabel(3, 1, "RegistryAbilitySlice");

    @Override
    protected void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_user_registry);
        initView();
        setListener();
    }

    /**
     * @return void
     * @date 2021/3/25 15:34
     * @description 绑定监听器
     */
    private void setListener() {
        registryBtn.setClickedListener(component -> {
            String phoneNum = phoneTx.getText().trim();
            String password = passwordTx.getText().trim();
            registry(phoneNum, password);
        });
    }

    /**
     * @param phoneNum 注册手机号
     * @param password 注册密码
     * @return void
     * @date 2021/3/25 15:35
     * @description 根据手机号密码进行注册操作
     */
    private void registry(String phoneNum, String password) {
        if (phoneNum == null || password == null || phoneNum.length() != ServiceConfig.PHONE_LENGTH || "".equals(password)) {
            new ToastDialog(RegistryAbilitySlice.this)
                    .setText("手机号格式不正确或密码为空")
                    .setDuration(4000)
                    .show();
            return;
        }
        //实例化OkHttp
        OkHttpClient okHttpClient = new OkHttpClient();
        FormBody.Builder builder = new FormBody.Builder();
        //构建请求体
        FormBody formBody = builder.add("userAccount", phoneNum)
                .add("userPassword", password)
                .add("flag", 1 + "")
                .build();
        //构建请求对象
        Request request = new Request.Builder()
                .url(ServiceConfig.SERVICE_ROOT + ServiceConfig.REGISTRY_URL)
                .method("Post", formBody)
                .post(formBody)
                .build();
        //创建Call对象发送请求
        Call call = okHttpClient.newCall(request);
        //异步请求
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //ToastDialog只能在主线程执行，所以获取主线程供子线程中创建ToastDialog，下同
                getMainTaskDispatcher().syncDispatch(() ->
                        new ToastDialog(RegistryAbilitySlice.this)
                                .setText("请求失败，请检查网络连接是否正常")
                                .setDuration(4000)
                                .show());
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                boolean result = new JsonParser().parse(response.body().string()).getAsJsonObject().get("result").getAsBoolean();
                if (response.body() != null && result) {
                    //注册成功
                    getMainTaskDispatcher().syncDispatch(() ->
                            new ToastDialog(RegistryAbilitySlice.this)
                                    .setText("注册成功")
                                    .setDuration(4000)
                                    .show());
                    //无参跳转
                    present(new LoginAbilitySlice(), new Intent());
                } else {
                    //注册失败
                    getMainTaskDispatcher().syncDispatch(() ->
                            new ToastDialog(RegistryAbilitySlice.this)
                                    .setText("注册失败，该用户已存在")
                                    .setDuration(4000)
                                    .show());
                }
            }
        });
    }

    /**
     * @return void
     * @date 2021/3/25 15:34
     * @description 初始化控件以及获取UI线程
     */
    private void initView() {
        phoneTx = (Text) findComponentById(ResourceTable.Id_registry_phone);
        passwordTx = (Text) findComponentById(ResourceTable.Id_registry_password);
        registryBtn = (Button) findComponentById(ResourceTable.Id_btn_registry);
    }
}