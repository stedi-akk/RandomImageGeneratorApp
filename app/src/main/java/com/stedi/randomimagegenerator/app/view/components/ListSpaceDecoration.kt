package com.stedi.randomimagegenerator.app.view.components

import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View

class ListSpaceDecoration(private val vSpace: Int, private val lrSpace: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        if (parent.getChildAdapterPosition(view) == 0) {
            outRect.top = vSpace
        }
        outRect.bottom = vSpace
        outRect.left = lrSpace
        outRect.right = lrSpace
    }
}