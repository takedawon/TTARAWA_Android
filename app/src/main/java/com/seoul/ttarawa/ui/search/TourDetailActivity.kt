package com.seoul.ttarawa.ui.search

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.text.Spanned
import android.view.View.VISIBLE
import com.naver.maps.geometry.Utmk
import com.seoul.ttarawa.BuildConfig
import com.seoul.ttarawa.R
import com.seoul.ttarawa.base.BaseActivity
import com.seoul.ttarawa.data.entity.LocationTourModel
import com.seoul.ttarawa.data.remote.response.TourDetailsResponse
import com.seoul.ttarawa.data.remote.response.TourImageResponse
import com.seoul.ttarawa.databinding.ActivityTourDetailBinding
import com.seoul.ttarawa.ext.hide
import com.seoul.ttarawa.ext.show
import com.seoul.ttarawa.module.NetworkModule
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.URLDecoder


class TourDetailActivity : BaseActivity<ActivityTourDetailBinding>(
    R.layout.activity_tour_detail
) {
    private lateinit var tour: LocationTourModel
    private lateinit var url: ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        tour = intent.getParcelableExtra(EXTRA_ENTITY)
        initView()

        showProgressBar()
        getTourImage(
            10,
            1,
            tour.contentId,
            "Y",
            "Y"
        )
    }

    @SuppressLint("SimpleDateFormat")
    override fun initView() {
        var swit = true
        val title: String = tour.title

        val sDate = tour.startDate
        val eDate = tour.endDate
        val date = getDateToString(sDate) + " ~ " + getDateToString(eDate)
        bind {
            txtTourTitle.text = title
            txtEventDate.text = date
            layoutTourDetails.setOnClickListener {
                swit = if (swit) {
                    setDetailsViewShow()
                    false
                } else {
                    setDetailsViewHide()
                    true
                }
            }
            btnTourDetailOk.setOnClickListener {
                setResult(
                    Activity.RESULT_OK,
                    Intent().apply {
                        putExtra(EXTRA_CATEGORY, tour.categoryCode)
                        putExtra(EXTRA_ENTITY, tour)
                    }
                )
                finish()
            }
        }
    }

    private fun getDateToString(date: Int): String {
        val stringDate = date.toString()
        val year = stringDate.substring(0, 4) + "-"
        val month = stringDate.substring(4, 6) + "-"
        val day = stringDate.substring(6, 8)
        return year + month + day
    }

    private fun setDetailsViewShow() {
        bind {
            btnDetailsShow.setImageResource(R.drawable.up_arrow)
            txtTourDetails.maxLines = Integer.MAX_VALUE
        }
    }

    private fun setDetailsViewHide() {
        bind {
            btnDetailsShow.setImageResource(R.drawable.down_arrow)
            txtTourDetails.maxLines = 6
        }
    }

    private fun getTourImage(
        numOfRows: Int,
        pageNo: Int,
        contentId: Int,
        imageYN: String,
        subImageYN: String
    ) {
        val image: ArrayList<String> = ArrayList()
        NetworkModule.tourImageApi.getTourImage(
            serviceKey = URLDecoder.decode(BuildConfig.KMA_KEY, "utf-8"),
            numOfRows = numOfRows,
            pageNo = pageNo,
            mobileOS = "AND",
            mobileApp = "TARRAWA",
            contentId = contentId,
            imageYN = imageYN,
            subImageYN = subImageYN,
            _type = "json"
        ).enqueue(object : Callback<TourImageResponse> {
            override fun onFailure(call: Call<TourImageResponse>, t: Throwable) {
                t.printStackTrace()
            }

            override fun onResponse(
                call: Call<TourImageResponse>,
                response: Response<TourImageResponse>
            ) {

                response.body()?.let {
                    val num = if (numOfRows >= it.response.body.totalCount)
                        it.response.body.totalCount
                    else
                        numOfRows
                    for (i in 0 until num) {
                        image.add(it.response.body.items.item[i].originimgurl)
                    }
                }

                val adapter = TourImageAdapter()
                adapter.replaceAll(image)
                bind {
                    tourDetailViewPager.adapter = adapter
                }

                getTourDetails(1, 1, contentId, 15)
            }
        })
    }

    private fun getTourDetails(numOfRows: Int, pageNo: Int, contentId: Int, contentTypeId: Int) {
        NetworkModule.tourDetailsApi.getTourDetail(
            serviceKey = URLDecoder.decode(BuildConfig.KMA_KEY, "utf-8"),
            numOfRows = numOfRows,
            pageNo = pageNo,
            MobileOS = "AND",
            MobileApp = "TARRAWA",
            contentId = contentId,
            contentTypeId = contentTypeId,
            _type = "json"
        ).enqueue(object : Callback<TourDetailsResponse> {
            override fun onFailure(call: Call<TourDetailsResponse>, t: Throwable) {
                t.printStackTrace()
            }

            override fun onResponse(
                call: Call<TourDetailsResponse>,
                response: Response<TourDetailsResponse>
            ) {

                bind {
                    response.body()?.let {
                        val str: String = it.response.body.items.item.infotext
                        txtTourDetails.text = str.htmlToString()
                    }
                }
                hideProgressBar()
            }
        })
    }

    fun String.htmlToString(): Spanned {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(this, Html.FROM_HTML_MODE_LEGACY)
        } else {
            Html.fromHtml(this)
        }
    }

    private fun showProgressBar() {
        binding.pbTourDetails.show()
    }

    private fun hideProgressBar() {
        bind {
            pbTourDetails.hide()
            grpTourDetails.visibility = VISIBLE
        }
    }

    companion object {
        const val EXTRA_ENTITY = "EXTRA_ENTITY"
        const val EXTRA_CATEGORY = "EXTRA_CATEGORY"
    }
}

