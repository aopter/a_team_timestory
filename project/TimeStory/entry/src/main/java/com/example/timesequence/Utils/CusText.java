package com.example.timesequence.Utils;

import ohos.agp.components.AttrSet;
import ohos.agp.components.Text;
import ohos.agp.text.Font;
import ohos.app.Context;

import java.io.File;

/**
 * @author PengHuAnZhi
 * @createTime 2021/3/23 17:17
 * @projectName TimeStory
 * @className CusText.java
 * @description TODO
 */
public class CusText extends Text {
    public CusText(Context context) {
        super(context);
        initFont();
    }

    public CusText(Context context, AttrSet attrSet) {
        super(context, attrSet);
        initFont();
    }

    public CusText(Context context, AttrSet attrSet, String styleName) {
        super(context, attrSet, styleName);
        initFont();
    }

    private void initFont() {
        if (!isFocused()) {
            Font.Builder builder = new Font.Builder(new File("profile/custom_fontt.ttf"));
            Font font = builder.build();
            setFont(font);
        }
    }
}