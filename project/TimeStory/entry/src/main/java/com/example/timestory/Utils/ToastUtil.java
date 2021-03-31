package com.example.timestory.Utils;


import com.example.timestory.ResourceTable;
import ohos.agp.components.*;
import ohos.agp.utils.LayoutAlignment;
import ohos.agp.window.dialog.ToastDialog;
import ohos.app.Context;

public class ToastUtil {
    public static void showEncourageToast(Context context, String message) {
        Component toastLayout = LayoutScatter.getInstance(context).parse(ResourceTable.Layout_toast_img_right, null, false);
        Text text = (Text) toastLayout.findComponentById(ResourceTable.Id_toast_message);
        Image image = (Image) toastLayout.findComponentById(ResourceTable.Id_iv_expression);
        text.setText(message);
        image.setPixelMap(ResourceTable.Media_toast_encourage);
        new ToastDialog(context)
                .setContentCustomComponent(toastLayout)
                .setSize(DirectionalLayout.LayoutConfig.MATCH_CONTENT, DirectionalLayout.LayoutConfig.MATCH_CONTENT)
                .setAlignment(LayoutAlignment.BOTTOM)
                .show();
    }

    public static void showCryToast(Context context, String message) {
        Component toastLayout = LayoutScatter.getInstance(context).parse(ResourceTable.Layout_toast_img_left, null, false);
        Text text = (Text) toastLayout.findComponentById(ResourceTable.Id_toast_message);
        Image image = (Image) toastLayout.findComponentById(ResourceTable.Id_iv_expression);
        text.setText(message);
        image.setPixelMap(ResourceTable.Media_toast_cry);
        new ToastDialog(context)
                .setContentCustomComponent(toastLayout)
                .setSize(DirectionalLayout.LayoutConfig.MATCH_CONTENT, DirectionalLayout.LayoutConfig.MATCH_CONTENT)
                .setAlignment(LayoutAlignment.BOTTOM)
                .show();
    }

    public static void showSickToast(Context context, String message) {
        Component toastLayout = LayoutScatter.getInstance(context).parse(ResourceTable.Layout_toast_img_right, null, false);
        Text text = (Text) toastLayout.findComponentById(ResourceTable.Id_toast_message);
        Image image = (Image) toastLayout.findComponentById(ResourceTable.Id_iv_expression);
        text.setText(message);
        image.setPixelMap(ResourceTable.Media_toast_sorry);
        new ToastDialog(context)
                .setContentCustomComponent(toastLayout)
                .setSize(DirectionalLayout.LayoutConfig.MATCH_CONTENT, DirectionalLayout.LayoutConfig.MATCH_CONTENT)
                .setAlignment(LayoutAlignment.BOTTOM)
                .show();
    }
}
