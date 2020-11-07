package com.weventer.app.imageloader.`class`

import android.content.Context
import android.util.AttributeSet

class ScaleImageView(context: Context, attrs:AttributeSet?=null) : androidx.appcompat.widget.AppCompatImageView(context, attrs) {


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        try {
            val drawable = drawable
            if (drawable == null) {
                setMeasuredDimension(0, 0)
            } else {
                val measuredWidth = MeasureSpec.getSize(widthMeasureSpec)
                val measuredHeight = MeasureSpec.getSize(heightMeasureSpec)
                if (measuredHeight == 0 && measuredWidth == 0) { //Height and width set to wrap_content
                    setMeasuredDimension(measuredWidth, measuredHeight)
                } else if (measuredHeight == 0) { //Height set to wrap_content
                    val height = measuredWidth * drawable.intrinsicHeight / drawable.intrinsicWidth
                    setMeasuredDimension(measuredWidth, height)
                } else if (measuredWidth == 0) { //Width set to wrap_content
                    val width = measuredHeight * drawable.intrinsicWidth / drawable.intrinsicHeight
                    setMeasuredDimension(width, measuredHeight)
                } else { //Width and height are explicitly set (either to match_parent or to exact value)
                    setMeasuredDimension(measuredWidth, measuredHeight)
                }
            }
        } catch (e: Exception) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        }
    }

}