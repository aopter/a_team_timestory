package com.example.timestory.ability.card.slice;

import com.example.timestory.ResourceTable;
import com.example.timestory.entity.card.UserCard;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Component;
import ohos.agp.components.ListContainer;

import java.util.ArrayList;
import java.util.List;


public class SpecificDynastyCardAbilitySlice extends AbilitySlice {
    ListContainer dynastyCardView;
    private List<UserCard> userCards = new ArrayList<>();
    private int dynastyId;


    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_specific_dynasty);
        initComponent();
        setListener();
    }

    private void setListener() {
        MyListener myListener = new MyListener();

    }

    private void initComponent() {
        
    }

    class MyListener implements Component.ClickedListener {

        @Override
        public void onClick(Component component) {
            switch (component.getId()) {

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
