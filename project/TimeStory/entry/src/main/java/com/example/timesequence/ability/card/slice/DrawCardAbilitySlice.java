package com.example.timesequence.ability.card.slice;

import com.example.timesequence.ResourceTable;
import com.example.timesequence.Utils.HmOSImageLoader;
import com.example.timesequence.constant.Constant;
import com.example.timesequence.constant.ServiceConfig;
import com.example.timesequence.entity.card.Card;
import com.example.timesequence.entity.card.Icon;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.Operation;
import ohos.agp.animation.Animator;
import ohos.agp.animation.AnimatorGroup;
import ohos.agp.animation.AnimatorProperty;
import ohos.agp.components.*;
import ohos.agp.components.element.PixelMapElement;
import ohos.eventhandler.EventHandler;
import ohos.eventhandler.EventRunner;
import ohos.eventhandler.InnerEvent;
import ohos.global.resource.NotExistException;
import okhttp3.*;

import java.io.IOException;
import java.util.List;


public class DrawCardAbilitySlice extends AbilitySlice {
    private AbilitySlice abilitySlice = this;
    private Image back;
    private Image card1;
    private Image card2;
    private Image card3;
    private Image card4;
    private Button toLastView;
    private DependentLayout cardContainer;
    private DirectionalLayout frontContainer;
    private Image drawCard;
    private Text tip;
    private Text text;
    private Image btnShare;
    private Image ERCode;
    private Text join;
    private List<Icon> icons;
    // 抽卡所消耗积分
    private int descCount = 60;
    // 是否已点击
    private boolean flag = false;

    private boolean isFlag = false;
    // 获取卡片
    private Card card;
    // 属性动画
    private AnimatorProperty cardAnimation;
    private OkHttpClient client;
    private AnimatorGroup group;
    private Gson gson;
    // 是否在分享状态
    private boolean isShareing = false;
    private EventRunner eventRunner = EventRunner.create(true);//线程投递器
    private EventHandler eventHandler = new EventHandler(eventRunner) {
        @Override
        protected void processEvent(InnerEvent event) {
            super.processEvent(event);
            switch (event.eventId) {
                case 1:
                    String result = (String) event.object;
                    card = gson.fromJson(result, Card.class);
                    System.out.println("____draw____" + card.toString());
                    break;
            }
        }
    };

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_draw_card);
        initComponent();
        setListener();
    }
    private void setListener() {
        MyListener myListener = new MyListener();
        drawCard.setClickedListener(myListener);
        card1.setClickedListener(myListener);
        card2.setClickedListener(myListener);
        card3.setClickedListener(myListener);
        card4.setClickedListener(myListener);
        back.setClickedListener(myListener);
        toLastView.setClickedListener(myListener);
        btnShare.setClickedListener(myListener);
        cardContainer.setClickedListener(myListener);
        frontContainer.setClickedListener(myListener);
    }

    private void initComponent() {
        back = (Image) findComponentById(ResourceTable.Id_back);
        card1 = (Image) findComponentById(ResourceTable.Id_card1);
        card2 = (Image) findComponentById(ResourceTable.Id_card2);
        card3 = (Image) findComponentById(ResourceTable.Id_card3);
        card4 = (Image) findComponentById(ResourceTable.Id_card4);
        toLastView = (Button) findComponentById(ResourceTable.Id_to_last_view);
        cardContainer = (DependentLayout) findComponentById(ResourceTable.Id_card_container);
        frontContainer = (DirectionalLayout) findComponentById(ResourceTable.Id_front_container);
        drawCard = (Image) findComponentById(ResourceTable.Id_draw_card_show);
        tip = (Text) findComponentById(ResourceTable.Id_tip);
        text = (Text) findComponentById(ResourceTable.Id_text);
        btnShare = (Image) findComponentById(ResourceTable.Id_share);
        ERCode = (Image) findComponentById(ResourceTable.Id_e_r_code);
        join = (Text) findComponentById(ResourceTable.Id_join);
        back.setVisibility(Component.VISIBLE);
        client = new OkHttpClient();
        gson = new GsonBuilder()
                .serializeNulls()
                .create();
        // 定义动画
        group = new AnimatorGroup();
        cardAnimation = drawCard.createAnimatorProperty();
        cardAnimation.scaleYBy(1).scaleY(0).scaleXBy(1).scaleX(0);
        cardAnimation.setStateChangedListener(new Animator.StateChangedListener() {
            @Override
            public void onStart(Animator animator) {

            }

            @Override
            public void onStop(Animator animator) {

            }

            @Override
            public void onCancel(Animator animator) {

            }

            @Override
            public void onEnd(Animator animator) {
                try {
                    drawCard.setBackground(new PixelMapElement(getResourceManager().getResource(ResourceTable.Media_bg_card_img)));
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (NotExistException e) {
                    e.printStackTrace();
                }
                tip.setText(" (≧∇≦)ﾉ 手气真棒，恭喜你获得了‘" + card.getCardName() + "’卡片！");
                HmOSImageLoader.with(abilitySlice).load(ServiceConfig.SERVICE_ROOT + "/img/" + card.getCardPicture())
                        .into(drawCard);
                AnimatorProperty property = drawCard.createAnimatorProperty().scaleXBy(0).scaleX(1).scaleYBy(0).scaleY(1).setDuration(1000);
                property.start();
                System.out.println("____draw____" + cardAnimation);
            }

            @Override
            public void onPause(Animator animator) {

            }

            @Override
            public void onResume(Animator animator) {

            }
        });
    }

    class MyListener implements Component.ClickedListener {

        @Override
        public void onClick(Component component) {
            switch (component.getId()) {
                case ResourceTable.Id_draw_card_show:
                    // 不是在分享状态下
                    if (!isShareing) {
                        if (!flag) {
                            // 如果未点击
                            flag = true;
                            toLastView.setVisibility(Component.VISIBLE);
                            cardAnimation.start();
                        } else {
                            // 已经点击，再次点击进入卡片界面
                            Intent intent = new Intent();
                            Operation operation = new Intent.OperationBuilder()
                                    .withDeviceId("")
                                    .withBundleName("com.example.timesequence")
                                    .withAbilityName("com.example.timesequence.ability.card.SpecificCardDetailAbility")
                                    .build();
                            intent.setParam("cardId", card.getCardId());
                            intent.setOperation(operation);
                            startAbility(intent, 0);
                        }
                    } else {
                        // 在分享状态下
                        toLastView.setVisibility(Component.VISIBLE);
                        join.setVisibility(Component.INVISIBLE);
                        ERCode.setVisibility(Component.INVISIBLE);
                        isShareing = false;
                    }
                    break;
                case ResourceTable.Id_card1:
                case ResourceTable.Id_card2:
                case ResourceTable.Id_card3:
                case ResourceTable.Id_card4:
                    System.out.println("____draw____选择了卡片");
                    if (!isFlag) {
                        getDrawCard();
                        Constant.User.setUserCount(Constant.User.getUserCount() - 60);
                        AnimatorProperty alphaAnim1 = frontContainer.createAnimatorProperty().alpha(0).setDuration(500);
                        alphaAnim1.setStateChangedListener(new Animator.StateChangedListener() {
                            @Override
                            public void onStart(Animator animator) {

                            }

                            @Override
                            public void onStop(Animator animator) {

                            }

                            @Override
                            public void onCancel(Animator animator) {

                            }

                            @Override
                            public void onEnd(Animator animator) {
                                frontContainer.setAlpha(0);
                                frontContainer.setVisibility(Component.HIDE);
                                AnimatorProperty property = cardContainer.createAnimatorProperty().alpha(0.7f).setDuration(1000);
                                AnimatorProperty toLast = toLastView.createAnimatorProperty().alpha(0.9f).setDuration(1000);
                                AnimatorProperty draw = drawCard.createAnimatorProperty().alpha(0.9f).setDuration(1000);
                                AnimatorProperty t = tip.createAnimatorProperty().alpha(0.9f).setDuration(1000);
                                group.runParallel(property, toLast, draw, t);
                                group.start();
                            }

                            @Override
                            public void onPause(Animator animator) {

                            }

                            @Override
                            public void onResume(Animator animator) {

                            }
                        });
                        alphaAnim1.start();
                        isFlag = true;
                    }
                    break;
                case ResourceTable.Id_back:
                case ResourceTable.Id_to_last_view:
                    terminate();
                    break;
                case ResourceTable.Id_share:
                    break;
                case ResourceTable.Id_card_container:
                    System.out.println("____draw____Id_card_container");
                    if (isFlag) {
                        toLastView.setVisibility(Component.VISIBLE);
                        join.setVisibility(Component.INVISIBLE);
                        ERCode.setVisibility(Component.INVISIBLE);
                        isShareing = false;
                    }
                    break;
                case ResourceTable.Id_front_container:
                    System.out.println("____draw____Id_front_container");
                    break;
            }
        }
    }

    //网络获取朝代全部的事件
    public void getDrawCard() {
        new Thread() {
            @Override
            public void run() {
                Request request = new Request.Builder()
                        .url(ServiceConfig.SERVICE_ROOT + "/card/draw/" + Constant.User.getUserId())
                        .build();
                System.out.println("____draw____" + ServiceConfig.SERVICE_ROOT + "/card/draw/" + 6);
                Call call = client.newCall(request);

                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        System.out.println("____draw____错误");
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String result = response.body().string();
                        System.out.println("____draw____" + result);
                        InnerEvent event = InnerEvent.get(1, result);
                        eventHandler.sendEvent(event);
                    }
                });
            }
        }.start();
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
