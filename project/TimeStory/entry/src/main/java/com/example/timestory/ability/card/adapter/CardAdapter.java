package com.example.timestory.ability.card.adapter;

import com.example.timestory.ResourceTable;
import com.example.timestory.entity.card.Icon;
import ohos.aafwk.ability.AbilitySlice;
import ohos.agp.components.*;

import java.util.List;

public class CardAdapter extends RecycleItemProvider {
    private List<Icon> icons;
    private AbilitySlice slice;

    public CardAdapter(List<Icon> icons, AbilitySlice slice) {
        this.icons = icons;
        this.slice = slice;
    }

    @Override
    public int getCount() {
        return icons.size();
    }

    @Override
    public Object getItem(int i) {
        return icons.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public Component getComponent(int i, Component component, ComponentContainer componentContainer) {
        Component cpt = component;
        if (cpt == null) {
            cpt = LayoutScatter.getInstance(slice).parse(ResourceTable.Layout_item_share_icon, null, false);
        }
        Icon icon = icons.get(i);
        Image image = (Image) cpt.findComponentById(ResourceTable.Id_share_icon);
        Text text = (Text) cpt.findComponentById(ResourceTable.Id_share_name);
        // TODO 利用位图将参数传递
        text.setText(icon.getName());
        return cpt;
    }
}
