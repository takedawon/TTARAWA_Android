package com.seoul.ttarawa.ui.mypath

import android.os.Bundle
import android.view.MenuItem
import com.seoul.ttarawa.R
import com.seoul.ttarawa.base.BaseActivity
import com.seoul.ttarawa.data.local.executor.LocalExecutor
import com.seoul.ttarawa.data.local.executor.provideLocalExecutor
import com.seoul.ttarawa.databinding.ActivityMyPathBinding
import com.seoul.ttarawa.ui.path.PathActivity
import org.jetbrains.anko.startActivity

class MyPathActivity : BaseActivity<ActivityMyPathBinding>(
    R.layout.activity_my_path
) {

    private val localExecutor: LocalExecutor by lazy { provideLocalExecutor(this@MyPathActivity) }

    private val myPathAdapter: MyPathAdapter by lazy { createMyPathAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initView()
    }

    override fun initView() {
        initActionBar()
        initRecyclerView()
    }

    private fun initRecyclerView() {
        binding.rvMyPath.adapter = myPathAdapter
        myPathAdapter.replaceAll(localExecutor.getPath())
    }

    private fun createMyPathAdapter() =
        MyPathAdapter().apply {
            onClickStartPathActivity = { id, date ->
                startActivity<PathActivity>(
                    PathActivity.EXTRA_PATH_ID to id,
                    PathActivity.EXTRA_DATE to date
                )
            }
        }

    private fun initActionBar() {
        setSupportActionBar(binding.tbMyPath)
        supportActionBar?.title = getString(R.string.my_path_toolbar_title)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {

            android.R.id.home -> {
                onBackPressed()
                return true
            }

            else -> {

            }
        }

        return super.onOptionsItemSelected(item)
    }

}
