package com.example.timestory.ability.user.slice;

import com.example.timestory.ResourceTable;
import com.example.timestory.Utils.ToastUtil;
import com.example.timestory.constant.Constant;
import com.example.timestory.constant.ServiceConfig;
import com.google.gson.JsonParser;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Button;
import ohos.agp.components.TextField;
import ohos.agp.window.dialog.ToastDialog;
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
    private TextField phoneTx;
    private TextField passwordTx;
    private Button registryBtn;

    @Override
    protected void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_user_registry);
        initView();
        setClickListener();
    }

    /**
     * @return void
     * @date 2021/3/25 15:34
     * @description 绑定监听器
     */
    private void setClickListener() {
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
        if (phoneNum == null || password == null || phoneNum.length() != Constant.PHONE_LENGTH || "".equals(password)) {
            ToastUtil.showCryToast(RegistryAbilitySlice.this, "手机号格式不正确或密码为空");
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
                getMainTaskDispatcher().syncDispatch(() -> ToastUtil.showCryToast(RegistryAbilitySlice.this, "请求失败，请检查网络连接是否正常"));
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                assert response.body() != null;
                boolean result = new JsonParser().parse(response.body().string()).getAsJsonObject().get("result").getAsBoolean();
                if (response.body() != null && result) {
                    //注册成功
                    getMainTaskDispatcher().syncDispatch(() ->
                            new ToastDialog(RegistryAbilitySlice.this)
                                    .setText("注册成功")
                                    .setDuration(4000)
                                    .show());
                    getMainTaskDispatcher().syncDispatch(() -> ToastUtil.showEncourageToast(RegistryAbilitySlice.this, "注册成功"));
                    //无参跳转
                    Intent intent = new Intent();
                    intent.setParam("phoneNum", phoneNum);
                    intent.setParam("password", password);
                    intent.setFlags(Intent.FLAG_ABILITY_CLEAR_MISSION | Intent.FLAG_ABILITY_NEW_MISSION);
                    present(new LoginAbilitySlice(), intent);
                    RegistryAbilitySlice.this.terminate();
                } else {
                    //注册失败
                    getMainTaskDispatcher().syncDispatch(() -> ToastUtil.showCryToast(RegistryAbilitySlice.this, "注册失败"));
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
        phoneTx = (TextField) findComponentById(ResourceTable.Id_registry_phone);
        passwordTx = (TextField) findComponentById(ResourceTable.Id_registry_password);
        registryBtn = (Button) findComponentById(ResourceTable.Id_btn_registry);
    }
}