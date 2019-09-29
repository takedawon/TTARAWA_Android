package com.seoul.ttarawa.ui.progress

import android.os.Bundle
import android.view.ViewGroup
import com.seoul.ttarawa.R
import com.seoul.ttarawa.base.BaseDialogFragment
import com.seoul.ttarawa.databinding.DialogFragmentProgressBinding

class ProgressDialogFragment : BaseDialogFragment<DialogFragmentProgressBinding>(
    R.layout.dialog_fragment_progress
) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.TransparentDialog)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        isCancelable = false

        dialog?.window?.decorView?.setBackgroundResource(android.R.color.transparent)
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
    }

    fun isShowing() = dialog?.isShowing ?: false

    companion object {
        @JvmStatic
        fun newInstance(): ProgressDialogFragment {
            return ProgressDialogFragment()
        }
    }
}