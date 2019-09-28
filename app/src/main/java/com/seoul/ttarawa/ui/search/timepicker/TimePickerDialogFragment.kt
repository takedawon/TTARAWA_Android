package com.seoul.ttarawa.ui.search.timepicker

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.TimePicker
import com.seoul.ttarawa.R
import com.seoul.ttarawa.base.BaseDialogFragment
import com.seoul.ttarawa.base.abstract.CustomAnimationListener
import com.seoul.ttarawa.databinding.DialogFragmentTimePickerBinding
import com.seoul.ttarawa.ext.*
import com.seoul.ttarawa.ui.search.SearchActivity
import org.jetbrains.anko.support.v4.px2dip
import timber.log.Timber

class TimePickerDialogFragment : BaseDialogFragment<DialogFragmentTimePickerBinding>(
    R.layout.dialog_fragment_time_picker
) {
    private lateinit var tpStartEnterAnim: Animation
    private lateinit var tpStartExitAnim: Animation
    private lateinit var tpEndEnterAnim: Animation
    private lateinit var tpEndExitAnim: Animation

    /**
     * 시작시간
     */
    private var startTime: String = ""
    /**
     * 종료시간
     */
    private var endTime: String = ""
    /**
     * 시작모드, 종료모드
     */
    private var mode = 0

    private var onConfirmCallback: ((startTime: String, endTime: String) -> Unit)? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        onConfirmCallback = { startTime, endTime ->
            (context as? SearchActivity)?.setTime(startTime, endTime)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getBundleData(arguments)
    }

    private fun getBundleData(bundle: Bundle?) {
        mode = bundle?.getInt(EXTRA_MODE) ?: MODE_START
        val currentTime = getCurrentDay("HH:mm")
        startTime = bundle?.getString(EXTRA_START_TIME, currentTime) ?: currentTime
        if (startTime.isEmpty()) {
            startTime = currentTime
        }
        endTime = bundle?.getString(EXTRA_END_TIME, currentTime) ?: currentTime
        if (endTime.isEmpty()) {
            endTime = currentTime
        }
        Timber.e("$startTime$endTime")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(EXTRA_MODE, mode)
        outState.putString(EXTRA_START_TIME, startTime)
        outState.putString(EXTRA_END_TIME, endTime)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        savedInstanceState?.let {
            getBundleData(savedInstanceState)
        }

        initView()
    }

    private fun initView() {
        bind {
            dialog?.window?.decorView?.setBackgroundResource(android.R.color.transparent)

            setTime(binding.tpStart, startTime)
            setTime(binding.tpEnd, endTime)
            if (mode == MODE_START) {
                changeStartMode(isAnim = false)
            } else {
                changeEndMode(isAnim = false)
            }

            initAnimation()

            btnTimePickerConfirm click {
                if (mode == MODE_START) {
                    changeEndMode()
                } else {
                    // 저장
                    onConfirmCallback?.invoke(startTime, endTime)
                    dismiss()
                }
            }
            btnTimePickerCancel click {
                if (mode == MODE_END) {
                    changeStartMode()
                } else {
                    dismiss()
                }
            }

            btnStartTime click {
                changeStartMode()
            }

            btnEndTime click {
                changeEndMode()
            }

            tpStart.setOnTimeChangedListener { _: TimePicker?, hourOfDay: Int, minute: Int ->
                startTime = "${hourOfDay.getTextFormattedTime()}:${minute.getTextFormattedTime()}"
            }

            tpEnd.setOnTimeChangedListener { _, hourOfDay, minute ->
                endTime = "${hourOfDay.getTextFormattedTime()}:${minute.getTextFormattedTime()}"
            }
        }
    }

    private fun initAnimation() {
        val animationListener = object : CustomAnimationListener() {
            override fun onAnimationEnd(animation: Animation?) {
                super.onAnimationEnd(animation)
                when (animation) {
                    tpStartEnterAnim -> binding.tpStart.show()
                    tpStartExitAnim -> binding.tpStart.hide()
                    tpEndEnterAnim -> binding.tpEnd.show()
                    tpEndExitAnim -> binding.tpEnd.hide()
                }
            }
        }
        tpStartEnterAnim = AnimationUtils.loadAnimation(activity, R.anim.enter_from_left)
        tpStartEnterAnim.setAnimationListener(animationListener)
        tpStartExitAnim = AnimationUtils.loadAnimation(activity, R.anim.exit_to_left)
        tpStartExitAnim.setAnimationListener(animationListener)
        tpEndEnterAnim = AnimationUtils.loadAnimation(activity, R.anim.enter_from_right)
        tpEndEnterAnim.setAnimationListener(animationListener)
        tpEndExitAnim = AnimationUtils.loadAnimation(activity, R.anim.exit_to_right)
        tpEndExitAnim.setAnimationListener(animationListener)
    }

    private fun setTime(timePicker: TimePicker, time: String) {
        Timber.e(time)
        if (time.isEmpty()) {
            return
        }

        val hour = time.substringBefore(":").toInt()
        Timber.e(hour.toString())
        val minute = time.substringAfter(":").toInt()
        Timber.e(minute.toString())

        timePicker.apply {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                this.hour = hour
                this.minute = minute
            } else {
                // FIXME 23 버전 이하에서 동작하기 위해 사용
                currentHour = hour
                currentMinute = minute
            }
        }
    }

    private fun changeStartMode(isAnim: Boolean = true) {
        bind {
            mode = MODE_START
            btnStartTime.elevation = px2dip(0)
            btnEndTime.elevation = px2dip(16)
            btnStartTime.isSelected = true
            btnEndTime.isSelected = false
            if (isAnim) {
                tpStart.startAnimation(tpStartEnterAnim)
                tpEnd.startAnimation(tpEndExitAnim)
            } else {
                tpStart.show()
                tpEnd.hide()
            }
        }
    }

    private fun changeEndMode(isAnim: Boolean = true) {
        bind {
            mode = MODE_END
            btnStartTime.elevation = px2dip(16)
            btnEndTime.elevation = px2dip(0)
            btnStartTime.isSelected = false
            btnEndTime.isSelected = true
            if (isAnim) {
                tpStart.startAnimation(tpStartExitAnim)
                tpEnd.startAnimation(tpEndEnterAnim)
            } else {
                tpStart.hide()
                tpEnd.show()
            }
        }
    }

    companion object {
        const val EXTRA_MODE = "EXTRA_MODE"
        const val EXTRA_START_TIME = "EXTRA_START_TIME"
        const val EXTRA_END_TIME = "EXTRA_END_TIME"

        const val MODE_START = 0
        const val MODE_END = 1

        fun newInstance(mode: Int, startTime: String, endTime: String) =
            TimePickerDialogFragment().apply {
                arguments = Bundle().apply {
                    putInt(EXTRA_MODE, mode)
                    putString(EXTRA_START_TIME, startTime)
                    putString(EXTRA_END_TIME, endTime)
                }
            }
    }
}