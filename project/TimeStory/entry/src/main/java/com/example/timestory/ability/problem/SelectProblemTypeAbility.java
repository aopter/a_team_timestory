package com.example.timestory.ability.problem;

import com.example.timestory.ability.problem.slice.SelectProblemTypeAbilitySlice;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Text;

public class SelectProblemTypeAbility extends Ability {



    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setMainRoute(SelectProblemTypeAbilitySlice.class.getName());
    }
}
