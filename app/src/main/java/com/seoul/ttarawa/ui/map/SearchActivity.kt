package com.seoul.ttarawa.ui.map

import android.graphics.Point
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.seoul.ttarawa.R
import com.seoul.ttarawa.base.BaseActivity
import com.seoul.ttarawa.databinding.ActivitySearchBinding
import org.jetbrains.anko.dip

/**
 * 검색 화면
 * 조건을 추가하고, 검색을 한다
 * 검색 결과를 가지고 PathFragment 로 이동
 */
class SearchActivity : BaseActivity<ActivitySearchBinding>(
    R.layout.activity_search
) {

    private var bottomSheetBehavior: BottomSheetBehavior<View>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
    }

    override fun initView() {
        bind {
            setSupportActionBar(tbSearch)
            supportActionBar?.title = getString(R.string.path_actionbar_title)

            llSearchBottomSheet.layoutParams.height = getHeightWithoutActionBarSize()
            bottomSheetBehavior = BottomSheetBehavior.from(binding.llSearchBottomSheet)
        }
    }

    private fun getHeightWithoutActionBarSize(): Int {
        val point = Point()
        this@SearchActivity.windowManager.defaultDisplay.getSize(point)
        return point.y - dip(56)
    }

    override fun onBackPressed() {
        if (bottomSheetBehavior?.state == BottomSheetBehavior.STATE_EXPANDED) {
            bottomSheetBehavior?.state = BottomSheetBehavior.STATE_COLLAPSED
            return
        }
        super.onBackPressed()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        item?.let {
            when (it.itemId) {
                android.R.id.home -> {
                    bottomSheetBehavior?.run {
                        state = if (state == BottomSheetBehavior.STATE_EXPANDED) {
                            BottomSheetBehavior.STATE_COLLAPSED

                        } else {
                            BottomSheetBehavior.STATE_EXPANDED
                        }
                        return true
                    }
                }
                else -> {
                    /*ignored*/
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }
}