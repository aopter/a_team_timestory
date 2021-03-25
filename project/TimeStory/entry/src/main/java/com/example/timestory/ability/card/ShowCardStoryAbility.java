package com.example.timestory.ability.card;

import com.example.timestory.ability.card.slice.ShowCardStoryAbilitySlice;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;

public class ShowCardStoryAbility extends Ability {
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setMainRoute(ShowCardStoryAbilitySlice.class.getName());
    }
}
