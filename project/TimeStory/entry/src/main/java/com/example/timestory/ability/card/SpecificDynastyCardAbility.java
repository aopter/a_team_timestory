package com.example.timestory.ability.card;

import com.example.timestory.ability.card.slice.SpecificDynastyCardAbilitySlice;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;

public class SpecificDynastyCardAbility extends Ability {
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setMainRoute(SpecificDynastyCardAbilitySlice.class.getName());
    }
}
