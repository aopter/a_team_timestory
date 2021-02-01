package com.example.timestroy.ability.dynasty;

import com.example.timestroy.ability.dynasty.slice.HomePageAbilitySlice;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;

public class HomePageAbility extends Ability {
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setMainRoute(HomePageAbilitySlice.class.getName());
    }
}
