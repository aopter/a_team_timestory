package com.example.timestory.ability.card.adapter;

import com.example.timestory.ResourceTable;
import com.example.timestory.entity.card.UserCard;
import ohos.aafwk.ability.AbilitySlice;
import ohos.agp.components.Component;
import ohos.agp.components.ComponentContainer;
import ohos.agp.components.LayoutScatter;
import ohos.agp.components.RecycleItemProvider;

import java.util.List;

public class SpecificDynastyCardAdapter extends RecycleItemProvider {
    private List<UserCard> cards;
    private AbilitySlice slice;

    @Override
    public int getCount() {
        return cards.size();
    }

    @Override
    public Object getItem(int i) {
        return cards.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public Component getComponent(int i, Component component, ComponentContainer componentContainer) {
        Component cpt = component;
        if (cpt == null) {
            cpt = LayoutScatter.getInstance(slice).parse(ResourceTable.Layout_item_dynasty_card, null, false);
        }
//        Icon icon = icons.get(i);
//        Image image = (Image) cpt.findComponentById(ResourceTable.Id_share_icon);
//        Text text = (Text) cpt.findComponentById(ResourceTable.Id_share_name);
//        // TODO 利用位图将参数传递
//        text.setText(icon.getName());
        return cpt;
    }
}
