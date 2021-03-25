package com.example.timestory.ability.user;

import com.example.timestory.ability.user.slice.SettingAbilitySlice;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;

/**
 * @author PengHuAnZhi
 * @createTime 2021/3/23 13:56
 * @projectName TimeStory
 * @className SettingAbility.java
 * @description TODO
 */
public class SettingAbility extends Ability {
    @Override
    protected void onStart(Intent intent) {
        super.onStart(intent);
        super.setMainRoute(SettingAbilitySlice.class.getName());
    }
}