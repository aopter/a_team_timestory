package com.example.timestory.Utils;

import com.example.timestory.Utils.decoder.GifDecoder;
import ohos.agp.animation.Animator;
import ohos.agp.animation.AnimatorValue;
import ohos.agp.components.AttrSet;
import ohos.agp.components.Image;
import ohos.app.Context;
import ohos.global.resource.NotExistException;
import ohos.global.resource.RawFileEntry;
import ohos.global.resource.ResourceManager;
import ohos.global.resource.WrongTypeException;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.media.image.ImageSource;
import ohos.media.image.PixelMap;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author PengHuAnZhi
 * @createTime 2021/3/2 19:48
 * @projectName TimeStory
 * @className Gif.java
 * @description TODO
 */
public class Gif extends Image {

    private List<PixelMap> pixelMapList = new ArrayList<>();

    private static HiLogLabel label = new HiLogLabel(HiLog.LOG_APP, 0x000110, "GifImage");
    // 动画
    private AnimatorValue animatorValue;
    private GifDecoder gifDecoder;
    private int duration;

    public Gif(Context context) {
        super(context);
    }

    public Gif(Context context, AttrSet attrSet) throws IOException, NotExistException, WrongTypeException {
        super(context, attrSet);
        gifDecoder = new GifDecoder();
        ResourceManager resourceManager = context.getResourceManager();
        ImageSource.SourceOptions sourceOptions = new ImageSource.SourceOptions();
        sourceOptions.formatHint = "image/gif";

        if (attrSet.getAttr("image_src").isPresent()) {
            String id = attrSet.getAttr("image_src").get().getStringValue();
            Pattern pattern = Pattern.compile("[^0-9]");
            Matcher matcher = pattern.matcher(id);
            String all = matcher.replaceAll("");
            RawFileEntry rawFileEntry = resourceManager.getRawFileEntry(resourceManager.getMediaPath(Integer.parseInt(all)));
            ImageSource imageSource = ImageSource.create(rawFileEntry.openRawFile(), sourceOptions);
            gifDecoder.read(rawFileEntry.openRawFile(), (int) rawFileEntry.openRawFileDescriptor().getFileSize());

            if (imageSource != null) {
                init(imageSource);
            }
        } else {
            invalidate();
        }

    }


    private int i;
    // 动画侦听函数
    private final AnimatorValue.ValueUpdateListener mAnimatorUpdateListener
            = (animatorValue, v) -> {
        setPixelMap(pixelMapList.get((int) (v * pixelMapList.size())));
        invalidate();
    };

    private void init(ImageSource imageSource) {
        duration = 0;
        //  invalidate();
        ImageSource.DecodingOptions decodingOptions = new ImageSource.DecodingOptions();
        decodingOptions.allowPartialImage = true;
        i = 1;
        if (gifDecoder.getFrameCount() > 0) {
            while (i < gifDecoder.getFrameCount()) {
                pixelMapList.add(imageSource.createPixelmap(i, decodingOptions));
                duration += gifDecoder.getDelay(i);
                i++;
            }
        } else {
            while (imageSource.createPixelmap(i, decodingOptions) != null) {
                pixelMapList.add(imageSource.createPixelmap(i, decodingOptions));
                duration += gifDecoder.getDelay(i);
                i++;
            }
        }
        // 启动动画
        animatorValue = new AnimatorValue();
        animatorValue.setCurveType(Animator.CurveType.LINEAR);
        animatorValue.setLoopedCount(Animator.INFINITE);
        animatorValue.setDelay(0);
        animatorValue.setDuration(duration == 0 ? 100000 : duration);
        animatorValue.setValueUpdateListener(mAnimatorUpdateListener);
        animatorValue.start();
    }
}