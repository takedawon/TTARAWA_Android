package com.seoul.ttarawa.ui.mypath

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import com.google.android.material.dialog.MaterialAlertDialogBuilder
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
import com.seoul.ttarawa.ui.main.LoginActivity
import com.seoul.ttarawa.ui.path.PathActivity
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.startActivityForResult
import org.jetbrains.anko.toast
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
        binding.rvMyPath.visibility = View.INVISIBLE
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
                        binding.rvMyPath.visibility = View.VISIBLE
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
                        binding.rvMyPath.visibility = View.VISIBLE
                        myPathAdapter.replaceAll(list)
                        hideProgress()
                        showEmpty()
                    }
                }
                )
        } else {
            binding.rvMyPath.visibility = View.VISIBLE
            myPathAdapter.replaceAll(list)
            hideProgress()
            showEmpty()
        }
    }

    private fun showEmpty() {
        if (myPathAdapter.itemCount == 0) {
            bind {
                tvMyPathEmpty.visibility = View.VISIBLE
                binding.rvMyPath.visibility = View.INVISIBLE
            }
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

            onClickChangeSharing = { path, position ->
                val isLocalData = try {
                    path.id.toInt()
                    true
                } catch (e: NumberFormatException) {
                    false
                }

                val uid = firebaseUser?.uid

                if (uid == null) {
                    MaterialAlertDialogBuilder(this@MyPathActivity)
                        .setTitle("로그인이 필요합니다.")
                        .setMessage("로그인이 필요한 서비스입니다. 로그인하시겠습니꺄?")
                        .setPositiveButton("네") { _, _ ->
                            startActivity<LoginActivity>()
                        }
                        .setNegativeButton("아니오") { dialog, _ ->
                            dialog.dismiss()
                        }
                        .show()
                }

                if (uid != null) {
                    if (isLocalData) {
                        showProgress()
                        // 파베에 등록하고, 로컬은 지우고, 상태값만 바꿔주기. 다음에 들어올 땐 파베에서 가져오게 된다.
                        val pathAndAllNodes = localExecutor.getPathAndNodes(path.id.toInt())

                        if (pathAndAllNodes != null) {
                            database.reference
                                .child(FirebaseLeaf.DB_LEAF_PATH)
                                .child(uid)
                                .child(path.date)
                                .push()
                                .setValue(pathAndAllNodes.toPathLeaf()) { error, _ ->
                                    if (error == null) {
                                        if (!path.shareYn) {
                                            toast(R.string.my_path_share_success)
                                        } else {
                                            toast(R.string.my_path_share_cancel_success)
                                        }
                                        myPathAdapter.changeStateSharing(position, !path.shareYn)
                                        try {
                                            // 기존 로컬 데이터 삭제, 파베에 올라간 데이터를 받기 위해 갱신
                                            localExecutor.deletePath(path.toPathEntity())
                                            hideProgress()
                                            getMyPath()
                                        } catch (e: Exception) {
                                            hideProgress()
                                            Timber.w(e)
                                        }
                                    } else {
                                        hideProgress()
                                        Timber.w("error = ${error.code}, $error")
                                        toast(R.string.my_path_share_fail)
                                    }
                                }
                        } else {
                            toast(R.string.my_path_share_fail)
                            hideProgress()
                        }
                    } else {
                        // 기존에 파베에 저쟝된 데이터
                        showProgress()
                        database.reference
                            .child(FirebaseLeaf.DB_LEAF_PATH)
                            .child(uid)
                            .child(path.date)
                            .child(path.id)
                            .child(FirebaseLeaf.DB_LEAF_SAHRE_YN)
                            .setValue(!path.shareYn) { error, _ ->
                                hideProgress()
                                if (error == null) {
                                    if (!path.shareYn) {
                                        toast(R.string.my_path_share_success)
                                    } else {
                                        toast(R.string.my_path_share_cancel_success)
                                    }
                                    myPathAdapter.changeStateSharing(position, !path.shareYn)
                                } else {
                                    Timber.w("error = ${error.code}, $error")
                                    toast(R.string.my_path_share_fail)
                                }
                            }
                    }
                }
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
