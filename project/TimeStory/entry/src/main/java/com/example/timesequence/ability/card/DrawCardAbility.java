package com.example.timesequence.ability.card;

import com.example.timesequence.ability.card.slice.DrawCardAbilitySlice;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;

public class DrawCardAbility extends Ability {
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setMainRoute(DrawCardAbilitySlice.class.getName());
    }
}
