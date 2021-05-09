package com.example.timestory.Utils;

import ohos.agp.components.AttrSet;
import ohos.agp.components.Image;
import ohos.agp.render.PixelMapHolder;
import ohos.agp.utils.RectFloat;
import ohos.app.Context;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.media.image.ImageSource;
import ohos.media.image.PixelMap;
import ohos.media.image.common.PixelFormat;
import ohos.media.image.common.Rect;
import ohos.media.image.common.Size;

import java.io.InputStream;

/**
 * @author PengHuAnZhi
 */
public class RoundImage extends Image {
    private static final HiLogLabel LABEL = new HiLogLabel(HiLog.DEBUG, 0, "RoundImage");
    /**
     * 像素图片持有者
     */
    private PixelMapHolder pixelMapHolder;
    /**
     * 目标区域
     */
    private RectFloat rectDst;
    /**
     * 源区域
     */
    private RectFloat rectSrc;

    public RoundImage(Context context) {
        this(context, null);

    }

    public RoundImage(Context context, AttrSet attrSet) {
        this(context, attrSet, null);
    }

    /**
     * 加载包含该控件的xml布局，会执行该构造函数
     *
     * @param context
     * @param attrSet
     * @param styleName
     */
    public RoundImage(Context context, AttrSet attrSet, String styleName) {
        super(context, attrSet, styleName);
        HiLog.error(LABEL, "RoundImage");
    }


    public void onRoundRectDraw(int radius) {
        //添加绘制任务
        this.addDrawTask((view, canvas) -> {
            if (pixelMapHolder == null) {
                return;
            }
            synchronized (pixelMapHolder) {
                //给目标区域赋值，宽度和高度取自xml配置文件中的属性
                rectDst = new RectFloat(0, 0, getWidth(), getHeight());
                //绘制圆角图片
                canvas.drawPixelMapHolderRoundRectShape(pixelMapHolder, rectSrc, rectDst, radius, radius);
                pixelMapHolder = null;
            }
        });
    }

    //使用canvas绘制圆形
    private void onCircleDraw() {
        //添加绘制任务，自定义组件的核心api调用，该接口的参数为Component下的DrawTask接口
        this.addDrawTask((view, canvas) -> {
            if (pixelMapHolder == null) {
                return;
            }
            synchronized (pixelMapHolder) {
                //给目标区域赋值，宽度和高度取自xml配置文件中的属性
                rectDst = new RectFloat(0, 0, getWidth(), getHeight());
                //使用canvas绘制输出圆角矩形的位图，该方法第4个参数和第5个参数为radios参数，
                // 绘制图片，必须把图片的宽度和高度先设置成一样，然后把它们设置为图片宽度或者高度一半时则绘制的为圆形
                canvas.drawPixelMapHolderRoundRectShape(pixelMapHolder, rectSrc, rectDst, getWidth() / 2, getHeight() / 2);
                pixelMapHolder = null;
            }
        });
    }


    /**
     * 获取原有Image中的位图资源后重新检验绘制该组件
     *
     * @param pixelMap
     */
    private void putPixelMap(PixelMap pixelMap) {
        if (pixelMap != null) {
            rectSrc = new RectFloat(0, 0, pixelMap.getImageInfo().size.width, pixelMap.getImageInfo().size.height);
            pixelMapHolder = new PixelMapHolder(pixelMap);
            invalidate();//重新检验该组件
        } else {
            pixelMapHolder = null;
            setPixelMap(null);
        }
    }


    /**
     * 通过资源ID获取位图对象
     **/
    private PixelMap getPixelMap(int resId) {
        InputStream drawableInputStream = null;
        try {
            drawableInputStream = getResourceManager().getResource(resId);
            ImageSource.SourceOptions sourceOptions = new ImageSource.SourceOptions();
            sourceOptions.formatHint = "image/png";
            ImageSource imageSource = ImageSource.create(drawableInputStream, null);
            ImageSource.DecodingOptions decodingOptions = new ImageSource.DecodingOptions();
            decodingOptions.desiredSize = new Size(0, 0);
            decodingOptions.desiredRegion = new Rect(0, 0, 0, 0);
            decodingOptions.desiredPixelFormat = PixelFormat.ARGB_8888;
            PixelMap pixelMap = imageSource.createPixelmap(decodingOptions);
            return pixelMap;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (drawableInputStream != null) {
                    drawableInputStream.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 对外调用的api，设置圆形图片方法
     *
     * @param resId
     */
    public void setPixelMapAndCircle(int resId) {
        PixelMap pixelMap = getPixelMap(resId);
        putPixelMap(pixelMap);
        onCircleDraw();
    }

    /**
     * 对外调用的api，设置圆形图片方法
     *
     */
    public void setPixelMapAndCircle(PixelMap pixelMap) {
        putPixelMap(pixelMap);
        onCircleDraw();
    }



    /**
     * 对外调用的api，设置圆角图片方法
     *
     * @param resId
     * @param radius
     */
    public void setPixelMapAndRoundRect(int resId, int radius) {
        PixelMap pixelMap = getPixelMap(resId);
        putPixelMap(pixelMap);
        onRoundRectDraw(radius);
    }
}
