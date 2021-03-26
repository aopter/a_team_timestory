package com.example.timestory.ability.user.slice;

import com.example.timestory.ResourceTable;
import com.example.timestory.ability.dynasty.slice.HomePageAbilitySlice;
import com.example.timestory.constant.Constant;
import com.example.timestory.constant.ServiceConfig;
import com.example.timestory.entity.User;
import com.google.gson.Gson;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Button;
import ohos.agp.components.RadioButton;
import ohos.agp.components.TextField;
import ohos.agp.window.dialog.ToastDialog;
import ohos.data.DatabaseHelper;
import ohos.data.preferences.Preferences;
import okhttp3.*;

import java.io.IOException;

/**
 * @author PengHuAnZhi
 * @createTime 2021/3/25 18:01:50
 * @projectName TimeStory
 * @className LoginAbilitySlice.java
 * @description 登录页面
 */
public class LoginAbilitySlice extends AbilitySlice {
    private RadioButton saveUserRb;
    private TextField phoneNumTf;
    private TextField passwordTf;
    private Button loginBtn;
    private Button registryBtn;
    private String phoneNum;
    private String password;
    private DatabaseHelper databaseHelper;

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_login);
        getPermission();
        initView();
        setClickListener();
        initTextField(intent);
    }

    private void getPermission() {
        String[] permission = {"ohos.permission.READ_USER_STORAGE", "ohos.permission.INTERNET","ohos.permission.WRITE_MEDIA"};
        requestPermissionsFromUser(permission, 0);
    }

    /**
     * @param intent
     * @return void
     * @date 2021/3/26 7:57
     * @description 判断intent中是否包含账号密码参数，如果有，就应该是注册成功后跳转回来的界面，自动的将注册的账号密码填充到文本域中
     */
    private void initTextField(Intent intent) {
        phoneNum = intent.getStringParam("phoneNum");
        password = intent.getStringParam("password");
        //如果intent没有值，则证明不是注册页面跳转来的
        if (phoneNum != null && password != null) {
            phoneNumTf.setText(phoneNum);
            passwordTf.setText(password);
        } else {
            //执行常规逻辑，判断用户是否记住密码
            judgeSaveUser();
        }
    }

    /**
     * @return void
     * @date 2021/3/25 20:54
     * @description 判断用户是否保存了密码
     */
    private void judgeSaveUser() {
        Preferences preferences = databaseHelper.getPreferences("userInfo");
        boolean saveStatus = preferences.getBoolean("saveUserInfo", false);
        if (saveStatus) {
            phoneNum = preferences.getString("phoneNum", "");
            password = preferences.getString("password", "");
            phoneNumTf.setText(phoneNum);
            passwordTf.setText(password);
            saveUserRb.setChecked(true);
        }
    }


    /**
     * @return void
     * @date 2021/3/25 18:02
     * @description 初始化监听器
     */
    private void setClickListener() {
        loginBtn.setClickedListener(component -> {
            phoneNum = phoneNumTf.getText().trim();
            password = passwordTf.getText().trim();
            login(phoneNum, password);
        });
        registryBtn.setClickedListener((component) -> {
            present(new RegistryAbilitySlice(), new Intent());
        });
    }

    /**
     * @param phoneNum 登录账号
     * @param password 登录密码
     * @return void
     * @date 2021/3/25 18:02
     * @description 处理登录请求
     */
    private void login(String phoneNum, String password) {
        if (phoneNum == null || password == null || phoneNum.length() != Constant.PHONE_LENGTH || "".equals(password)) {
            new ToastDialog(LoginAbilitySlice.this)
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
                .url(ServiceConfig.SERVICE_ROOT + ServiceConfig.LOGIN_URL)
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
                        new ToastDialog(LoginAbilitySlice.this)
                                .setText("请求失败，请检查网络连接是否正常")
                                .setDuration(4000)
                                .show());
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.body() != null) {
                    String result = response.body().string();
                    if (result.contains("false")) {
                        //登录失败
                        getMainTaskDispatcher().syncDispatch(() ->
                                new ToastDialog(LoginAbilitySlice.this)
                                        .setText("登录失败，请检查用户名和密码是否正确")
                                        .setDuration(4000)
                                        .show());
                    } else {
                        if (saveUserRb.isChecked()) {
                            saveUser();
                        } else {
                            deleteUser();
                        }
                        //登录成功
                        getMainTaskDispatcher().syncDispatch(() ->
                                new ToastDialog(LoginAbilitySlice.this)
                                        .setText("登陆成功")
                                        .setDuration(4000)
                                        .show());
                        //处理返回Json串
                        Constant.User = new Gson().fromJson(result, User.class);
                        Constant.User.setUserAccount(phoneNum);
                        //跳转
                        Intent intent = new Intent();
                        intent.setFlags(Intent.FLAG_ABILITY_CLEAR_MISSION | Intent.FLAG_ABILITY_NEW_MISSION);
                        present(new HomePageAbilitySlice(), intent);
                        LoginAbilitySlice.this.terminate();
                    }
                } else {
                    getMainTaskDispatcher().syncDispatch(() ->
                            new ToastDialog(LoginAbilitySlice.this)
                                    .setText("收到响应异常，请稍后再试")
                                    .setDuration(4000)
                                    .show());
                }
            }
        });
    }

    /**
     * @return void
     * @date 2021/3/25 20:14
     * @description 删除登录信息
     */

    private void deleteUser() {
        // 默认存储目录可以通过context.getPreferencesDir()获取。
        Preferences preferences = databaseHelper.getPreferences("userInfo");
        preferences.clear();
        preferences.flushSync();
    }

    /**
     * @return void
     * @date 2021/3/25 20:15
     * @description 保存登录信息
     */
    private void saveUser() {
        // 默认存储目录可以通过context.getPreferencesDir()获取。
        Preferences preferences = databaseHelper.getPreferences("userInfo");
        preferences.putString("phoneNum", phoneNum);
        preferences.putString("password", password);
        preferences.putBoolean("saveUserInfo", true);
        preferences.flushSync();
    }

    /**
     * @return void
     * @date 2021/3/25 18:02
     * @description 初始化视图以及微型数据库
     */
    private void initView() {
        saveUserRb = (RadioButton) findComponentById(ResourceTable.Id_save_user);
        phoneNumTf = (TextField) findComponentById(ResourceTable.Id_login_phone);
        passwordTf = (TextField) findComponentById(ResourceTable.Id_login_password);
        loginBtn = (Button) findComponentById(ResourceTable.Id_login_btn_login);
        registryBtn = (Button) findComponentById(ResourceTable.Id_login_btn_registry);
        databaseHelper = new DatabaseHelper(this);
    }
}