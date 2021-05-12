package com.example.timesequence.ability.problem;

import com.example.timesequence.ability.problem.slice.SelectProblemSlice;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;

public class ProblemInfoAbility extends Ability {
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setMainRoute(SelectProblemSlice.class.getName());
    }
}
