package com.example.timestory.ability.user.slice;

import com.example.timestory.ResourceTable;
import com.example.timestory.Utils.HmOSImageLoader;
import com.example.timestory.constant.Constant;
import com.example.timestory.constant.ServiceConfig;
import com.example.timestory.entity.UserDetails;
import com.google.gson.Gson;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.*;
import ohos.agp.window.dialog.CommonDialog;
import ohos.agp.window.dialog.ToastDialog;
import ohos.eventhandler.EventHandler;
import ohos.eventhandler.EventRunner;
import ohos.eventhandler.InnerEvent;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import okhttp3.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;

/**
 * @author PengHuAnZhi
 * @createTime 2021/3/23 13:57
 * @projectName TimeStory
 * @className SettingAbilitySlice.java
 * @description TODO
 */
public class SettingAbilitySlice extends AbilitySlice implements Component.ClickedListener, RadioContainer.CheckedStateChangedListener {
    private Image mSettingUserHeader;
    private Text mSettingUserName;
    private Text mModifyName;
    private Text mSettingUserSignature;
    private Text mModifyUserSignature;
    private Text mModifyUserPhone;
    private Text mSettingRule;
    private Button mBtnSubmitQuestion;
    private Button mSettingExitBtn;
    private RadioContainer mRadioContainer;
    private HiLogLabel hiLogLabel = new HiLogLabel(1, 1, "phz");
    private EventRunner eventRunner = EventRunner.create(true);
    private EventHandler eventHandler = new EventHandler(eventRunner) {
        @Override
        protected void processEvent(InnerEvent event) {
            super.processEvent(event);
            if (event.eventId == 1) {
                getUITaskDispatcher().syncDispatch(() -> {
                    HiLog.info(hiLogLabel, Constant.UserDetails.toString());
                    //初始化头像
                    HmOSImageLoader.with(SettingAbilitySlice.this).load(ServiceConfig.SERVICE_ROOT + "/img/" + Constant.User.getUserHeader()).into(mSettingUserHeader);
                    //初始化昵称
                    mSettingUserName.setText(Constant.User.getUserNickname());
                    //初始化个性签名
                    mSettingUserSignature.setText(Constant.User.getUserSignature());
                    //初始化手机号
                    mModifyUserPhone.setText(Constant.User.getUserAccount());
                    //初始化性别
                    if (Constant.SEXARR[0].equals(Constant.UserDetails.getUserSex())) {
                        mRadioContainer.mark(0);
                    } else {
                        mRadioContainer.mark(1);
                    }
                    //初始化规则
                    mSettingRule.setText(Constant.Rule.getRuleInfo());
                });
            }
        }
    };

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_user_setting);
        initView();
        setClickListener();
        initDate();
    }

    private void initDate() {
        new Thread(() -> {
            try {
                URL url = new URL(ServiceConfig.SERVICE_ROOT + ServiceConfig.USERDETALURL + Constant.User.getUserId());
                URLConnection connection;
                connection = url.openConnection();
                InputStream in = connection.getInputStream();
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(in, StandardCharsets.UTF_8));
                String detail = reader.readLine();
                Constant.UserDetails = new Gson().fromJson(detail, UserDetails.class);
                getMainTaskDispatcher().syncDispatch(() -> new ToastDialog(SettingAbilitySlice.this)
                        .setText(Constant.UserDetails.toString())
                        .setDuration(4000)
                        .show());
                eventHandler.sendEvent(1);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void setClickListener() {
        mSettingUserHeader.setClickedListener(this);
        mModifyName.setClickedListener(this);
        mModifyUserSignature.setClickedListener(this);
        mBtnSubmitQuestion.setClickedListener(this);
        mSettingExitBtn.setClickedListener(this);
    }

    private void initView() {
        mSettingUserHeader = (Image) findComponentById(ResourceTable.Id_setting_user_header);
        mSettingUserName = (Text) findComponentById(ResourceTable.Id_setting_user_name);
        mModifyName = (Text) findComponentById(ResourceTable.Id_modify_name);
        mSettingUserSignature = (Text) findComponentById(ResourceTable.Id_setting_user_signature);
        mModifyUserSignature = (Text) findComponentById(ResourceTable.Id_modify_user_signature);
        mModifyUserPhone = (Text) findComponentById(ResourceTable.Id_modify_user_phone);
        mRadioContainer = (RadioContainer) findComponentById(ResourceTable.Id_radio_container);
        mSettingRule = (Text) findComponentById(ResourceTable.Id_setting_rule);
        mBtnSubmitQuestion = (Button) findComponentById(ResourceTable.Id_btn_submit_question);
        mSettingExitBtn = (Button) findComponentById(ResourceTable.Id_setting_exit_btn);
    }

    @Override
    public void onClick(Component component) {
        switch (component.getId()) {
            case ResourceTable.Id_setting_user_header:
                present(new PickHeaderAbilitySlice(), new Intent());
                break;
            case ResourceTable.Id_modify_name:
                CommonDialog nameCommonDialog = new CommonDialog(this);
                Component nameDialogComponent = showCommonDialog(nameCommonDialog);
                TextField nameTextField = (TextField) nameDialogComponent.findComponentById(ResourceTable.Id_tf_modify);
                Button nameSureBtn = (Button) nameDialogComponent.findComponentById(ResourceTable.Id_setting_dialog_sure);
                Button nameCancelBtn = (Button) nameDialogComponent.findComponentById(ResourceTable.Id_setting_dialog_cancel);
                nameCancelBtn.setClickedListener(cancelComponent -> nameCommonDialog.hide());
                nameSureBtn.setClickedListener(sureComponent -> sendModifyRequest(Constant.UserDetails.getUserId() + "", nameTextField.getText().trim(), Constant.UserDetails.getUserSignature(), Constant.UserDetails.getUserSex()));
                break;
            case ResourceTable.Id_modify_user_signature:
                CommonDialog signatureNameCommonDialog = new CommonDialog(this);
                Component signatureDialogComponent = showCommonDialog(signatureNameCommonDialog);
                TextField signatureTextField = (TextField) signatureDialogComponent.findComponentById(ResourceTable.Id_tf_modify);
                Button sureBtn = (Button) signatureDialogComponent.findComponentById(ResourceTable.Id_setting_dialog_sure);
                Button cancelBtn = (Button) signatureDialogComponent.findComponentById(ResourceTable.Id_setting_dialog_cancel);
                cancelBtn.setClickedListener(cancelComponent -> signatureNameCommonDialog.hide());
                sureBtn.setClickedListener(sureComponent -> sendModifyRequest(Constant.UserDetails.getUserId() + "", Constant.UserDetails.getUserNickname(), signatureTextField.getText().trim(), Constant.UserDetails.getUserSex()));
                break;
            case ResourceTable.Id_btn_submit_question:
                new ToastDialog(SettingAbilitySlice.this)
                        .setText("您的问题已提交，感谢您的支持")
                        .setDuration(4000)
                        .show();
                break;
            case ResourceTable.Id_setting_exit_btn:
                present(new LoginAbilitySlice(), new Intent());
                break;
            default:
                new ToastDialog(SettingAbilitySlice.this)
                        .setText("未知错误")
                        .setDuration(4000)
                        .show();
                break;
        }
    }

    private Component showCommonDialog(CommonDialog commonDialog) {
        Component dialogComponent = LayoutScatter.getInstance(this).parse(ResourceTable.Layout_dialog_setting, null, true);
        commonDialog.setSize(800, 600);
        commonDialog.setContentCustomComponent(dialogComponent);
        commonDialog.show();
        return dialogComponent;
    }

    private void sendModifyRequest(String userDetailsId, String userNickname, String userSignature, String userSex) {
        //实例化OkHttp
        OkHttpClient okHttpClient = new OkHttpClient();
        FormBody.Builder builder = new FormBody.Builder();
        //构建请求体
        FormBody formBody = builder.add("userDetailsId", userDetailsId)
                .add("userNickname", userNickname)
                .add("userSignature", userSignature)
                .add("userSex", userSex)
                .build();
        //构建请求对象
        Request request = new Request.Builder()
                .url(ServiceConfig.SERVICE_ROOT + "/userdetails/modify")
                .method("Post", formBody)
                .post(formBody)
                .build();
        //创建Call对象发送请求
        Call call = okHttpClient.newCall(request);
        //同步请求
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                getMainTaskDispatcher().syncDispatch(() -> {
                    new ToastDialog(SettingAbilitySlice.this)
                            .setText("请求失败")
                            .setDuration(4000)
                            .show();
                });
            }

            @Override
            public void onResponse(Call call, Response response) {
                getMainTaskDispatcher().syncDispatch(() -> new ToastDialog(SettingAbilitySlice.this)
                        .setText("修改成功")
                        .setDuration(4000)
                        .show());
            }
        });
    }

    @Override
    public void onCheckedChanged(RadioContainer radioContainer, int i) {
        sendModifyRequest(Constant.UserDetails.getUserId() + "", Constant.User.getUserNickname(), Constant.UserDetails.getUserSignature(), Constant.SEXARR[i]);
    }
}