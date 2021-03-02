package com.example.timestory.ability.user;

import com.example.timestory.ability.user.slice.UserCenterAbilitySlice;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;

public class UserCenterAbility extends Ability {
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setMainRoute(UserCenterAbilitySlice.class.getName());
    }
}