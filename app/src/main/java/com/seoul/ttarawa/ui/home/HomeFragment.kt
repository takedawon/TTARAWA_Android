package com.seoul.ttarawa.ui.home

import android.os.Bundle
import android.util.Log
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import com.seoul.ttarawa.R
import com.seoul.ttarawa.base.BaseFragment
import com.seoul.ttarawa.databinding.FragmentHomeBinding
import com.seoul.ttarawa.util.LocationUtil
import io.nlopez.smartlocation.OnLocationUpdatedListener
import io.nlopez.smartlocation.SmartLocation
import io.nlopez.smartlocation.location.providers.LocationGooglePlayServicesProvider
import org.jetbrains.anko.support.v4.toast
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home) {
    private val homeViewModel: HomeViewModel by viewModel()


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        var permissionlistener: PermissionListener = object : PermissionListener {
            override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
            }

            override fun onPermissionGranted() {
                // 권한 허가시 실행 할 내용
                val provider: LocationGooglePlayServicesProvider? =
                    LocationGooglePlayServicesProvider()
                provider?.setCheckLocationSettings(true)
                val smartLocation = SmartLocation.Builder(
                    activity!!
                ).logging(true).build()
                smartLocation.location(provider).start(OnLocationUpdatedListener {
                    Log.i("location : ", it.longitude.toString() + "," + it.latitude)
                })
                val lastLocation = SmartLocation.with(activity!!).location().lastLocation
                if (lastLocation != null) {
                    toast(lastLocation.latitude.toString() + " " + lastLocation.longitude.toString())
                    var lat: Double = lastLocation.latitude
                    var lon: Double = lastLocation.longitude
                    val currentLocation: LocationUtil.Grid =
                        LocationUtil.convertToGrid(lat = lat, lng = lon)
                    homeViewModel.setLocation(currentLocation.nx, currentLocation.ny)
                }

                smartLocation.location(provider).stop()

            }
        }

        TedPermission.with(activity!!)
            .setPermissionListener(permissionlistener)
            .setRationaleMessage("앱의 기능을 사용하기 위해서는 권한이 필요합니다.")
            .setDeniedMessage("[설정] > [권한] 에서 권한을 허용할 수 있습니다.")
            .setPermissions(
                android.Manifest.permission.ACCESS_NETWORK_STATE,
                android.Manifest.permission.ACCESS_FINE_LOCATION
                , android.Manifest.permission.ACCESS_COARSE_LOCATION
            )
            .check()
    }

    override fun initView() {

    }

    companion object {
        fun newInstance(): HomeFragment {
            return HomeFragment()
        }
    }
}