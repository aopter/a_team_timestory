package com.example.timestory.ability.problem.slice;


import com.example.timestory.ResourceTable;
import com.example.timestory.Utils.Util;
import com.example.timestory.ability.dynasty.HomePageAbility;
import com.example.timestory.ability.problem.ProblemInfoAbility;
import com.example.timestory.ability.problem.SelectProblemTypeAbility;
import com.example.timestory.ability.user.LoginAbility;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Component;
import ohos.agp.components.DirectionalLayout;

public class SelectProblemSlice extends AbilitySlice {


    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_select_problem_type);


    }



    @Override
    public void onActive() {
        super.onActive();
    }

    @Override
    public void onForeground(Intent intent) {
        super.onForeground(intent);
    }



}
