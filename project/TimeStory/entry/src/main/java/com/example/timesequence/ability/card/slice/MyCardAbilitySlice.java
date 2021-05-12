package com.example.timesequence.ability.card.slice;

import com.example.timesequence.ability.card.adapter.CardProvider;
import com.example.timesequence.ResourceTable;
import com.example.timesequence.constant.Constant;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.Operation;
import ohos.agp.components.*;

public class MyCardAbilitySlice extends AbilitySlice {
    private ListContainer dyanstiesView;
    private CardProvider cardProvider;
    private Image back;

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_my_card);
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
                        .withBundleName("com.example.timesequence")
                        .withAbilityName("com.example.timesequence.ability.card.SpecificDynastyCardAbility")
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
