package com.example.timestory.ability.card.slice;

import com.example.timestory.ResourceTable;
import com.example.timestory.Utils.HmOSImageLoader;
import com.example.timestory.Utils.ToastUtil;
import com.example.timestory.constant.Constant;
import com.example.timestory.constant.ServiceConfig;
import com.example.timestory.entity.card.Card;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.animation.AnimatorProperty;
import ohos.agp.components.Component;
import ohos.agp.components.DirectionalLayout;
import ohos.agp.components.Image;
import ohos.agp.components.Text;

import java.util.ArrayList;
import java.util.List;


public class ShowCardStoryAbilitySlice extends AbilitySlice {
    private Card card;
    private List<String> event = new ArrayList<>();
    private int currentStory = 0;
    private Image back;
    private DirectionalLayout formerStory;
    private Text story;
    private DirectionalLayout nextStory;
    private Image cardImg;
    private Text tip;
    private long clickMillis = 0;
    private long clickTwiceMillis;
    private AbilitySlice abilitySlice = this;

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_show_card_story);
        clickMillis = System.currentTimeMillis();
        card = intent.getSerializableParam("card");
        initComponent();
        setListener();
        System.out.println("____story____" + card.toString());
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
    }

    private void initComponent() {
        back = (Image) findComponentById(ResourceTable.Id_back);
        formerStory = (DirectionalLayout) findComponentById(ResourceTable.Id_former_story);
        story = (Text) findComponentById(ResourceTable.Id_story);
        nextStory = (DirectionalLayout) findComponentById(ResourceTable.Id_next_story);
        cardImg = (Image) findComponentById(ResourceTable.Id_card_img);
        tip = (Text) findComponentById(ResourceTable.Id_role_story);
    }

    class MyListener implements Component.ClickedListener {

        @Override
        public void onClick(Component component) {
            switch (component.getId()) {
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
