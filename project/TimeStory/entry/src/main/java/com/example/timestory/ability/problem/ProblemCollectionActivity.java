package com.example.timestory.ability.problem;

import com.example.timestory.ability.problem.slice.ProblemCollectionActivitySlice;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;

public class ProblemCollectionActivity extends Ability {
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setMainRoute(ProblemCollectionActivitySlice.class.getName());
    }
}
