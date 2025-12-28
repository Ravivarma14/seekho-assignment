package com.rvcn.seekhoanimeapplication.ui.home.adapters

import android.graphics.Canvas
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.abs

class CarouselScaleItemDecorator : RecyclerView.ItemDecoration() {

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val centerX = parent.width / 2f

        for (i in 0 until parent.childCount) {
            val child = parent.getChildAt(i)
            val childCenterX = (child.left + child.right) / 2f
            val distance = abs(centerX - childCenterX)
            val scale = 1f - (distance / parent.width)
                .coerceIn(0f, 0.3f)

            child.scaleX = 0.85f + scale
            child.scaleY = 0.85f + scale
        }
    }
}
