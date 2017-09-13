package com.stedi.randomimagegenerator.app.view.components;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class SpaceItemDecoration extends RecyclerView.ItemDecoration {
    private final int verticalSpace;
    private final int lrSpace;

    public SpaceItemDecoration(int verticalSpace, int lrSpace) {
        this.verticalSpace = verticalSpace;
        this.lrSpace = lrSpace;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (parent.getChildAdapterPosition(view) == 0)
            outRect.top = verticalSpace;
        outRect.bottom = verticalSpace;
        outRect.left = lrSpace;
        outRect.right = lrSpace;
    }
}
