package com.example.timesequence.ability.card;

import com.example.timesequence.ability.card.slice.SpecificCardDetailAbilitySlice;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;

public class SpecificCardDetailAbility extends Ability {
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setMainRoute(SpecificCardDetailAbilitySlice.class.getName());
    }
}
