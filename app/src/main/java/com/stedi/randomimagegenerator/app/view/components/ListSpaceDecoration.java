package com.stedi.randomimagegenerator.app.view.components;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class ListSpaceDecoration extends RecyclerView.ItemDecoration {
    private final int vSpace;
    private final int lrSpace;

    public ListSpaceDecoration(int vSpace, int lrSpace) {
        this.vSpace = vSpace;
        this.lrSpace = lrSpace;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (parent.getChildAdapterPosition(view) == 0)
            outRect.top = vSpace;
        outRect.bottom = vSpace;
        outRect.left = lrSpace;
        outRect.right = lrSpace;
    }
}
