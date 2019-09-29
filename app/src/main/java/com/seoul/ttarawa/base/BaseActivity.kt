package com.seoul.ttarawa.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.seoul.ttarawa.ui.progress.ProgressDialogFragment

abstract class BaseActivity<B : ViewDataBinding>(private val layoutId: Int) :
    AppCompatActivity() {

    private var progress: ProgressDialogFragment? = null

    protected lateinit var binding: B
        private set

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, layoutId)
        binding.lifecycleOwner = this
    }

    protected fun bind(action: B.() -> Unit) {
        binding.run(action)
    }

    protected fun <T : ViewDataBinding> bind(binding: T, action: T.() -> Unit) {
        binding.run(action)
    }

    abstract fun initView()

    fun showProgress() {
        progress = ProgressDialogFragment.newInstance()

        progress?.let {
            if (it.isShowing()) {
                return
            }
            progress?.show(supportFragmentManager, null)
        }
    }

    fun hideProgress() {
        progress?.dismiss()
    }
}
