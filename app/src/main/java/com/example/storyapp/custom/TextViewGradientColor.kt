package com.example.storyapp.custom

import android.content.Context
import android.graphics.LinearGradient
import android.graphics.Shader
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.res.ResourcesCompat
import com.example.storyapp.R
import kotlin.math.cos
import kotlin.math.sin

class TextViewGradientColor: AppCompatTextView {


    val colorOne = ResourcesCompat.getColor(resources, R.color.colorTextGradient_1, null)
    val colorTwo = ResourcesCompat.getColor(resources, R.color.colorTextGradient_2, null)
    val colors = arrayOf(colorOne, colorTwo)

    val width = paint.measureText(text.toString())


    constructor(context: Context) : super(context) {
        init()
    }
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {

        paint.shader = LinearGradient(
            0f,
            0f,
            width,
            (sin(Math.toRadians(45.0) * textSize).toFloat()),
            colorOne,
            colorTwo,
            Shader.TileMode.CLAMP
        )
        invalidate()
    }
}