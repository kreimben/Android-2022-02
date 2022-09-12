package com.example.ch2

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View


class MyView : View {
    private var rect = Rect(10, 10, 110, 110)

    private val paint = Paint()
    var drawing_mode: String = ""

    constructor(context: Context) : super(context) {
        init(null, 0)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        init(attrs, defStyle)
    }

    private fun init(attrs: AttributeSet?, defStyle: Int) {
        // Load attributes
        val a = context.obtainStyledAttributes(
            attrs, R.styleable.MyView, defStyle, 0
        )
        a.recycle()
    }

    private var cx: Float = 0F
    private var cy: Float = 0F

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        paint.color = Color.BLUE
        when (this.drawing_mode) {
            "rect" -> {
                canvas.drawRect(rect, paint)
            }
            "circle" -> {
                println("Drawing circle!")
                canvas.drawCircle(this.cx, this.cy, 100F, paint)
            }
            else -> {
                println("Why this is called?")
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event?.action == MotionEvent.ACTION_DOWN ||
            event?.action == MotionEvent.ACTION_MOVE
        ) {
            if (this.drawing_mode == "rect") {
                /* Deal with action */
                rect.left = event.x.toInt()
                rect.top = event.y.toInt()
                rect.right = rect.left + 100
                rect.bottom = rect.top + 100
            } else if (this.drawing_mode == "circle") {
                println("circle click")
                this.cx = event.x.toFloat()
                this.cy = event.y.toFloat()
            }

            /*
            Must call `invalidate()` to re-draw object.
            NOT `onDraw()`!!!!!!!
             */
            invalidate()

            // If I not return true, This function will be executing ever.
            return true
        }
        return super.onTouchEvent(event)
    }
}