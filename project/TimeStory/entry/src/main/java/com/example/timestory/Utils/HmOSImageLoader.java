package com.example.timestory.Utils;

import com.example.timestory.constant.Constant;
import ohos.aafwk.ability.AbilitySlice;
import ohos.agp.components.Image;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.media.image.ImageSource;
import ohos.media.image.PixelMap;
import ohos.media.image.common.Size;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.io.InputStream;

public class HmOSImageLoader {
    private final static HiLogLabel LABEL_LOG = new HiLogLabel(HiLog.LOG_APP, 0, "HmOSImageLoader");
    Image image;
    String url;
    int defImage;
    AbilitySlice abilitySlice;

    private HmOSImageLoader(AbilitySlice abilitySlice) {
        this.abilitySlice = abilitySlice;
    }

    public static HmOSImageLoader with(AbilitySlice abilitySlice) {
        return new HmOSImageLoader(abilitySlice);
    }

    public HmOSImageLoader load(String url) {
        this.url = url;
        return this;
    }

    public HmOSImageLoader def(int defImage) {
        this.defImage = defImage;
        return this;
    }

    public void into(Image image) {
        this.image = image;
        start();
    }

    private void start() {
        if (Constant.eventPics.containsKey(url)) {
            abilitySlice.getUITaskDispatcher().asyncDispatch(new Runnable() {
                @Override
                public void run() {
                    //展示到组件上
                    image.setPixelMap(Constant.eventPics.get(url));
                }
            });
        }
        if (defImage != 0) {
            image.setPixelMap(defImage);
        }
        Request request = new Request.Builder().url(url).get().build();
        new Thread(() -> {
            OkHttpClient okHttpClient = new OkHttpClient();
            try {
//                PixelMap.InitializationOptions initializationOptions = new PixelMap.InitializationOptions();
//                initializationOptions.size = new Size(imageWidth, imageHeight);
                //异步网络请求
                Response execute = okHttpClient.newCall(request).execute();
                //获取流
                InputStream inputStream = execute.body().byteStream();
                //利用鸿蒙api将流解码为图片源
                ImageSource imageSource = ImageSource.create(inputStream, null);
                //根据图片源创建位图
                PixelMap pixelMap = imageSource.createPixelmap(null);

                abilitySlice.getUITaskDispatcher().asyncDispatch(new Runnable() {
                    @Override
                    public void run() {
                        //展示到组件上
                        image.setPixelMap(pixelMap);
                        //释放位图
                        Constant.eventPics.put(url, pixelMap);
                    }
                });
            } catch (IOException e) {
                HiLog.error(LABEL_LOG, " ----- " + e.getMessage());
                e.printStackTrace();
            }
        }).start();
    }
}