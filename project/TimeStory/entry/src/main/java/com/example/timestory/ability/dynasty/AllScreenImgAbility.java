package com.example.timestory.ability.dynasty;

import com.example.timestory.ability.dynasty.slice.AllScreenImgAbilitySlice;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;

public class AllScreenImgAbility extends Ability {
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setMainRoute(AllScreenImgAbilitySlice.class.getName());
    }
}
