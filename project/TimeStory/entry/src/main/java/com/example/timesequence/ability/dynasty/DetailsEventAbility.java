package com.example.timesequence.ability.dynasty;

import com.example.timesequence.ability.dynasty.slice.DetailsEventAbilitySlice;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;

public class DetailsEventAbility extends Ability {
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setMainRoute(DetailsEventAbilitySlice.class.getName());
    }
}
