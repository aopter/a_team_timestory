package com.example.timestory.ability.card.slice;

import com.example.timestory.ResourceTable;
import com.example.timestory.Utils.HmOSImageLoader;
import com.example.timestory.Utils.Util;
import com.example.timestory.ability.card.ShowCardStoryAbility;
import com.example.timestory.constant.ServiceConfig;
import com.example.timestory.entity.card.Card;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.Operation;
import ohos.agp.components.Button;
import ohos.agp.components.Component;
import ohos.agp.components.Image;
import ohos.agp.components.Text;
import ohos.agp.window.dialog.ToastDialog;
import ohos.eventhandler.EventHandler;
import ohos.eventhandler.EventRunner;
import ohos.eventhandler.InnerEvent;
import okhttp3.*;

import java.io.IOException;


public class SpecificCardDetailAbilitySlice extends AbilitySlice {
    private Card card;
    private Image cardPic;
    private Button cardName;
    private Text cardInfo;
    private Button cardStory;
    private Image back;
    private Gson gson;
    private int cardId;
    private OkHttpClient client;
    private AbilitySlice abilitySlice = this;
    private EventRunner eventRunner = EventRunner.create(true);//线程投递器
    private EventHandler eventHandler = new EventHandler(eventRunner) {
        @Override
        protected void processEvent(InnerEvent event) {
            super.processEvent(event);
            switch (event.eventId) {
                case 1:
                    String result = (String) event.object;
                    card = gson.fromJson(result, Card.class);
                    System.out.println("____cdetail____" + card.toString());
                    showDatas();
                    break;
            }
        }
    };


    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_specific_card_detail);
        initComponent();
        card = intent.getSerializableParam("card");
        if (card == null) {
            cardId = intent.getIntParam("cardId", -1);
            if (cardId == -1) {
                new ToastDialog(getContext()).setText("获取卡片详情出错了").show();
                cardName.setVisibility(Component.HIDE);
                cardStory.setVisibility(Component.HIDE);
            } else {
                cardName.setVisibility(Component.VISIBLE);
                getCardData();
            }
        } else {
            showDatas();
        }
        setListener();
    }

    private void showDatas() {
        cardInfo.setText(card.getCardInfo());
        cardName.setText(card.getCardName());
        HmOSImageLoader.with(abilitySlice)
                .load(ServiceConfig.SERVICE_ROOT + "/img/" + card.getCardPicture())
                .into(cardPic);
        if (card.getCardType() == 1) {
            cardStory.setVisibility(Component.VISIBLE);
        } else {
            cardStory.setVisibility(Component.HIDE);
        }
    }

    private void getCardData() {
        new Thread() {
            @Override
            public void run() {
                Request request = new Request.Builder()
                        .url(ServiceConfig.SERVICE_ROOT + "/card/details/" + cardId)
                        .build();
                Call call = client.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        // TODO获取卡片内容失败
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String result = response.body().string();
                        InnerEvent event = InnerEvent.get(1, result);
                        eventHandler.sendEvent(event);
                    }
                });
            }
        }.start();
    }

    private void setListener() {
        MyListener myListener = new MyListener();
        back.setClickedListener(myListener);
        cardStory.setClickedListener(myListener);
    }

    private void initComponent() {
        back = (Image) findComponentById(ResourceTable.Id_back);
        cardPic = (Image) findComponentById(ResourceTable.Id_card_img);
        cardName = (Button) findComponentById(ResourceTable.Id_card_name);
        cardInfo = (Text) findComponentById(ResourceTable.Id_card_info);
        cardStory = (Button) findComponentById(ResourceTable.Id_card_story);
        client = new OkHttpClient();
        gson = new GsonBuilder()
                .serializeNulls()
                .create();
    }

    class MyListener implements Component.ClickedListener {

        @Override
        public void onClick(Component component) {
            switch (component.getId()) {
                case ResourceTable.Id_back:

                    break;
                case ResourceTable.Id_card_story:

                    Intent intent = new Intent();
                    Operation operation = new Intent.OperationBuilder()
                            .withDeviceId("")
                            .withBundleName("com.example.timestory")
                            .withAbilityName("com.example.timestory.ability.card.ShowCardStoryAbility")
                            .build();
                    intent.setParam("card", card);
                    intent.setOperation(operation);
                    startAbility(intent, 0);
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
