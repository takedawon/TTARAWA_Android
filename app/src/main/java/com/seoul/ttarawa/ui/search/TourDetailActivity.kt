package com.seoul.ttarawa.ui.search

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.View.VISIBLE
import com.seoul.ttarawa.BuildConfig
import com.seoul.ttarawa.R
import com.seoul.ttarawa.base.BaseActivity
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
    private lateinit var url: ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        showProgressBar()
        initView()
        val contentId= intent.getIntExtra("contentId",0)
        getTourImage(10,
            1,
            contentId,
            "Y",
            "Y")
    }

    override fun initView() {
        var swit = true
        val title:String = intent.getStringExtra("title")
        bind {
            txtTourTitle.text=title
            layoutTourDetails.setOnClickListener {
                swit = if(swit) {
                    setDetailsViewShow()
                    false
                } else {
                    setDetailsViewHide()
                    true
                }
            }
            btnTourDetailOk.setOnClickListener{
                val intentSearch = Intent(this@TourDetailActivity, SearchActivity::class.java)
                intentSearch.putExtra("mapX", intent.getDoubleArrayExtra("mapX"))
                intentSearch.putExtra("mapY", intent.getDoubleArrayExtra("mapY"))
                startActivity(intentSearch)
            }
        }
    }

    private fun setDetailsViewShow() {
        bind{
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
                    val num=it.response.body.totalCount
                    for (i in 0 until num) {
                        image.add(it.response.body.items.item[i].originimgurl)
                    }
                }

                val adapter = TourImageAdapter()
                adapter.replaceAll(image)
                bind {
                    tourDetailViewPager.adapter = adapter
                }

                getTourDetails(1,1,contentId,15)
            }
        })
    }

    private fun getTourDetails(numOfRows:Int,pageNo:Int,contentId:Int,contentTypeId:Int) {
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
                    response.body()?.let{
                        var str:String = it.response.body.items.item.infotext
                        txtTourDetails.text = str.htmlToString().replace("\n","")
                    }
                }
                hideProgressBar()
            }
        })
    }

    fun String.htmlToString() : String {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(this, Html.FROM_HTML_MODE_LEGACY).toString()
        } else {
            return Html.fromHtml(this).toString()
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
}

