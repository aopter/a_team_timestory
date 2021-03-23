package com.example.timestory.ability.dynasty;

import com.example.timestory.ability.dynasty.slice.DetailsEventAbilitySlice;
import com.example.timestory.ability.dynasty.slice.DynastyIntroduceActivitySlice;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;

public class DetailsEventAbility extends Ability {
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setMainRoute(DetailsEventAbilitySlice.class.getName());
    }
}
