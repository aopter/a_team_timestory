package com.example.timestory.ability.dynasty;

import com.example.timestory.ability.dynasty.slice.DetailsEventIntroduceSlice;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;

public class DetailsEventIntroduceAbility extends Ability {

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setMainRoute(DetailsEventIntroduceSlice.class.getName());
    }
}
