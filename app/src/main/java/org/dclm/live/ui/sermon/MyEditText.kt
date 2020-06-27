package org.dclm.live.ui.sermon

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText

class MyEditText : AppCompatEditText {
    private var mRect: Rect = Rect()
    private var mPaint: Paint = Paint()


    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {

        // define the style of line
        mPaint.style = Paint.Style.STROKE
        // define the color of line
        mPaint.color = Color.BLACK
    }

    constructor(context: Context) : super(context)

    override fun onDraw(canvas: Canvas) {
        val height = height
        val lHeight = lineHeight
        val left = left
        val right = right
        val paddingTop = paddingTop
        val paddingBottom = paddingBottom
        val paddingLeft = paddingLeft
        val paddingRight = paddingRight
        //int count = (height-paddingTop-paddingBottom);
        // the number of line
        var count = height / lHeight
        if (lineCount > count) {
            // for long text with scrolling
            count = lineCount
        }
        val r = mRect
        val paint = mPaint

        // first line
       var baseline = getLineBounds(0, r)


        // draw the remaining lines.
        for (i in 0 until count) {
            canvas.drawLine( r.left.toFloat(), baseline + 1.toFloat(), r.right.toFloat(), baseline + 1.toFloat(), paint)
            // next line
            baseline += lineHeight
        }



/*
        for (int i = 0; i < count*2; i++) {
            int baseline = lHeight * (i+1) + paddingTop;

            canvas.drawLine(left+paddingLeft, baseline, right-paddingRight, baseline, mPaint);
        }*/super.onDraw(canvas)
    }

    fun number(): Int {
        return lineCount
    }
}