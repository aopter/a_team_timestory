package com.example.timestory.ability.user.slice;

import com.example.timestory.ResourceTable;
import com.example.timestory.Utils.HmOSImageLoader;
import com.example.timestory.constant.Constant;
import com.example.timestory.constant.ServiceConfig;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.ability.DataAbilityHelper;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Image;
import ohos.media.image.ImageSource;
import ohos.media.image.PixelMap;
import ohos.media.image.common.Size;
import ohos.utils.net.Uri;

import java.io.FileDescriptor;

/**
 * @author PengHuAnZhi
 * @createTime 2021/3/23 13:57
 * @projectName TimeStory
 * @className SettingAbilitySlice.java
 * @description TODO
 */
public class SettingAbilitySlice extends AbilitySlice {
    private Image image;

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_user_setting);
        initView();
        setClickListener();
        judgeModifyUserHeader(intent);
    }

    private void judgeModifyUserHeader(Intent intent) {
        if (intent.getBooleanParam("modifyUserHeader", false)) {
            HmOSImageLoader.with(this).load(ServiceConfig.SERVICE_ROOT + "/img/" + Constant.User.getUserHeader()).into(image);
        }
    }

    private void setClickListener() {
        image.setClickedListener(component -> {
            present(new PickHeaderAbilitySlice(), new Intent());
        });
    }

    private void initView() {
        image = (Image) findComponentById(ResourceTable.Id_setting_user_header);
    }
}