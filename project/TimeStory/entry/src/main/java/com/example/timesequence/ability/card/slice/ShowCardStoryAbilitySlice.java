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
    }

    @Override
    public boolean onStartContinuation() {
        return true;
    }

    @Override
    public boolean onSaveData(IntentParams intentParams) {
        // 开始保存slice数据
        intentParams.setParam("card", card);
        System.out.println("card" + intentParams.getParam("card"));
        return true;
    }

    @Override
    public boolean onRestoreData(IntentParams intentParams) {
        // 目标侧接收迁移数据
        card = (Card) intentParams.getParam("card");
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
                case ResourceTable.Id_btn_continue://流转
                    if (null == Constant.getAvailableDeviceIds()) {
                        ToastUtil.showSickToast(getApplicationContext(), "附近没有能够流转的设备，请确认是否有流转设备");
                    } else {
                        // 找到第一个流转的设备
                        continueAbility(Constant.getAvailableDeviceIds().get(0));
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