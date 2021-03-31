package com.example.timestory.ability.card.adapter;

import com.example.timestory.ResourceTable;
import com.example.timestory.Utils.HmOSImageLoader;
import com.example.timestory.constant.ServiceConfig;
import com.example.timestory.entity.card.UserCard;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.Operation;
import ohos.agp.components.*;

import java.util.List;

public class SpecificDynastyCardProvider extends RecycleItemProvider {
    private List<List<UserCard>> cards;
    private AbilitySlice slice;

    public SpecificDynastyCardProvider(List<List<UserCard>> cards, AbilitySlice slice) {
        this.cards = cards;
        this.slice = slice;
    }

    @Override
    public int getCount() {
        return cards == null ? 0 : cards.size();
    }

    @Override
    public Object getItem(int i) {
        if (cards != null && i >= 0 && i < cards.size()) {
            return cards.get(i);
        }
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public Component getComponent(int position, Component component, ComponentContainer componentContainer) {
        final Component cpt;
        if (component == null) {
            cpt = LayoutScatter.getInstance(slice).parse(ResourceTable.Layout_item_dynasty_card, null, false);
        } else {
            cpt = component;
        }
        DirectionalLayout cardContainer1 = (DirectionalLayout) cpt.findComponentById(ResourceTable.Id_card_container1);
        Text cardName1 = (Text) cpt.findComponentById(ResourceTable.Id_card_name1);
        Image cardPic1 = (Image) cpt.findComponentById(ResourceTable.Id_card_pic1);
        Text cardNum1 = (Text) cpt.findComponentById(ResourceTable.Id_card_num1);
        int width = cardPic1.getWidth() - 20;
        int height = cardPic1.getHeight() - 40;
        DirectionalLayout cardContainer2 = (DirectionalLayout) cpt.findComponentById(ResourceTable.Id_card_container2);
        Text cardName2 = (Text) cpt.findComponentById(ResourceTable.Id_card_name2);
        Image cardPic2 = (Image) cpt.findComponentById(ResourceTable.Id_card_pic2);
        Text cardNum2 = (Text) cpt.findComponentById(ResourceTable.Id_card_num2);
        DirectionalLayout cardContainer3 = (DirectionalLayout) cpt.findComponentById(ResourceTable.Id_card_container3);
        Text cardName3 = (Text) cpt.findComponentById(ResourceTable.Id_card_name3);
        Image cardPic3 = (Image) cpt.findComponentById(ResourceTable.Id_card_pic3);
        Text cardNum3 = (Text) cpt.findComponentById(ResourceTable.Id_card_num3);
        DirectionalLayout cardContainer4 = (DirectionalLayout) cpt.findComponentById(ResourceTable.Id_card_container4);
        Text cardName4 = (Text) cpt.findComponentById(ResourceTable.Id_card_name4);
        Image cardPic4 = (Image) cpt.findComponentById(ResourceTable.Id_card_pic4);
        Text cardNum4 = (Text) cpt.findComponentById(ResourceTable.Id_card_num4);
        for (int i = 0; i < cards.get(position).size(); i++) {
            if (i % 4 == 0) {
                cardContainer1.setVisibility(Component.VISIBLE);
                cardName1.setText(cards.get(position).get(i).getCardListVO().getCardName());
                HmOSImageLoader.with(slice)
                        .load(ServiceConfig.SERVICE_ROOT + "/picture/download/" + cards.get(position).get(i).getCardListVO().getCardPicture())
                        .into(cardPic1);
                cardNum1.setText(cards.get(position).get(i).getCardCount() + "");
                int finalI = i;
                cardContainer1.setClickedListener(new Component.ClickedListener() {
                    @Override
                    public void onClick(Component component) {
                        Intent intent = new Intent();
                        Operation operation = new Intent.OperationBuilder()
                                .withDeviceId("")
                                .withBundleName("com.example.timestory")
                                .withAbilityName("com.example.timestory.ability.card.SpecificCardDetailAbility")
                                .build();
                        intent.setParam("cardId", cards.get(position).get(finalI).getCardListVO().getCardId());
                        intent.setOperation(operation);
                        slice.startAbility(intent, 0);
                    }
                });
            } else if (i % 4 == 1) {
                cardContainer2.setVisibility(Component.VISIBLE);
                cardName2.setText(cards.get(position).get(i).getCardListVO().getCardName());
                HmOSImageLoader.with(slice)
                        .load(ServiceConfig.SERVICE_ROOT + "/picture/download/" + cards.get(position).get(i).getCardListVO().getCardPicture())
                        .into(cardPic2);
                cardNum2.setText(cards.get(position).get(i).getCardCount() + "");
                int finalI = i;
                cardContainer2.setClickedListener(new Component.ClickedListener() {
                    @Override
                    public void onClick(Component component) {
                        Intent intent = new Intent();
                        Operation operation = new Intent.OperationBuilder()
                                .withDeviceId("")
                                .withBundleName("com.example.timestory")
                                .withAbilityName("com.example.timestory.ability.card.SpecificCardDetailAbility")
                                .build();
                        intent.setParam("cardId", cards.get(position).get(finalI).getCardListVO().getCardId());
                        intent.setOperation(operation);
                        slice.startAbility(intent, 0);
                    }
                });
            } else if (i % 4 == 2) {
                cardContainer3.setVisibility(Component.VISIBLE);
                cardName3.setText(cards.get(position).get(i).getCardListVO().getCardName());
                HmOSImageLoader.with(slice)
                        .load(ServiceConfig.SERVICE_ROOT + "/picture/download/" + cards.get(position).get(i).getCardListVO().getCardPicture())
                        .into(cardPic3);
                cardNum3.setText(cards.get(position).get(i).getCardCount() + "");
                int finalI = i;
                cardContainer3.setClickedListener(new Component.ClickedListener() {
                    @Override
                    public void onClick(Component component) {
                        Intent intent = new Intent();
                        Operation operation = new Intent.OperationBuilder()
                                .withDeviceId("")
                                .withBundleName("com.example.timestory")
                                .withAbilityName("com.example.timestory.ability.card.SpecificCardDetailAbility")
                                .build();
                        intent.setParam("cardId", cards.get(position).get(finalI).getCardListVO().getCardId());
                        intent.setOperation(operation);
                        slice.startAbility(intent, 0);
                    }
                });
            } else if (i % 4 == 3) {
                cardContainer4.setVisibility(Component.VISIBLE);
                cardName4.setText(cards.get(position).get(i).getCardListVO().getCardName());
                HmOSImageLoader.with(slice)
                        .load(ServiceConfig.SERVICE_ROOT + "/picture/download/" + cards.get(position).get(i).getCardListVO().getCardPicture())
                        .into(cardPic4);
                cardNum4.setText(cards.get(position).get(i).getCardCount() + "");
                int finalI = i;
                cardContainer4.setClickedListener(new Component.ClickedListener() {
                    @Override
                    public void onClick(Component component) {
                        Intent intent = new Intent();
                        Operation operation = new Intent.OperationBuilder()
                                .withDeviceId("")
                                .withBundleName("com.example.timestory")
                                .withAbilityName("com.example.timestory.ability.card.SpecificCardDetailAbility")
                                .build();
                        intent.setParam("cardId", cards.get(position).get(finalI).getCardListVO().getCardId());
                        intent.setOperation(operation);
                        slice.startAbility(intent, 0);
                    }
                });
            }
        }
        return cpt;
    }
}
