package com.example.timesequence.ability.dynasty;

import com.example.timesequence.ability.dynasty.slice.DynastyIntroduceActivitySlice;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;

public class DynastyIntroduceActivity extends Ability {
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setMainRoute(DynastyIntroduceActivitySlice.class.getName());
    }
}
