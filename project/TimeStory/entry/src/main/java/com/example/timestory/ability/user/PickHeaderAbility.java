package com.example.timestory.ability.user;

import com.example.timestory.ability.user.slice.PickHeaderAbilitySlice;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;

/**
 * @author PengHuAnZhi
 * @createTime 2021/3/26 9:57
 * @projectName TimeStory
 * @className PickHeaderAbility.java
 * @description TODO
 */
public class PickHeaderAbility extends Ability {

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setMainRoute(PickHeaderAbilitySlice.class.getName());
    }
}