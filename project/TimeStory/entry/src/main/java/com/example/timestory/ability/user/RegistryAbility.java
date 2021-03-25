package com.example.timestory.ability.user;

import com.example.timestory.ability.user.slice.RegistryAbilitySlice;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;

public class RegistryAbility extends Ability {

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setMainRoute(RegistryAbilitySlice.class.getName());
    }
}