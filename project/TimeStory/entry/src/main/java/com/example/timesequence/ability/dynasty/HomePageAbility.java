package com.example.timesequence.ability.dynasty;

import com.example.timesequence.ability.dynasty.slice.HomePageAbilitySlice;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;

public class HomePageAbility extends Ability {
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setMainRoute(HomePageAbilitySlice.class.getName());
    }
}
