package com.example.timesequence.ability.problem;

import com.example.timesequence.ability.problem.slice.SelectProblemTypeAbilitySlice;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;

public class SelectProblemTypeAbility extends Ability {



    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setMainRoute(SelectProblemTypeAbilitySlice.class.getName());
    }
}
