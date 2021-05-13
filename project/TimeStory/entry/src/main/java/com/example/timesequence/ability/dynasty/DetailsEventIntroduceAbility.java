package com.example.timesequence.ability.dynasty;

import com.example.timesequence.ability.dynasty.slice.DetailsEventIntroduceSlice;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.ability.IAbilityContinuation;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.IntentParams;

import java.util.ArrayList;
import java.util.List;

public class DetailsEventIntroduceAbility extends Ability {

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setMainRoute(DetailsEventIntroduceSlice.class.getName());
    }
}
