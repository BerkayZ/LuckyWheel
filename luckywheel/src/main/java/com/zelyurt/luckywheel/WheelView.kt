package com.zelyurt.luckywheel

import android.animation.Animator
import android.content.Context
import android.graphics.*
import android.text.TextPaint
import android.text.TextUtils
import android.util.AttributeSet
import android.view.View
import android.view.animation.DecelerateInterpolator
import androidx.core.content.res.ResourcesCompat

internal class WheelView:View {
    private var range = RectF()
    private lateinit var archPaint:Paint
    private lateinit var textPaint:Paint
    private var padding:Int = 0
    private var radius:Int = 0
    private var center:Int = 0
    private var mWheelBackground:Int = 0
    private lateinit var mWheelItems:List<WheelItem>
    private var mTextPaint = TextPaint()
    private val bounds = Rect()
    private var textColor: Int = Color.WHITE
    private var textSize: Float = 30F
    private var mOnLuckyWheelReachTheTarget:OnLuckyWheelReachTheTarget?= null
    private lateinit var onRotationListener:OnRotationListener
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
            context,
            attrs,
            defStyleAttr
    )
    private fun initComponents() {
        archPaint = Paint()
        archPaint.isAntiAlias = true
        archPaint.isDither = true
        textPaint = Paint()
        textPaint.color = textColor
        textPaint.isAntiAlias = true
        textPaint.isDither = true
        textPaint.textSize = textSize
        range = RectF(padding.toFloat(), padding.toFloat(), (padding + radius).toFloat(), (padding + radius).toFloat())
    }

    private fun getAngleOfIndexTarget(target: Int):Float {
        return ((360 / mWheelItems.size) * target).toFloat()
    }

    fun setTextColor(tColor: Int){
        textColor = tColor
        invalidate()
    }

    fun setTextSize(tSize: Float){
        textSize = tSize
        invalidate()
    }

    fun setWheelBackgoundWheel(wheelBackground: Int) {
        mWheelBackground = wheelBackground
        invalidate()
    }

    fun setWheelListener(onLuckyWheelReachTheTarget: OnLuckyWheelReachTheTarget) {
        mOnLuckyWheelReachTheTarget = onLuckyWheelReachTheTarget
    }

    fun addWheelItems(wheelItems: List<WheelItem>) {
        mWheelItems = wheelItems
        invalidate()
    }

    private fun drawWheelBackground(canvas: Canvas) {
        val backgroundPainter = Paint()
        backgroundPainter.isAntiAlias = true
        backgroundPainter.isDither = true
        backgroundPainter.color = mWheelBackground
        canvas.drawCircle(center.toFloat(), center.toFloat(), center.toFloat(), backgroundPainter)
    }

    fun rotateWheelToTarget(target: Int) {
        val wheelItemCenter = 270 - getAngleOfIndexTarget(target) + (360 / mWheelItems.size) / 2
        val defaultRotationTime = 9000
        animate().setInterpolator(DecelerateInterpolator())
            .setDuration(defaultRotationTime.toLong())
            .rotation((360 * 15) + wheelItemCenter)
            .setListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator) {}
                override fun onAnimationEnd(animation: Animator) {
                    mOnLuckyWheelReachTheTarget?.onReachTarget()
                    onRotationListener.onFinishRotation()
                    clearAnimation()
                }
                override fun onAnimationCancel(animation: Animator) {}
                override fun onAnimationRepeat(animation: Animator) {}
            })
            .start()
    }

    fun rotateWheelRandom(){
        resetRotationLocationToZeroAngle((1..mWheelItems.size).random())
    }

    fun resetRotationLocationToZeroAngle(target: Int) {
        animate().setDuration(0).rotation(0F).setListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {}
            override fun onAnimationEnd(animation: Animator) {
                rotateWheelToTarget(target)
                clearAnimation()
            }
            override fun onAnimationCancel(animation: Animator) {}
            override fun onAnimationRepeat(animation: Animator) {}
        })
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawWheelBackground(canvas)
        initComponents()
        var tempAngle = 0F
        val sweepAngle = (360 / mWheelItems.size).toFloat()
        var textAngle = sweepAngle /2
        mTextPaint.set(this.textPaint)
        val cx = height / 2F
        val cy = width / 2F
        val rad = cx.coerceAtMost(cy);
        for (i in mWheelItems.indices) {
            archPaint.color = mWheelItems[i].color
            canvas.drawArc(range, tempAngle, sweepAngle, true, archPaint)
            mTextPaint.getTextBounds(mWheelItems[i].text, 0, mWheelItems[i].text.length, bounds)
            val textHeight: Int = bounds.height() / 2
            val x = cx - rad + 25
            val y = cy + textHeight
            val textWidth: Float = rad - 5 * 10
            val item = TextUtils.ellipsize(mWheelItems[i].text, mTextPaint, textWidth, TextUtils.TruncateAt.END)
            canvas.save()
            canvas.rotate(textAngle + 180, cx, cy)
            canvas.drawText(item.toString(), x, y, textPaint)
            canvas.restore()
            textAngle += sweepAngle
            tempAngle += sweepAngle
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = measuredWidth.coerceAtMost(measuredHeight)
        val defaultPadding = 5
        padding = if (paddingLeft != 0) defaultPadding else paddingLeft
        radius = width - padding * 2
        center = width / 2
        setMeasuredDimension(width, width)
    }

    fun setOnRotationListener(onRotationListener: OnRotationListener) {
        this.onRotationListener = onRotationListener
    }
}
