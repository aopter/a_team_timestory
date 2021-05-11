package com.example.timestory.ability.dynasty.slice;

import com.example.timestory.ResourceTable;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.webengine.WebView;

public class AllScreenImgAbilitySlice extends AbilitySlice {

    @Override
    protected void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_all_screen);
        WebView allScreen = (WebView) findComponentById(ResourceTable.Id_all_screen);
        allScreen.getWebConfig().setJavaScriptPermit(true);  // 如果网页需要使用JavaScript，增加此行；如何使用JavaScript下文有详细介绍
        final String url = "http://webapp.vizen.cn/gugong_app_pc/index.html"; // EXAMPLE_URL由开发者自定义
        allScreen.load(url);
    }
}
