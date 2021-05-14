package com.example.timesequence.ability.dynasty.slice;

import com.example.timesequence.ResourceTable;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.webengine.WebConfig;
import ohos.agp.components.webengine.WebView;

public class AllScreenImgAbilitySlice extends AbilitySlice {

    @Override
    protected void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_all_screen);
        WebView allScreen = (WebView) findComponentById(ResourceTable.Id_all_screen);
        WebConfig webConfig = allScreen.getWebConfig();
        webConfig.setSecurityMode(WebConfig.SECURITY_SELF_ADAPTIVE);
        webConfig.setDataAbilityPermit(true);
        webConfig.setJavaScriptPermit(true);
        webConfig.setLoadsImagesPermit(true);
        webConfig.setLocationPermit(true);
        webConfig.setMediaAutoReplay(true);
        final String url = "http://webapp.vizen.cn/gugong_app_pc/index.html"; // EXAMPLE_URL由开发者自定义
        allScreen.load(url);
    }
}
