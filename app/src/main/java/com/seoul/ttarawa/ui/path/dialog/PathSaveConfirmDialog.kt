package com.seoul.ttarawa.ui.path.dialog

import android.content.Context
import android.os.Bundle
import com.seoul.ttarawa.base.BaseDialogFragment
import com.seoul.ttarawa.databinding.DialogFragmentPathSaveConfirmBinding
import com.seoul.ttarawa.ext.click
import com.seoul.ttarawa.ext.showKeyboard
import com.seoul.ttarawa.ui.path.PathActivity
import org.jetbrains.anko.support.v4.toast


class PathSaveConfirmDialog : BaseDialogFragment<DialogFragmentPathSaveConfirmBinding>(
    R.layout.dialog_fragment_path_save_confirm
) {
    private lateinit var onConfirmCallback: ((pathTitle: String) -> Unit)

    override fun onAttach(context: Context) {
        super.onAttach(context)
        onConfirmCallback = {
            (context as? PathActivity)?.savePath(it)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        dialog?.window?.decorView?.setBackgroundResource(android.R.color.transparent)

        bind {
            btnPathSaveCancel click {
                dismiss()
            }

            btnPathSaveConfirm click {
                startSavePathCallback()
            }

            tietPathSavePathName.requestFocus()
            this@PathSaveConfirmDialog.context?.showKeyboard()
        }
    }

    private fun startSavePathCallback() {
        val pathTitle = binding.tietPathSavePathName.text.toString()
        if (pathTitle.isEmpty()) {
            toast("경로명을 입력해주세요")
            return
        }
        onConfirmCallback(pathTitle)
        dismiss()
    }

    companion object {
        @JvmStatic
        fun newInstance(): PathSaveConfirmDialog {
            return PathSaveConfirmDialog()
        }
    }
}