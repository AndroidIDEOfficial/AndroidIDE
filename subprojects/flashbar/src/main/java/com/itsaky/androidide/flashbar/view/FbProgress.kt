package com.itsaky.androidide.flashbar.view

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.os.Build
import android.os.Build.VERSION
import android.os.Parcel
import android.os.Parcelable
import android.os.SystemClock
import android.provider.Settings
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import com.itsaky.androidide.flashbar.R
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.roundToInt

/** Forked from Todd Davies' Progress Wheel https://github.com/Todd-Davies/ProgressWheel */
class FbProgress : View {

  private val barLength = 16
  private val barMaxLength = 270
  private val pauseGrowingTime: Long = 200

  private var circleRadius = 28
  private var barWidth = 4
  private var rimWidth = 4
  private var fillRadius = false
  private var timeStartGrowing = 0.0
  private var barSpinCycleTime = 460.0
  private var barExtraLength = 0f
  private var barGrowingFromFront = true
  private var pausedTimeWithoutGrowing: Long = 0

  private var barColor = -0x56000000
  private var rimColor = 0x00FFFFFF

  private val barPaint = Paint()
  private val rimPaint = Paint()

  private var circleBounds = RectF()

  private var spinSpeed = 230.0f
  private var lastTimeAnimated: Long = 0
  private var linearProgress: Boolean = false

  private var progress = 0.0f
  private var targetProgress = 0.0f
  private var isSpinning = false
  private var shouldAnimate: Boolean = false

  private var callback: ProgressCallback? = null

  constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
    parseAttributes(context.obtainStyledAttributes(attrs, R.styleable.FbProgress))
    setAnimationEnabled()
  }

  constructor(context: Context) : super(context) {
    setAnimationEnabled()
  }

  override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec)

    val viewWidth = circleRadius + this.paddingLeft + this.paddingRight
    val viewHeight = circleRadius + this.paddingTop + this.paddingBottom

    val widthMode = MeasureSpec.getMode(widthMeasureSpec)
    val widthSize = MeasureSpec.getSize(widthMeasureSpec)
    val heightMode = MeasureSpec.getMode(heightMeasureSpec)
    val heightSize = MeasureSpec.getSize(heightMeasureSpec)

    val width: Int =
      when (widthMode) {
        MeasureSpec.EXACTLY -> widthSize
        MeasureSpec.AT_MOST -> viewWidth.coerceAtMost(widthSize)
        else -> viewWidth
      }

    val height: Int =
      when {
        heightMode == MeasureSpec.EXACTLY || widthMode == MeasureSpec.EXACTLY -> heightSize
        heightMode == MeasureSpec.AT_MOST -> viewHeight.coerceAtMost(heightSize)
        else -> viewHeight
      }

    setMeasuredDimension(width, height)
  }

  override fun onSizeChanged(w: Int, h: Int, oldW: Int, oldH: Int) {
    super.onSizeChanged(w, h, oldW, oldH)

    setupBounds(w, h)
    setupPaints()
    invalidate()
  }

  override fun onDraw(canvas: Canvas) {
    super.onDraw(canvas)

    canvas.drawArc(circleBounds, 360f, 360f, false, rimPaint)

    var mustInvalidate = false

    if (!shouldAnimate) {
      return
    }

    if (isSpinning) {
      mustInvalidate = true

      val deltaTime = SystemClock.uptimeMillis() - lastTimeAnimated
      val deltaNormalized = deltaTime * spinSpeed / 1000.0f

      updateBarLength(deltaTime)

      progress += deltaNormalized
      if (progress > 360) {
        progress -= 360f

        runCallback(-1.0f)
      }
      lastTimeAnimated = SystemClock.uptimeMillis()

      var from = progress - 90
      var length = barLength + barExtraLength

      if (isInEditMode) {
        from = 0f
        length = 135f
      }
      canvas.drawArc(circleBounds, from, length, false, barPaint)
    } else {
      val oldProgress = progress

      if (progress != targetProgress) {
        mustInvalidate = true

        val deltaTime = (SystemClock.uptimeMillis() - lastTimeAnimated).toFloat() / 1000
        val deltaNormalized = deltaTime * spinSpeed

        progress = (progress + deltaNormalized).coerceAtMost(targetProgress)
        lastTimeAnimated = SystemClock.uptimeMillis()
      }

      if (oldProgress != progress) {
        runCallback()
      }

      var offset = 0.0f
      var progress = this.progress
      if (!linearProgress) {
        val factor = 2.0f
        offset =
          (1.0f - (1.0f - this.progress / 360.0f).toDouble().pow((2.0f * factor).toDouble()))
            .toFloat() * 360.0f
        progress =
          (1.0f - (1.0f - this.progress / 360.0f).toDouble().pow(factor.toDouble())).toFloat() *
            360.0f
      }

      if (isInEditMode) {
        progress = 360f
      }

      canvas.drawArc(circleBounds, offset - 90, progress, false, barPaint)
    }

    if (mustInvalidate) {
      invalidate()
    }
  }

  override fun onVisibilityChanged(changedView: View, visibility: Int) {
    super.onVisibilityChanged(changedView, visibility)

    if (visibility == VISIBLE) {
      lastTimeAnimated = SystemClock.uptimeMillis()
    }
  }

  public override fun onSaveInstanceState(): Parcelable {
    val superState = super.onSaveInstanceState()

    val ss = WheelSavedState(superState)

    ss.mProgress = this.progress
    ss.mTargetProgress = this.targetProgress
    ss.isSpinning = this.isSpinning
    ss.spinSpeed = this.spinSpeed
    ss.barWidth = this.barWidth
    ss.barColor = this.barColor
    ss.rimWidth = this.rimWidth
    ss.rimColor = this.rimColor
    ss.circleRadius = this.circleRadius
    ss.linearProgress = this.linearProgress
    ss.fillRadius = this.fillRadius

    return ss
  }

  public override fun onRestoreInstanceState(state: Parcelable) {
    if (state !is WheelSavedState) {
      super.onRestoreInstanceState(state)
      return
    }

    super.onRestoreInstanceState(state.superState)

    this.progress = state.mProgress
    this.targetProgress = state.mTargetProgress
    this.isSpinning = state.isSpinning
    this.spinSpeed = state.spinSpeed
    this.barWidth = state.barWidth
    this.barColor = state.barColor
    this.rimWidth = state.rimWidth
    this.rimColor = state.rimColor
    this.circleRadius = state.circleRadius
    this.linearProgress = state.linearProgress
    this.fillRadius = state.fillRadius

    this.lastTimeAnimated = SystemClock.uptimeMillis()
  }

  fun resetCount() {
    progress = 0.0f
    targetProgress = 0.0f
    invalidate()
  }

  fun stopSpinning() {
    isSpinning = false
    progress = 0.0f
    targetProgress = 0.0f
    invalidate()
  }

  fun spin() {
    lastTimeAnimated = SystemClock.uptimeMillis()
    isSpinning = true
    invalidate()
  }

  fun setInstantProgress(update: Float) {
    var progressUpdate = update
    if (isSpinning) {
      progress = 0.0f
      isSpinning = false
    }

    if (progressUpdate > 1.0f) {
      progressUpdate -= 1.0f
    } else if (progressUpdate < 0) {
      progressUpdate = 0f
    }

    if (progressUpdate == targetProgress) {
      return
    }

    targetProgress = (progressUpdate * 360.0f).coerceAtMost(360.0f)
    progress = targetProgress
    lastTimeAnimated = SystemClock.uptimeMillis()
    invalidate()
  }

  fun getProgress(): Float {
    return if (isSpinning) -1f else progress / 360.0f
  }

  fun setProgress(update: Float) {
    var progress = update
    if (isSpinning) {
      this.progress = 0.0f
      isSpinning = false

      runCallback()
    }

    if (progress > 1.0f) {
      progress -= 1.0f
    } else if (progress < 0) {
      progress = 0f
    }

    if (progress == targetProgress) {
      return
    }

    if (this.progress == targetProgress) {
      lastTimeAnimated = SystemClock.uptimeMillis()
    }

    targetProgress = (progress * 360.0f).coerceAtMost(360.0f)

    invalidate()
  }

  fun setLinearProgress(isLinear: Boolean) {
    linearProgress = isLinear
    if (!isSpinning) {
      invalidate()
    }
  }

  fun getCircleRadius() = circleRadius

  fun setCircleRadius(circleRadius: Int) {
    this.circleRadius = circleRadius
    if (!isSpinning) {
      invalidate()
    }
  }

  fun getBarWidth() = barWidth

  fun setBarWidth(barWidth: Int) {
    this.barWidth = barWidth
    if (!isSpinning) {
      invalidate()
    }
  }

  fun getBarColor() = barColor

  fun setBarColor(barColor: Int) {
    this.barColor = barColor
    setupPaints()
    if (!isSpinning) {
      invalidate()
    }
  }

  fun getRimColor(): Int {
    return rimColor
  }

  fun setRimColor(rimColor: Int) {
    this.rimColor = rimColor
    setupPaints()
    if (!isSpinning) {
      invalidate()
    }
  }

  fun getSpinSpeed() = spinSpeed / 360.0f

  fun setSpinSpeed(spinSpeed: Float) {
    this.spinSpeed = spinSpeed * 360.0f
  }

  fun getRimWidth() = rimWidth

  fun setRimWidth(rimWidth: Int) {
    this.rimWidth = rimWidth
    if (!isSpinning) {
      invalidate()
    }
  }

  fun setCallback(progressCallback: ProgressCallback) {
    callback = progressCallback

    if (!isSpinning) {
      runCallback()
    }
  }

  private fun setAnimationEnabled() {
    val currentApiVersion = VERSION.SDK_INT

    val animationValue =
      if (currentApiVersion >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
        Settings.Global.getFloat(
          context.contentResolver,
          Settings.Global.ANIMATOR_DURATION_SCALE,
          1f
        )
      } else {
        Settings.System.getFloat(
          context.contentResolver,
          Settings.System.ANIMATOR_DURATION_SCALE,
          1f
        )
      }

    shouldAnimate = animationValue != 0f
  }

  private fun setupBounds(layoutWidth: Int, layoutHeight: Int) {
    val paddingTop = paddingTop
    val paddingBottom = paddingBottom
    val paddingLeft = paddingLeft
    val paddingRight = paddingRight

    if (!fillRadius) {
      val minValue =
        (layoutWidth - paddingLeft - paddingRight).coerceAtMost(
          layoutHeight - paddingBottom - paddingTop
        )

      val circleDiameter = minValue.coerceAtMost(circleRadius * 2 - barWidth * 2)

      val xOffset = (layoutWidth - paddingLeft - paddingRight - circleDiameter) / 2 + paddingLeft
      val yOffset = (layoutHeight - paddingTop - paddingBottom - circleDiameter) / 2 + paddingTop

      circleBounds =
        RectF(
          (xOffset + barWidth).toFloat(),
          (yOffset + barWidth).toFloat(),
          (xOffset + circleDiameter - barWidth).toFloat(),
          (yOffset + circleDiameter - barWidth).toFloat()
        )
    } else {
      circleBounds =
        RectF(
          (paddingLeft + barWidth).toFloat(),
          (paddingTop + barWidth).toFloat(),
          (layoutWidth - paddingRight - barWidth).toFloat(),
          (layoutHeight - paddingBottom - barWidth).toFloat()
        )
    }
  }

  private fun parseAttributes(a: TypedArray) {
    val metrics = context.resources.displayMetrics
    barWidth =
      TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, barWidth.toFloat(), metrics).toInt()
    rimWidth =
      TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, rimWidth.toFloat(), metrics).toInt()
    circleRadius =
      TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, circleRadius.toFloat(), metrics)
        .toInt()

    circleRadius =
      a.getDimension(R.styleable.FbProgress_fbp_circleRadius, circleRadius.toFloat()).toInt()

    fillRadius = a.getBoolean(R.styleable.FbProgress_fbp_fillRadius, false)

    barWidth = a.getDimension(R.styleable.FbProgress_fbp_barWidth, barWidth.toFloat()).toInt()

    rimWidth = a.getDimension(R.styleable.FbProgress_fbp_rimWidth, rimWidth.toFloat()).toInt()

    val baseSpinSpeed = a.getFloat(R.styleable.FbProgress_fbp_spinSpeed, spinSpeed / 360.0f)
    spinSpeed = baseSpinSpeed * 360

    barSpinCycleTime =
      a.getInt(R.styleable.FbProgress_fbp_barSpinCycleTime, barSpinCycleTime.toInt()).toDouble()

    barColor = a.getColor(R.styleable.FbProgress_fbp_barColor, barColor)

    rimColor = a.getColor(R.styleable.FbProgress_fbp_rimColor, rimColor)

    linearProgress = a.getBoolean(R.styleable.FbProgress_fbp_linearProgress, false)

    if (a.getBoolean(R.styleable.FbProgress_fbp_progressIndeterminate, false)) {
      spin()
    }

    a.recycle()
  }

  private fun setupPaints() {
    barPaint.color = barColor
    barPaint.isAntiAlias = true
    barPaint.style = Paint.Style.STROKE
    barPaint.strokeWidth = barWidth.toFloat()

    rimPaint.color = rimColor
    rimPaint.isAntiAlias = true
    rimPaint.style = Paint.Style.STROKE
    rimPaint.strokeWidth = rimWidth.toFloat()
  }

  private fun updateBarLength(deltaTimeInMilliSeconds: Long) {
    if (pausedTimeWithoutGrowing >= pauseGrowingTime) {
      timeStartGrowing += deltaTimeInMilliSeconds.toDouble()

      if (timeStartGrowing > barSpinCycleTime) {
        timeStartGrowing -= barSpinCycleTime
        pausedTimeWithoutGrowing = 0
        barGrowingFromFront = !barGrowingFromFront
      }

      val distance = cos((timeStartGrowing / barSpinCycleTime + 1) * Math.PI).toFloat() / 2 + 0.5f
      val destLength = (barMaxLength - barLength).toFloat()

      if (barGrowingFromFront) {
        barExtraLength = distance * destLength
      } else {
        val newLength = destLength * (1 - distance)
        progress += barExtraLength - newLength
        barExtraLength = newLength
      }
    } else {
      pausedTimeWithoutGrowing += deltaTimeInMilliSeconds
    }
  }

  private fun runCallback(value: Float) {
    if (callback != null) {
      callback!!.onProgressUpdate(value)
    }
  }

  private fun runCallback() {
    if (callback != null) {
      val normalizedProgress = (progress * 100 / 360.0f).roundToInt().toFloat() / 100
      callback!!.onProgressUpdate(normalizedProgress)
    }
  }

  interface ProgressCallback {
    fun onProgressUpdate(progress: Float)
  }

  internal class WheelSavedState : View.BaseSavedState {
    var mProgress: Float = 0.toFloat()
    var mTargetProgress: Float = 0.toFloat()
    var isSpinning: Boolean = false
    var spinSpeed: Float = 0.toFloat()
    var barWidth: Int = 0
    var barColor: Int = 0
    var rimWidth: Int = 0
    var rimColor: Int = 0
    var circleRadius: Int = 0
    var linearProgress: Boolean = false
    var fillRadius: Boolean = false

    constructor(superState: Parcelable?) : super(superState)

    private constructor(`in`: Parcel) : super(`in`) {
      this.mProgress = `in`.readFloat()
      this.mTargetProgress = `in`.readFloat()
      this.isSpinning = `in`.readByte().toInt() != 0
      this.spinSpeed = `in`.readFloat()
      this.barWidth = `in`.readInt()
      this.barColor = `in`.readInt()
      this.rimWidth = `in`.readInt()
      this.rimColor = `in`.readInt()
      this.circleRadius = `in`.readInt()
      this.linearProgress = `in`.readByte().toInt() != 0
      this.fillRadius = `in`.readByte().toInt() != 0
    }

    override fun writeToParcel(out: Parcel, flags: Int) {
      super.writeToParcel(out, flags)
      out.writeFloat(this.mProgress)
      out.writeFloat(this.mTargetProgress)
      out.writeByte((if (isSpinning) 1 else 0).toByte())
      out.writeFloat(this.spinSpeed)
      out.writeInt(this.barWidth)
      out.writeInt(this.barColor)
      out.writeInt(this.rimWidth)
      out.writeInt(this.rimColor)
      out.writeInt(this.circleRadius)
      out.writeByte((if (linearProgress) 1 else 0).toByte())
      out.writeByte((if (fillRadius) 1 else 0).toByte())
    }

    companion object {
      @Suppress("unused")
      @JvmField
      val CREATOR: Parcelable.Creator<WheelSavedState> =
        object : Parcelable.Creator<WheelSavedState> {
          override fun createFromParcel(`in`: Parcel): WheelSavedState {
            return WheelSavedState(`in`)
          }

          override fun newArray(size: Int): Array<WheelSavedState> {
            return newArray(size)
          }
        }
    }
  }
}
