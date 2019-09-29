package com.seoul.ttarawa.ui.main.community

import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import com.loopeer.cardstack.AllMoveDownAnimatorAdapter
import com.loopeer.cardstack.CardStackView
import com.loopeer.cardstack.UpDownAnimatorAdapter
import com.loopeer.cardstack.UpDownStackAnimatorAdapter
import com.seoul.ttarawa.R
import com.seoul.ttarawa.base.BaseActivity
import com.seoul.ttarawa.databinding.ActivityCommunityBinding


class CommunityAcitivty : BaseActivity<ActivityCommunityBinding>(
    R.layout.activity_community
) {
    private val TEST_DATAS = listOf<Int>(
        R.color.color_1,
        R.color.color_2,
        R.color.color_3,
        R.color.color_4,
        R.color.color_5,
        R.color.color_6,
        R.color.color_7,
        R.color.color_8,
        R.color.color_9,
        R.color.color_10,
        R.color.color_11,
        R.color.color_12,
        R.color.color_13,
        R.color.color_14,
        R.color.color_15,
        R.color.color_16,
        R.color.color_17,
        R.color.color_18,
        R.color.color_19,
        R.color.color_20,
        R.color.color_21,
        R.color.color_22,
        R.color.color_23,
        R.color.color_24,
        R.color.color_25,
        R.color.color_26
    )
    private lateinit var mStackView: CardStackView
    private lateinit var mActionButtonContainer: LinearLayout
    private lateinit var mTestStackAdapter: TestStackAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bind {
            stackviewMain.setItemExpendListener { expend ->

            }
            mTestStackAdapter = TestStackAdapter(this@CommunityAcitivty)
            stackviewMain.setAdapter(mTestStackAdapter)

        }
        Handler().postDelayed(
            { mTestStackAdapter!!.updateData(TEST_DATAS) }, 200
        )

    }

    override fun initView() {
        mTestStackAdapter = TestStackAdapter(this)
        mStackView.setAdapter(mTestStackAdapter)
        mTestStackAdapter.updateData(TEST_DATAS)
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
