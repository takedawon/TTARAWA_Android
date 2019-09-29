package com.seoul.ttarawa.ui.path

import android.content.Context
import android.graphics.Canvas
import android.graphics.DashPathEffect
import android.graphics.Paint
import android.util.SparseArray
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.util.forEach
import androidx.recyclerview.widget.RecyclerView
import com.seoul.ttarawa.R
import com.seoul.ttarawa.data.entity.WayPointEntity
import java.lang.ref.WeakReference

class PathItemDecoration(
    val context: Context,
    pathAdapter: PathAdapter
) : RecyclerView.ItemDecoration() {

    private val pathAdapterRef = WeakReference(pathAdapter)
    private val resources = context.resources
    private val textSize = resources.getDimensionPixelSize(
        R.dimen.path_bottom_sheet_left_time_text_size
    )
    private val textLeftSpace = resources.getDimensionPixelSize(
        R.dimen.path_bottom_sheet_left_time_text_left
    )
    private val textPaddingTop = resources.getDimensionPixelSize(
        R.dimen.path_bottom_sheet_left_time_text_padding_top
    )
    private val textPaddingBottom = resources.getDimensionPixelSize(
        R.dimen.path_bottom_sheet_left_time_text_padding_bottom
    )
    private val lineInterval = resources.getDimensionPixelSize(
        R.dimen.path_bottom_sheet_left_time_line_interval
    ).toFloat()

    private val adapterPositionToViews = SparseArray<View>()
    private val cachedDateTimeText = HashMap<String, String>()

    private data class TimeText(
        val dayNumber: Int,
        val text: String,
        val y: Float,
        val fixed: Boolean
    )

    private val paint = Paint().apply {
        style = Paint.Style.FILL
        textSize = this@PathItemDecoration.textSize.toFloat()
        color = ContextCompat.getColor(context, R.color.colorOnBackground)
        isAntiAlias = true
    }

    private val dashLinePaint = Paint().apply {
        style = Paint.Style.STROKE
        color = ContextCompat.getColor(context, R.color.border_on_bg)
        strokeWidth = resources.getDimensionPixelSize(
            R.dimen.path_bottom_sheet_left_time_line_width
        ).toFloat()
        isAntiAlias = true
    }

    private val fontMetrics = paint.fontMetrics
    private val textX = textLeftSpace.toFloat()
    private val lineX = textX + (paint.measureText("00:00") / 2F)

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val pathAdapter = pathAdapterRef.get() ?: return

        // sort child views by adapter position
        for (i in 0 until parent.childCount) {
            val view = parent.getChildAt(i)
            val position = parent.getChildAdapterPosition(view)
            if (position != RecyclerView.NO_POSITION && position < pathAdapter.itemCount) {
                adapterPositionToViews.put(position, view)
            }
        }

        var lastTimeText: TimeText? = null
        adapterPositionToViews.forEach { position, view ->
            val timeText = calcTimeText(position, view) ?: return@forEach

            lastTimeText?.let {
                if (timeText.dayNumber == it.dayNumber && timeText.text == it.text) return@forEach
            }
            lastTimeText = timeText

            c.drawText(
                timeText.text,
                textX,
                timeText.y,
                paint
            )

            // find next time session from all sessions
            var nextTimePosition = -1
            for (pos in position until pathAdapter.itemCount) {
                val path = getPath(pos) ?: break
                if (path.dayNumber != timeText.dayNumber) {
                    break
                }
                val time = extractPathTime(path)
                if (time != timeText.text) {
                    nextTimePosition = pos
                    break
                }
            }

            // no more different time sessions below, no need to draw line
            if (nextTimePosition < 0) return@forEach

            val offset = if (timeText.fixed) -view.top.toFloat() else 0F
            dashLinePaint.pathEffect = DashPathEffect(
                floatArrayOf(lineInterval, lineInterval),
                offset
            )

            // draw line to next time text if exists, otherwise draw line to the bottom
            calcTimeText(parent, nextTimePosition)?.let { nextTimeText ->
                c.drawLine(
                    lineX,
                    timeText.y + fontMetrics.bottom,
                    lineX,
                    nextTimeText.y + fontMetrics.top,
                    dashLinePaint
                )
            } ?: run {
                c.drawLine(
                    lineX,
                    timeText.y + fontMetrics.bottom,
                    lineX,
                    c.height.toFloat(),
                    dashLinePaint
                )
            }
        }

        adapterPositionToViews.clear()
    }

    private fun calcTimeText(parent: RecyclerView, position: Int): TimeText? {
        val view = parent.findViewHolderForAdapterPosition(position)?.itemView ?: return null
        return calcTimeText(position, view)
    }

    private fun calcTimeText(position: Int, view: View): TimeText? {
        val path = getPath(position) ?: return null
        val nextPath = getPath(position + 1)
        val time = extractPathTime(path)
        val nextTime = nextPath?.let {
            extractPathTime(it)
        }

        var y = view.top.coerceAtLeast(0) + textPaddingTop + textSize
        if (path.dayNumber != nextPath?.dayNumber || time != nextTime) {
            y = y.coerceAtMost(view.bottom - textPaddingBottom)
        }
        val fixed = y == textPaddingTop + textSize
        return TimeText(path.dayNumber, time, y.toFloat(), fixed)
    }

    private fun getPath(position: Int): WayPointEntity? {
        val pathAdapter: PathAdapter = pathAdapterRef.get() ?: return null

        if (position < 0 || position >= pathAdapter.itemCount) {
            return null
        }
        return pathAdapter.getItem(position)
    }

    private fun extractPathTime(wayPoint: WayPointEntity): String {
        return cachedDateTimeText[wayPoint.startTime]
            ?: wayPoint.startTime.also {
                cachedDateTimeText[wayPoint.startTime] = it
            }
    }
}