package com.example.timesequence.ability.dynasty;

import com.example.timesequence.ability.dynasty.slice.AllScreenImgAbilitySlice;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;

public class AllScreenImgAbility extends Ability {
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setMainRoute(AllScreenImgAbilitySlice.class.getName());
    }
}
