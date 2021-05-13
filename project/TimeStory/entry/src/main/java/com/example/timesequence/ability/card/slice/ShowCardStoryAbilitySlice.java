package com.example.timesequence.ability.card.slice;

import com.example.timesequence.ResourceTable;
import com.example.timesequence.Utils.HmOSImageLoader;
import com.example.timesequence.Utils.ToastUtil;
import com.example.timesequence.constant.Constant;
import com.example.timesequence.constant.ServiceConfig;
import com.example.timesequence.entity.card.Card;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.ability.IAbilityContinuation;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.IntentParams;
import ohos.agp.components.*;
import ohos.hiviewdfx.HiLog;

import java.util.ArrayList;
import java.util.List;


public class ShowCardStoryAbilitySlice extends AbilitySlice implements IAbilityContinuation {
    private Card card;
    private List<String> event = new ArrayList<>();
    private int currentStory = 0;
    private Image back;
    private DirectionalLayout formerStory;
    private Text story;
    private DirectionalLayout nextStory;
    private Image cardImg;
    private Text tip;
    private Button btn_continue;
    private long clickMillis = 0;
    private long clickTwiceMillis;
    private AbilitySlice abilitySlice = this;
    private boolean flag = false; // 是否正在迁移
    private String isMoved = "false"; // 是否迁移

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_show_card_story);
        clickMillis = System.currentTimeMillis();
        if (card == null) {
            card = intent.getSerializableParam("card");
        }
        initComponent();
        setListener();
        for (String e : card.getCardStory().split(Constant.DELIMITER)) {
            event.add(e);
        }
        System.out.println("____story____" + event);
        HmOSImageLoader.with(abilitySlice)
                .load(ServiceConfig.SERVICE_ROOT + "/img/" + card.getCardPicture())
                .into(cardImg);
        defineViewAndAnimation();
    }

    private void defineViewAndAnimation() {
        tip.setText("'" + card.getCardName() + "'的那些事");
        story.setText("    " + event.get(currentStory));
        System.out.println("____story____currentStory:" + currentStory + ", story:" + event.get(currentStory));

    }

    private void setListener() {
        MyListener myListener = new MyListener();
        back.setClickedListener(myListener);
        formerStory.setClickedListener(myListener);
        nextStory.setClickedListener(myListener);
        btn_continue.setClickedListener(myListener);
    }

    private void initComponent() {
        back = (Image) findComponentById(ResourceTable.Id_back);
        formerStory = (DirectionalLayout) findComponentById(ResourceTable.Id_former_story);
        story = (Text) findComponentById(ResourceTable.Id_story);
        nextStory = (DirectionalLayout) findComponentById(ResourceTable.Id_next_story);
        cardImg = (Image) findComponentById(ResourceTable.Id_card_img);
        tip = (Text) findComponentById(ResourceTable.Id_role_story);
        btn_continue = (Button) findComponentById(ResourceTable.Id_btn_continue);
        if (isMoved.contains("true")) {
            btn_continue.setVisibility(Component.HIDE);
        }
    }

    @Override
    public boolean onStartContinuation() {
        return true;
    }

    @Override
    public boolean onSaveData(IntentParams intentParams) {
        // 开始保存slice数据
        intentParams.setParam("card", card);
        intentParams.setParam("isMoved", "true");
        System.out.println("card" + intentParams.getParam("card"));
        return true;
    }

    @Override
    public boolean onRestoreData(IntentParams intentParams) {
        // 目标侧接收迁移数据
        card = (Card) intentParams.getParam("card");
        isMoved = intentParams.getParam("isMoved").toString();
        System.out.println("card" + card);
        return true;
    }

    @Override
    public void onCompleteContinuation(int i) {

    }

    class MyListener implements Component.ClickedListener {

        @Override
        public void onClick(Component component) {
            switch (component.getId()) {
                case ResourceTable.Id_btn_continue://迁移与回迁控制
                    //可以让用户弹窗选择 设备 这里直接用的第一个设备
                    //在两个搭载鸿蒙操作系统的手机上均安装这个程序，并在其中一个设备上打开的该应用程序：按钮
                    // 可以实现应用程序在两个设备间的流转了。
//                不过，这两个设备需要在同一个WiFi下，并且登录同一个华为账号，才可以使用分布式软总线实现流转。
//                    if (!flag) {  // 未迁移
//                        // 通过FLAG_GET_ONLINE_DEVICE标记获得在线设备列表
//                        List<DeviceInfo> deviceInfoList = DeviceManager.getDeviceList(DeviceInfo.FLAG_GET_ONLINE_DEVICE);
//                        if (deviceInfoList.size() < 1) {
//                            ToastUtil.showSickToast(getApplicationContext(), "附近没有能够流转的设备，请确认是否有流转设备");
//                        } else {
//                            DeviceSelectDialog dialog = new DeviceSelectDialog(getApplicationContext());
//                            // 点击后迁移到指定设备
//                            dialog.setListener(
//                                    deviceInfo -> {
//                                        try {
//                                            // 迁移
//                                            String deviceId = deviceInfo.getDeviceId();
//                                            continueAbilityReversibly(deviceId);
//                                            flag = true;
//                                            ToastUtil.showEncourageToast(getApplicationContext(), "迁移成功~");
//                                            btn_continue.setText("回迁");
//                                        } catch (IllegalStateException | UnsupportedOperationException e) {
//                                            ToastUtil.showSickToast(getApplicationContext(), "迁移失败~");
//                                        }
//                                        dialog.hide();
//                                    });
//                            dialog.show();
//                        }
//                    } else {
//                        reverseContinueAbility();
//                        flag = false;
//                        ToastUtil.showEncourageToast(getApplicationContext(), "回迁成功~");
//                        btn_continue.setText("迁移");
//                    }
                    if (!flag) {
                        if (null == Constant.getAvailableDeviceIds()) {
                            ToastUtil.showSickToast(getApplicationContext(), "附近没有能够流转的设备，请确认是否有流转设备");
                        } else {
                            // 找到第一个流转的设备
                            continueAbilityReversibly(Constant.getAvailableDeviceIds().get(0));
                            ToastUtil.showEncourageToast(getApplicationContext(), "迁移成功~");
                            flag = true;
                            btn_continue.setText("回迁");
                        }
                    } else {
                        reverseContinueAbility();
                        flag = false;
                        ToastUtil.showEncourageToast(getApplicationContext(), "回迁成功,再次迁移请重新进入此页~");
                        btn_continue.setText("迁移");
                    }
                    break;
                case ResourceTable.Id_back:
                    terminate();
                    break;
                case ResourceTable.Id_former_story:
                    clickTwiceMillis = System.currentTimeMillis();
                    if ((clickTwiceMillis - clickMillis) < 250) {
                        if (currentStory > 0) {
                            ToastUtil.showCryToast(abilitySlice, "跟不上你的脚步了，点击慢一点吧");
                        }
                    } else {
                        clickMillis = clickTwiceMillis;
                        currentStory = currentStory - 1;
                        if (currentStory < 0) {
                            ToastUtil.showSickToast(abilitySlice, "不能再回头啦");
                            currentStory = 0;
                        } else {
                            story.setText(event.get(currentStory));
                            System.out.println("____story____currentStory:" + currentStory + ", story:" + event.get(currentStory));
                        }
                    }
                    break;
                case ResourceTable.Id_next_story:
                    clickTwiceMillis = System.currentTimeMillis();
                    if ((clickTwiceMillis - clickMillis) < 250) {
                        if (currentStory < event.size() - 1) {
                            ToastUtil.showCryToast(abilitySlice, "跟不上你的脚步了，点击慢一点吧");
                        }
                    } else {
                        clickMillis = clickTwiceMillis;
                        currentStory = currentStory + 1;
                        if (currentStory >= event.size()) {
                            currentStory = event.size() - 1;
                            ToastUtil.showSickToast(abilitySlice, "没有更多了");
                        } else {
                            story.setText(event.get(currentStory));
                            System.out.println("____story____currentStory:" + currentStory + ", story:" + event.get(currentStory));
                        }
                    }
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
