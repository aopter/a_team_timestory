package com.example.timestory.ability.user.slice;

import com.example.timestory.ResourceTable;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;

/**
 * @author PengHuAnZhi
 * @createTime 2021/3/23 13:57
 * @projectName TimeStory
 * @className SettingAbilitySlice.java
 * @description TODO
 */
public class SettingAbilitySlice extends AbilitySlice {
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_user_setting);
    }
}