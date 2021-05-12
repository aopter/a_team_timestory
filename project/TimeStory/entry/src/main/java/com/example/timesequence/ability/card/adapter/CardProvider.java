package com.example.timesequence.ability.card.adapter;

import com.example.timesequence.ResourceTable;
import com.example.timesequence.entity.UserUnlockDynasty;
import ohos.aafwk.ability.AbilitySlice;
import ohos.agp.components.*;

import java.util.List;

public class CardProvider extends RecycleItemProvider {
    private List<UserUnlockDynasty> dynasties;
    private AbilitySlice slice;

    public CardProvider(List<UserUnlockDynasty> dynasties, AbilitySlice slice) {
        this.dynasties = dynasties;
        this.slice = slice;
    }

    @Override
    public int getCount() {
        return dynasties == null ? 0 : dynasties.size();
    }

    @Override
    public Object getItem(int i) {
        if (dynasties != null && i >= 0 && i < dynasties.size()) {
            return dynasties.get(i);
        }
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public Component getComponent(int i, Component component, ComponentContainer componentContainer) {
        final Component cpt;
        if (component == null) {
            cpt = LayoutScatter.getInstance(slice).parse(ResourceTable.Layout_item_dynasty, null, false);
        } else {
            cpt = component;
        }
        Text dynasty = (Text) cpt.findComponentById(ResourceTable.Id_dynasty);
        dynasty.setText(dynasties.get(i).getDynastyName());
        return cpt;
    }
}
