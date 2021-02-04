package com.example.timestory.ability.card;

import com.example.timestory.ability.card.slice.MyCardAbilitySlice;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;

public class MyCardAbility extends Ability {
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setMainRoute(MyCardAbilitySlice.class.getName());
    }
}
