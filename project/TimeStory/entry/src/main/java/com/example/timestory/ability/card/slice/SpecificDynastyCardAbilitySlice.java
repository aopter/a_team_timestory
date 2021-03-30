package com.example.timestory.ability.card.slice;

import com.example.timestory.ResourceTable;
import com.example.timestory.ability.card.adapter.SpecificDynastyCardProvider;
import com.example.timestory.constant.ServiceConfig;
import com.example.timestory.entity.card.UserCard;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.*;
import ohos.agp.window.dialog.ToastDialog;
import ohos.app.dispatcher.TaskDispatcher;
import ohos.app.dispatcher.task.TaskPriority;
import ohos.eventhandler.EventHandler;
import ohos.eventhandler.EventRunner;
import ohos.eventhandler.InnerEvent;
import okhttp3.*;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class SpecificDynastyCardAbilitySlice extends AbilitySlice {
    private AbilitySlice abilitySlice = this;
    private ListContainer dynastyCardView;
    private List<List<UserCard>> userCards = new ArrayList<>();
    private int dynastyId;
    private Image back;
    private TextField searchCardName;
    private Image searchDelete;
    private Text searchBtn;
    private OkHttpClient client;
    private Gson gson;
    private int type = 0;
    private SpecificDynastyCardProvider cardAdapter;
    private TaskDispatcher parallelTaskDispatcher;//并发任务分发器
    private EventRunner eventRunner = EventRunner.create(true);//线程投递器
    private EventHandler eventHandler = new EventHandler(eventRunner) {
        @Override
        protected void processEvent(InnerEvent event) {
            super.processEvent(event);
            switch (event.eventId) {
                case 1:
                    String result = (String) event.object;
                    Type type = new TypeToken<ArrayList<UserCard>>() {
                    }.getType();
                    List<UserCard> cards = gson.fromJson(result, type);
                    for (int i = 0; i < cards.size(); i++) {
                        if (i % 4 == 0) {
                            List<UserCard> u = new ArrayList<>();
                            u.add(cards.get(i));
                            if (i < cards.size() - 1) {
                                u.add(cards.get(i + 1));
                                if (i < cards.size() - 2) {
                                    u.add(cards.get(i + 2));
                                    if (i < cards.size() - 3) {
                                        u.add(cards.get(i + 3));
                                    }
                                }
                            }
                            userCards.add(u);
                        }
                    }
                    System.out.println("____dycard____" + result);
                    if (userCards.size() == 0) {
                        getUITaskDispatcher().syncDispatch(() ->
                                new ToastDialog(abilitySlice).setText("没有此朝代的卡片").show()
                        );
                    } else {
                        getUITaskDispatcher().syncDispatch(() ->
                                cardAdapter = new SpecificDynastyCardProvider(userCards, abilitySlice)
                        );
                        getUITaskDispatcher().syncDispatch(() ->
                                dynastyCardView.setItemProvider(cardAdapter)
                        );
                    }
                    break;
                case 2:
                    userCards.clear();
                    String result2 = (String) event.object;
                    Type type2 = new TypeToken<ArrayList<UserCard>>() {
                    }.getType();
                    List<UserCard> cards2 = gson.fromJson(result2, type2);
                    for (int i = 0; i < cards2.size(); i++) {
                        if (i % 4 == 0) {
                            List<UserCard> u = new ArrayList<>();
                            u.add(cards2.get(i));
                            if (i < cards2.size() - 1) {
                                u.add(cards2.get(i + 1));
                                if (i < cards2.size() - 2) {
                                    u.add(cards2.get(i + 2));
                                    if (i < cards2.size() - 3) {
                                        u.add(cards2.get(i + 3));
                                    }
                                }
                            }
                            userCards.add(u);
                        }
                    }
                    System.out.println("____dycard____" + userCards.toString());
                    if (userCards.size() == 0) {
                        getUITaskDispatcher().syncDispatch(() ->
                                new ToastDialog(abilitySlice).setText("没有相关卡片，请重新搜索吧").show()
                        );
                    }
                    getUITaskDispatcher().syncDispatch(() ->
                            cardAdapter = new SpecificDynastyCardProvider(userCards, abilitySlice)
                    );
                    getUITaskDispatcher().syncDispatch(() ->
                            dynastyCardView.setItemProvider(cardAdapter)
                    );
                    break;
            }
        }
    };

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_specific_dynasty);
        parallelTaskDispatcher = createParallelTaskDispatcher("specificDynastyCardParallelTaskDispatcher", TaskPriority.DEFAULT);
        initComponent();
        setListener();
        String id = intent.getStringParam("dynastyId");
        dynastyId = Integer.parseInt(id);
        System.out.println("____dycard____已经启动" + dynastyId);
        getCardsByType(type);
    }

    private void setListener() {
        MyListener myListener = new MyListener();
        back.setClickedListener(myListener);
        searchDelete.setClickedListener(myListener);
        searchBtn.setClickedListener(myListener);
    }

    private void initComponent() {
        dynastyCardView = (ListContainer) findComponentById(ResourceTable.Id_dynasty_cards);
        back = (Image) findComponentById(ResourceTable.Id_back);
        searchCardName = (TextField) findComponentById(ResourceTable.Id_search_card_name);
        searchDelete = (Image) findComponentById(ResourceTable.Id_search_delete);
        searchBtn = (Text) findComponentById(ResourceTable.Id_search_btn);
        client = new OkHttpClient();
        gson = new GsonBuilder()//创建GsonBuilder对象
                .serializeNulls()//允许输出Null值属性
                .create();//创建Gson对象
        searchCardName.addTextObserver(new Text.TextObserver() {
            @Override
            public void onTextUpdated(String s, int i, int i1, int i2) {
                if (!"".equals(searchCardName.getText().toString())) {
                    searchDelete.setVisibility(Component.VISIBLE);
                } else {
                    searchDelete.setVisibility(Component.INVISIBLE);
                }
            }
        });
    }

    class MyListener implements Component.ClickedListener {

        @Override
        public void onClick(Component component) {
            switch (component.getId()) {
                case ResourceTable.Id_back:
                    break;
                case ResourceTable.Id_search_btn:
                    String key = searchCardName.getText().toString().trim();
                    System.out.println("____dycard____" + userCards.toString());
                    if ("".equals(key)) {
                        getCardsByType(type);
                    } else {
                        getCardsByKey(key);
                    }
                    break;
                case ResourceTable.Id_search_delete:
                    searchCardName.setText("");
                    searchDelete.setVisibility(Component.INVISIBLE);
                    break;

            }
        }
    }

    private void getCardsByKey(String key) {
        Runnable getCardsByKey = new Runnable() {
            @Override
            public void run() {
                // TODO Constant.User.getUserId()
                Request request = new Request.Builder()
                        .url(ServiceConfig.SERVICE_ROOT + "/usercard/list/" + 6 + "/" + dynastyId + "/" + type + "/" + key)
                        .build();
                Call call = client.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        new ToastDialog(getContext()).setText("获取朝代卡片失败了").show();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String result = response.body().string();
                        InnerEvent event = InnerEvent.get(2, result);
                        eventHandler.sendEvent(event);
                    }
                });
            }
        };
        parallelTaskDispatcher.asyncDispatch(getCardsByKey);
    }

    private void getCardsByType(int type) {
        Runnable getCardsByType = new Runnable() {
            @Override
            public void run() {
                // TODO Constant.User.getUserId()
                Request request = new Request.Builder()
                        .url(ServiceConfig.SERVICE_ROOT + "/usercard/list/" + 6 + "/" + dynastyId + "/" + type)
                        .build();
                Call call = client.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        // 获取失败
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String result = response.body().string();
                        InnerEvent event = null;
                        if (cardAdapter == null) {
                            event = InnerEvent.get(1, result);
                        } else {
                            event = InnerEvent.get(2, result);
                        }
                        eventHandler.sendEvent(event);
                    }
                });
            }
        };
        parallelTaskDispatcher.asyncDispatch(getCardsByType);
    }


    @Override
    public void onActive() {
        super.onActive();
        searchCardName.setText("");
    }

    @Override
    public void onForeground(Intent intent) {
        super.onForeground(intent);
    }
}
