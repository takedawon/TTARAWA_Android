package com.seoul.ttarawa.ui.mypath

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.seoul.ttarawa.R
import com.seoul.ttarawa.base.BaseActivity
import com.seoul.ttarawa.data.local.executor.LocalExecutor
import com.seoul.ttarawa.data.local.executor.provideLocalExecutor
import com.seoul.ttarawa.data.remote.FirebaseLeaf
import com.seoul.ttarawa.data.remote.leaf.PathLeaf
import com.seoul.ttarawa.databinding.ActivityMyPathBinding
import com.seoul.ttarawa.ui.path.PathActivity
import org.jetbrains.anko.startActivityForResult
import timber.log.Timber

class MyPathActivity : BaseActivity<ActivityMyPathBinding>(
    R.layout.activity_my_path
) {

    private val localExecutor: LocalExecutor by lazy { provideLocalExecutor(this@MyPathActivity) }

    private lateinit var database: FirebaseDatabase

    private var firebaseUser: FirebaseUser? = null

    private val myPathAdapter: MyPathAdapter by lazy { createMyPathAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        firebaseUser = FirebaseAuth.getInstance().currentUser
        database = FirebaseDatabase.getInstance()

        initView()

        getMyPath()
    }

    override fun initView() {
        initActionBar()
        initRecyclerView()
    }

    private fun initRecyclerView() {
        binding.rvMyPath.adapter = myPathAdapter
    }

    private fun getMyPath() {
        showProgress()

        val list = localExecutor.getPath()
            .map { it.toPath() }
            .toMutableList()

        val uid = firebaseUser?.uid
        if (!uid.isNullOrEmpty()) {
            database.reference
                .child(FirebaseLeaf.DB_LEAF_PATH)
                .child(uid)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onCancelled(error: DatabaseError) {
                        hideProgress()
                        Timber.e("$error")
                    }

                    override fun onDataChange(user: DataSnapshot) {
                        for (date in user.children) {
                            for (path in date.children) {
                                val pathLeaf = path.getValue(PathLeaf::class.java)
                                if (pathLeaf != null) {
                                    list.add(pathLeaf.toPath(path.key ?: ""))
                                }
                            }
                        }
                        myPathAdapter.replaceAll(list)
                        hideProgress()
                    }
                }
                )
        }
    }

    private fun createMyPathAdapter() =
        MyPathAdapter().apply {
            onClickStartPathActivity = { id, date ->

                startActivityForResult<PathActivity>(
                    requestCode = PATH_UPDATE_CODE,
                    params = *arrayOf(
                        PathActivity.EXTRA_PATH_ID to id,
                        PathActivity.EXTRA_DATE to date
                    )
                )
            }
        }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == PATH_UPDATE_CODE) {
            }
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

    companion object {
        const val PATH_UPDATE_CODE = 3000
    }

}
