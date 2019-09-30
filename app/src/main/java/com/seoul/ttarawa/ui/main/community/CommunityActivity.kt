package com.seoul.ttarawa.ui.main.community

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.loopeer.cardstack.AllMoveDownAnimatorAdapter
import com.loopeer.cardstack.CardStackView
import com.loopeer.cardstack.UpDownAnimatorAdapter
import com.loopeer.cardstack.UpDownStackAnimatorAdapter
import com.seoul.ttarawa.R
import com.seoul.ttarawa.base.BaseActivity
import com.seoul.ttarawa.data.entity.CommunityEntity
import com.seoul.ttarawa.data.remote.FirebaseLeaf
import com.seoul.ttarawa.data.remote.leaf.PathLeaf
import com.seoul.ttarawa.databinding.ActivityCommunityBinding
import com.seoul.ttarawa.ui.path.PathActivity
import org.jetbrains.anko.startActivity
import timber.log.Timber


class CommunityActivity : BaseActivity<ActivityCommunityBinding>(
    R.layout.activity_community
) {
    private lateinit var database: FirebaseDatabase

    private lateinit var mStackView: CardStackView
    private lateinit var mActionButtonContainer: LinearLayout
    private lateinit var mTestStackAdapter: TestStackAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        database = FirebaseDatabase.getInstance()
        initView()

        getSharedPathList()

    }

    override fun initView() {
        bind {
            stackviewMain.setItemExpendListener { expend -> }

            mTestStackAdapter = TestStackAdapter(this@CommunityActivity).apply {
                onClickStartPathActivity = { pathId, date ->
                    startActivity<PathActivity>(
                        PathActivity.EXTRA_PATH_ID to pathId,
                        PathActivity.EXTRA_DATE to date
                    )
                }
            }

            stackviewMain.setAdapter(mTestStackAdapter)
        }
    }

    private fun getSharedPathList() {
        showProgress()
        val communityEntityList = mutableListOf<CommunityEntity>()

        val userMap = mutableMapOf<String, String>()

        // 유저 키와 이름을 매칭시키기
        database.reference
            .child(FirebaseLeaf.DB_LEAF_USER)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    hideProgress()
                    Timber.w("$error")
                }

                override fun onDataChange(user: DataSnapshot) {
                    for (userInfo in user.children) {
                        val userKey = userInfo.key
                        val userName = userInfo.child("nickname").value.toString()
                        if (userKey != null) {
                            userMap[userKey] = userName
                        }
                    }

                    // 공유된 패스 가져오기
                    database.reference
                        .child(FirebaseLeaf.DB_LEAF_PATH)
                        .addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onCancelled(error: DatabaseError) {
                                hideProgress()
                                Timber.w("$error")
                            }

                            override fun onDataChange(path: DataSnapshot) {
                                hideProgress()
                                for (pathUser in path.children) {
                                    val userKey = pathUser.key
                                    if (userKey != null) {
                                        for (date in pathUser.children) {
                                            // PATH - uid - date - path
                                            date.children.forEach { pathInDate ->
                                                val pathLeaf =
                                                    pathInDate.getValue(PathLeaf::class.java)
                                                val pathId = pathInDate.key
                                                val userName = userMap[userKey]
                                                if (pathLeaf != null && userName != null && pathId != null) {
                                                    communityEntityList.add(
                                                        pathLeaf.toCommunityEntity(
                                                            pathId,
                                                            userName
                                                        )
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }

                                communityEntityList.sortByDescending { it.date }

                                addListInAdapter(communityEntityList)
                            }
                        })
                }
            })
    }

    private fun addListInAdapter(communityEntityList: List<CommunityEntity>) {
        mTestStackAdapter.updateData(communityEntityList)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_actions, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_all_down -> mStackView.setAnimatorAdapter(
                AllMoveDownAnimatorAdapter(
                    mStackView
                )
            )
            R.id.menu_up_down -> mStackView.setAnimatorAdapter(
                UpDownAnimatorAdapter(mStackView)
            )
            R.id.menu_up_down_stack -> mStackView.setAnimatorAdapter(
                UpDownStackAnimatorAdapter(
                    mStackView
                )
            )
        }
        return super.onOptionsItemSelected(item)
    }

    fun onPreClick(view: View) {
        mStackView.pre()
    }

    fun onNextClick(view: View) {
        mStackView.next()
    }

    fun onItemExpend(expend: Boolean) {
        mActionButtonContainer.visibility = if (expend) View.VISIBLE else View.GONE
    }
}
