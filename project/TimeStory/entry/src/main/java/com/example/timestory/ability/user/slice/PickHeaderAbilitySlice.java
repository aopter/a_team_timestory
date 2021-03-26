package com.example.timestory.ability.user.slice;

import com.example.timestory.ResourceTable;
import com.example.timestory.constant.Constant;
import com.example.timestory.constant.ServiceConfig;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.ability.DataAbilityHelper;
import ohos.aafwk.ability.DataAbilityRemoteException;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Component;
import ohos.agp.components.Image;
import ohos.agp.components.TableLayout;
import ohos.agp.components.Text;
import ohos.agp.window.dialog.ToastDialog;
import ohos.data.resultset.ResultSet;
import ohos.media.image.ImageSource;
import ohos.media.image.PixelMap;
import ohos.media.image.common.Size;
import ohos.media.photokit.metadata.AVStorage;
import ohos.utils.net.Uri;
import okhttp3.*;

import java.io.*;
import java.util.ArrayList;

/**
 * @author PengHuAnZhi
 * @createTime 2021/3/26 9:57
 * @projectName TimeStory
 * @className PickHeaderAbilitySlice.java
 * @description TODO
 */
public class PickHeaderAbilitySlice extends AbilitySlice {
    private TableLayout imgLayout;
    private Text preText, text;

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_user_setting_pick_head);
        imgLayout = (TableLayout) findComponentById(ResourceTable.Id_layout_id);
        imgLayout.setColumnCount(3);
        preText = (Text) findComponentById(ResourceTable.Id_text_pre_id);
        text = (Text) findComponentById(ResourceTable.Id_text_id);
        displayPic();
    }

    private void displayPic() {
        imgLayout.removeAllComponents();
        ArrayList<Integer> imgList = new ArrayList<>();
        DataAbilityHelper helper = DataAbilityHelper.creator(getContext());
        try {
            ResultSet result = helper.query(AVStorage.Images.Media.EXTERNAL_DATA_ABILITY_URI, null, null);
            if (result == null) {
                preText.setVisibility(Component.VISIBLE);
            } else {
                preText.setVisibility(Component.HIDE);
            }
            while (result != null && result.goToNextRow()) {
                int mediaId = result.getInt(result.getColumnIndexForName(AVStorage.Images.Media.ID));
                Uri uri = Uri.appendEncodedPathToUri(AVStorage.Images.Media.EXTERNAL_DATA_ABILITY_URI, "" + mediaId);
                FileDescriptor fileDesc = helper.openFile(uri, "r");
                ImageSource.DecodingOptions decodingOpts = new ImageSource.DecodingOptions();
                decodingOpts.desiredSize = new Size(300, 300);
                ImageSource imageSource = ImageSource.create(fileDesc, null);
                PixelMap pixelMap = imageSource.createThumbnailPixelmap(decodingOpts, true);
                File file = new File(getExternalCacheDir() + "images/head" + Constant.User.getUserId() + ".jpg");
                if (!file.getParentFile().exists()) {
                    file.getParentFile().mkdirs();
                }
                if (!file.exists()) {
                    file.createNewFile();
                }
                FileInputStream fileInputStream = new FileInputStream(fileDesc);
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                int len = 0;
                byte[] bytes = new byte[512];
                while ((len = fileInputStream.read(bytes)) != -1) {
                    fileOutputStream.write(bytes, 0, len);
                }
                fileInputStream.close();
                fileOutputStream.close();
                Image img = new Image(PickHeaderAbilitySlice.this);
                img.setId(mediaId);
                img.setHeight(300);
                img.setWidth(300);
                img.setMarginTop(20);
                img.setMarginLeft(20);
                img.setPixelMap(pixelMap);
                img.setScaleMode(Image.ScaleMode.ZOOM_CENTER);
                img.setClickable(true);
                img.setClickedListener(component -> {
                    OkHttpClient client = new OkHttpClient();
                    MultipartBody.Builder requestBody = new MultipartBody
                            .Builder()
                            .setType(MultipartBody.FORM);//通过表单上传
                    RequestBody fileBody = RequestBody.create(MediaType.parse("image/*"), file);//上传的文件以及类型
                    MultipartBody multipartBody = requestBody
                            .addFormDataPart("file", file.getName(), fileBody)
                            .addFormDataPart("userId", Constant.User.getUserId() + "")
                            .build();
                    new ToastDialog(PickHeaderAbilitySlice.this)
                            .setText("文件大小" + file.getTotalSpace())
                            .setDuration(4000)
                            .show();
                    Request request = new Request.Builder()
                            .url(ServiceConfig.SERVICE_ROOT + "/picture/upload")
//                            .method("Post", multipartBody)
                            .post(multipartBody)
                            .build();
                    client.newBuilder().build().newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            getMainTaskDispatcher().syncDispatch(() ->
                                    new ToastDialog(PickHeaderAbilitySlice.this)
                                            .setText("请求失败，请检查网络连接是否正常")
                                            .setDuration(4000)
                                            .show());
                            e.printStackTrace();
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            String result = response.body().string();
                            getMainTaskDispatcher().syncDispatch(() ->
                                    new ToastDialog(PickHeaderAbilitySlice.this)
                                            .setText("text:" + result)
                                            .setDuration(4000)
                                            .show());
                            Constant.ChangeHeader = 1;
                            Constant.User.setUserHeader("user/us-" + Constant.User.getUserId() + ".jpg");
                            Intent intent = new Intent();
                            intent.setParam("modifyUserHeader", true);
                            intent.setFlags(Intent.FLAG_ABILITY_CLEAR_MISSION | Intent.FLAG_ABILITY_NEW_MISSION);
                            present(new SettingAbilitySlice(), intent);
                            PickHeaderAbilitySlice.this.terminate();
                        }
                    });
                });
                imgLayout.addComponent(img);
                imgList.add(mediaId);
            }
        } catch (DataAbilityRemoteException | IOException e) {
            e.printStackTrace();
        }
        if (imgList.size() > 0) {
            preText.setVisibility(Component.HIDE);
            text.setVisibility(Component.VISIBLE);
            text.setText("照片数量：" + imgList.size());
        } else {
            preText.setVisibility(Component.VISIBLE);
            preText.setText("No picture.");
            text.setVisibility(Component.HIDE);
        }
    }
}