package com.zelyurt.luckywheel

import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.annotation.AttrRes
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import java.util.Collections.rotate

class LuckyWheel:FrameLayout, View.OnTouchListener, OnRotationListener {
    private lateinit var wheelView:WheelView
    private lateinit var arrow:ImageView
    private var target = -1
    private var isRotate = false

    constructor(@NonNull context: Context, @Nullable attrs: AttributeSet) : super(context, attrs) {
        initComponent()
        applyAttribute(attrs)
    }
    constructor(@NonNull context: Context, @Nullable attrs: AttributeSet, @AttrRes defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initComponent()
        applyAttribute(attrs)
    }
    private fun initComponent() {
        inflate(context, R.layout.luckywheel, this)
        setOnTouchListener(this)
        wheelView = findViewById(R.id.wv_main_wheel)
        wheelView.setOnRotationListener(this)
        arrow = findViewById(R.id.iv_arrow)
    }

    fun addWheelItems(wheelItems: List<WheelItem>) {
        wheelView.addWheelItems(wheelItems)
    }

    private fun applyAttribute(attrs: AttributeSet) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.LuckyWheel, 0, 0)
        try {
            val backgroundColor = typedArray.getColor(R.styleable.LuckyWheel_background_color, Color.GREEN)
            val arrowImage = typedArray.getResourceId(R.styleable.LuckyWheel_arrow_image, R.drawable.arrow)
            val textColor = typedArray.getColor(R.styleable.LuckyWheel_text_color, Color.WHITE)
            val textSize = typedArray.getFloat(R.styleable.LuckyWheel_text_size, 30F)
            wheelView.setWheelBackgoundWheel(backgroundColor)
            wheelView.setTextColor(textColor)
            wheelView.setTextSize(textSize)
            arrow.setImageResource(arrowImage)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        typedArray.recycle()
    }

    fun setLuckyWheelReachTheTarget(onLuckyWheelReachTheTarget: OnLuckyWheelReachTheTarget) {
        wheelView.setWheelListener(onLuckyWheelReachTheTarget)
    }

    fun setTarget(target: Int) {
        this.target = target
    }

    fun rotateWheelTo(number: Int) {
        isRotate = true
        wheelView.resetRotationLocationToZeroAngle(number)
    }

    fun rotateToRandom(){
        isRotate = true
        wheelView.rotateWheelRandom()
    }

    override fun onFinishRotation() {
        isRotate = false
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        return true
    }
}