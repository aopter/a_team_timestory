package com.example.timestory.ability.user.slice;

import com.example.timestory.ResourceTable;
import com.example.timestory.Utils.HmOSImageLoader;
import com.example.timestory.Utils.ToastUtil;
import com.example.timestory.constant.Constant;
import com.example.timestory.constant.ServiceConfig;
import com.example.timestory.entity.UserDetails;
import com.google.gson.Gson;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.*;
import ohos.agp.window.dialog.CommonDialog;
import ohos.eventhandler.EventHandler;
import ohos.eventhandler.EventRunner;
import ohos.eventhandler.InnerEvent;
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
    private DependentLayout mModifyName;
    private Text mSettingUserSignature;
    private DependentLayout mModifyUserSignature;
    private Text mModifyUserPhone;
    private Button mSettingExitBtn;
    private RadioContainer mRadioContainer;
    private EventRunner eventRunner = EventRunner.create(true);
    private EventHandler eventHandler = new EventHandler(eventRunner) {
        @Override
        protected void processEvent(InnerEvent event) {
            super.processEvent(event);
            if (event.eventId == 1) {
                getUITaskDispatcher().syncDispatch(() -> {
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
                });
            }
        }
    };

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_user_setting);
        initView();
        initDate();
        setClickListener();
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
        mSettingExitBtn.setClickedListener(this);
        mRadioContainer.setMarkChangedListener(this);
    }

    private void initView() {
        mSettingUserHeader = (Image) findComponentById(ResourceTable.Id_setting_user_header);
        mSettingUserName = (Text) findComponentById(ResourceTable.Id_setting_user_name);
        mModifyName = (DependentLayout) findComponentById(ResourceTable.Id_modify_name);
        mSettingUserSignature = (Text) findComponentById(ResourceTable.Id_setting_user_signature);
        mModifyUserSignature = (DependentLayout) findComponentById(ResourceTable.Id_modify_user_signature);
        mModifyUserPhone = (Text) findComponentById(ResourceTable.Id_modify_user_phone);
        mRadioContainer = (RadioContainer) findComponentById(ResourceTable.Id_radio_container);
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
                nameSureBtn.setClickedListener(sureComponent -> {
                    String newName = nameTextField.getText().trim();
                    sendModifyRequest(nameCommonDialog, Constant.UserDetails.getUserId() + "", newName, Constant.UserDetails.getUserSignature(), Constant.UserDetails.getUserSex());
                    mSettingUserName.setText(newName);
                });
                break;
            case ResourceTable.Id_modify_user_signature:
                CommonDialog signatureNameCommonDialog = new CommonDialog(this);
                Component signatureDialogComponent = showCommonDialog(signatureNameCommonDialog);
                TextField signatureTextField = (TextField) signatureDialogComponent.findComponentById(ResourceTable.Id_tf_modify);
                Button sureBtn = (Button) signatureDialogComponent.findComponentById(ResourceTable.Id_setting_dialog_sure);
                Button cancelBtn = (Button) signatureDialogComponent.findComponentById(ResourceTable.Id_setting_dialog_cancel);
                cancelBtn.setClickedListener(cancelComponent -> signatureNameCommonDialog.hide());
                sureBtn.setClickedListener(sureComponent -> {
                    String newSignature = signatureTextField.getText().trim();
                    sendModifyRequest(signatureNameCommonDialog, Constant.UserDetails.getUserId() + "", Constant.UserDetails.getUserNickname(), newSignature, Constant.UserDetails.getUserSex());
                    mSettingUserSignature.setText(newSignature);
                });
                break;
            case ResourceTable.Id_setting_exit_btn:
                present(new LoginAbilitySlice(), new Intent());
                break;
            default:
                getMainTaskDispatcher().syncDispatch(() -> ToastUtil.showCryToast(SettingAbilitySlice.this, "未知错误"));
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

    private void sendModifyRequest(CommonDialog commonDialog, String userDetailsId, String userNickname, String userSignature, String userSex) {
        OkHttpClient okHttpClient = new OkHttpClient();
        FormBody.Builder builder = new FormBody.Builder();
        FormBody formBody = builder.add("userId", userDetailsId)
                .add("userNickname", userNickname)
                .add("userSignature", userSignature)
                .add("userSex", userSex)
                .build();
        Request request = new Request.Builder()
                .url(ServiceConfig.SERVICE_ROOT + "/userdetails/modify")
                .method("Post", formBody)
                .post(formBody)
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                getMainTaskDispatcher().syncDispatch(() -> ToastUtil.showCryToast(SettingAbilitySlice.this, "请求失败"));
                if (commonDialog != null) {
                    commonDialog.hide();
                }
            }

            @Override
            public void onResponse(Call call, Response response) {
                if (commonDialog != null) {
                    getMainTaskDispatcher().syncDispatch(commonDialog::hide);
                }
                getMainTaskDispatcher().syncDispatch(() -> ToastUtil.showEncourageToast(SettingAbilitySlice.this, "修改成功"));
            }
        });
    }

    @Override
    public void onCheckedChanged(RadioContainer radioContainer, int i) {
        sendModifyRequest(null, Constant.UserDetails.getUserId() + "", Constant.User.getUserNickname(), Constant.UserDetails.getUserSignature(), Constant.SEXARR[i]);
    }
}