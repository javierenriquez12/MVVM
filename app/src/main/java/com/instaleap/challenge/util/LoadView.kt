package com.instaleap.challenge.util

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator.INFINITE
import android.animation.ValueAnimator.REVERSE
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import com.instaleap.challenge.R

class LoadView : View {

    companion object {
        val COLOR_PURPLE = Color.parseColor("#FFBB86FC")
        val COLOR_RED = Color.parseColor("#DB4437")
        val COLOR_YELLOW = Color.parseColor("#F4B400")
        val COLOR_GREEN = Color.parseColor("#0F9D58")

        const val CIRCLE_COUNT = 4
        const val DEFAULT_CIRCLE_RADIUS = 20f
        const val DEFAULT_CIRCLE_MARGIN = 20f
        const val DEFAULT_ANIM_DISTANCE = 50f
        const val DEFAULT_ANIM_DURATION = -1L
        const val DEFAULT_ANIM_DELAY = 150L
        const val DEFAULT_ANIM_INTERPOLATOR = 0
    }

    private var circleRadius = DEFAULT_CIRCLE_RADIUS
    private var circleMargin = DEFAULT_CIRCLE_MARGIN
    private var animDistance = DEFAULT_ANIM_DISTANCE
    private var animDuration = DEFAULT_ANIM_DURATION
    private var animDelay = DEFAULT_ANIM_DELAY
    private var animInterpolator = DEFAULT_ANIM_INTERPOLATOR
    private var colors = listOf(COLOR_PURPLE, COLOR_RED, COLOR_YELLOW, COLOR_GREEN)
    private val positions = mutableListOf(0f, 0f, 0f, 0f)
    private val animatorSet = AnimatorSet()

    private val paint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL
        color = Color.RED
    }

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        val typedArray =
            context.theme.obtainStyledAttributes(attrs, R.styleable.LoadView, 0, 0)
        circleRadius =
            typedArray.getDimension(R.styleable.LoadView_circleRadio, this.circleRadius)
        circleMargin =
            typedArray.getDimension(R.styleable.LoadView_circleMargin, this.circleMargin)
        animDistance =
            typedArray.getDimension(R.styleable.LoadView_animDistance, this.animDistance)
        animDuration = typedArray.getInt(
            R.styleable.LoadView_animDuration,
            this.animDuration.toInt()
        ).toLong()
        animDelay =
            typedArray.getInt(R.styleable.LoadView_animDelay, this.animDelay.toInt())
                .toLong()
        animInterpolator = typedArray.getInt(
            R.styleable.LoadView_animInterpolator,
            this.animInterpolator
        )
        typedArray.recycle()

        val animators = mutableListOf<Animator>()

        for (i in 0 until CIRCLE_COUNT) {
            animators.add(ObjectAnimator.ofFloat(0f, animDistance).apply {
                this.duration = animDuration
                this.startDelay = i * animDelay
                this.repeatCount = INFINITE
                this.repeatMode = REVERSE
                this.interpolator = AccelerateDecelerateInterpolator()
                this.addUpdateListener {
                    positions[i] = it.animatedValue as Float
                    invalidate()
                }
            })
        }

        animatorSet.playTogether(animators)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        var startPoint = width / 2 - ((CIRCLE_COUNT - 1) * (circleRadius + circleMargin / 2))

        for (i in 0 until CIRCLE_COUNT) {
            paint.color = colors[i]
            canvas.drawCircle(startPoint, height / 2f + positions[i], circleRadius, paint)
            startPoint += (circleRadius * 2) + circleMargin
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        animatorSet.start()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        animatorSet.end()
    }
}