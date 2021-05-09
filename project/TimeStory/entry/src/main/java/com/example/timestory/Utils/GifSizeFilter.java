package com.example.timestory.Utils;

import com.example.timestory.ResourceTable;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.filter.Filter;
import com.zhihu.matisse.model.IncapableCause;
import com.zhihu.matisse.model.Item;
import com.zhihu.matisse.utils.PhotoMetadataUtils;
import ohos.aafwk.ability.DataAbilityHelper;
import ohos.agp.utils.Point;
import ohos.app.Context;

import java.util.HashSet;
import java.util.Set;


public class GifSizeFilter extends Filter {

    private int mMinWidth;
    private int mMinHeight;
    private int mMaxSize;

    public GifSizeFilter(int minWidth, int minHeight, int maxSizeInBytes) {
        mMinWidth = minWidth;
        mMinHeight = minHeight;
        mMaxSize = maxSizeInBytes;
    }

    @Override
    public Set<MimeType> constraintTypes() {
        return new HashSet<MimeType>() {{
            add(MimeType.GIF);
        }};
    }

    @Override
    public IncapableCause filter(Context context, Item item) {
        if (!needFiltering(context, item)) {
            return null;
        }

        Point size = PhotoMetadataUtils.getBitmapBound(DataAbilityHelper.creator(context), item.getContentUri());
        if (size.getPointX() < mMinWidth || size.getPointY() < mMinHeight || item.size > mMaxSize) {
            try {
                return new IncapableCause(IncapableCause.DIALOG, context.getResourceManager().getElement(ResourceTable.String_error_gif).getString(mMinWidth,
                        String.valueOf(PhotoMetadataUtils.getSizeInMB(mMaxSize))));
            } catch (Exception e) {
                e.printStackTrace();
                return new IncapableCause(IncapableCause.DIALOG, String.format("x or y bound size should be at least %1$dpx and file size should be no more than %2$sM",
                        mMinWidth, String.valueOf(PhotoMetadataUtils.getSizeInMB(mMaxSize))));
            }
        }
        return null;
    }

}