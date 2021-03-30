package com.example.timestory.ability.card.slice;

import com.example.timestory.ResourceTable;
import com.example.timestory.ability.card.adapter.CardProvider;
import com.example.timestory.constant.Constant;
import com.example.timestory.entity.UserUnlockDynasty;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.Operation;
import ohos.agp.components.*;

import java.util.ArrayList;
import java.util.List;

public class MyCardAbilitySlice extends AbilitySlice {
    private ListContainer dyanstiesView;
    private CardProvider cardProvider;
    private Image back;

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_my_card);
        List<UserUnlockDynasty> dynasties = new ArrayList<>();
        UserUnlockDynasty dy1 = new UserUnlockDynasty();
        dy1.setDynastyId("10");
        dy1.setDynastyName("隋朝");
        UserUnlockDynasty dy2 = new UserUnlockDynasty();
        dy2.setDynastyId("11");
        dy2.setDynastyName("唐朝");
        UserUnlockDynasty dy3 = new UserUnlockDynasty();
        dy3.setDynastyId("12");
        dy3.setDynastyName("五代十国");
        dynasties.add(dy1);
        dynasties.add(dy2);
        dynasties.add(dy3);
        Constant.UnlockDynasty = dynasties;
        System.out.println("mycard" + dynasties);
        initComponent();
        setListener();
    }

    private void setListener() {
        MyListener myListener = new MyListener();
        back.setClickedListener(myListener);
    }

    private void initComponent() {
        dyanstiesView = (ListContainer) findComponentById(ResourceTable.Id_dynasty_list);
        back = (Image) findComponentById(ResourceTable.Id_back);
        cardProvider = new CardProvider(Constant.UnlockDynasty, this);
        dyanstiesView.setItemProvider(cardProvider);
        dyanstiesView.setOrientation(Component.HORIZONTAL);
        dyanstiesView.setReboundEffect(true);
        dyanstiesView.setItemClickedListener(new ListContainer.ItemClickedListener() {
            @Override
            public void onItemClicked(ListContainer listContainer, Component component, int i, long l) {
                Intent intent = new Intent();
                Operation operation = new Intent.OperationBuilder()
                        .withDeviceId("")
                        .withBundleName("com.example.timestory")
                        .withAbilityName("com.example.timestory.ability.card.SpecificDynastyCardAbility")
                        .build();
                intent.setParam("dynastyId", Constant.UnlockDynasty.get(i).getDynastyId());
                intent.setOperation(operation);
                System.out.println("____dycard____click:" + Constant.UnlockDynasty.get(i).getDynastyId());
                startAbility(intent, 0);
            }
        });
    }

    class MyListener implements Component.ClickedListener {

        @Override
        public void onClick(Component component) {
            switch (component.getId()) {
                case ResourceTable.Id_back:
                    terminate();
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
