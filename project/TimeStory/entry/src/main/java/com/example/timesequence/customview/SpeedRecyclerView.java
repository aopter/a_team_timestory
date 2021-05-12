package com.example.timesequence.customview;

import ohos.agp.components.AttrSet;
import ohos.agp.components.ListContainer;
import ohos.app.Context;

/**
 * 控制fling速度
 */
public class SpeedRecyclerView extends ListContainer {
    private static final float FLING_SCALE_DOWN_FACTOR = 0.5f; // 减速因子
    private static final int FLING_MAX_VELOCITY = 8000; // 最大顺时滑动速度

    public SpeedRecyclerView(Context context) {
        super(context);
    }

    public SpeedRecyclerView(Context context, AttrSet attrSet) {
        super(context, attrSet);
    }

    public SpeedRecyclerView(Context context, AttrSet attrSet, String styleName) {
        super(context, attrSet, styleName);
    }

    @Override
    public void setContentOffSet(int startOffset, int endOffset) {
        startOffset = solveVelocity(startOffset);
        endOffset = solveVelocity(endOffset);
        super.setContentOffSet(startOffset, endOffset);
    }

    private int solveVelocity(int velocity) {
        if (velocity > 0) {
            return Math.min(velocity, FLING_MAX_VELOCITY);
        } else {
            return Math.max(velocity, -FLING_MAX_VELOCITY);
        }
    }
}
