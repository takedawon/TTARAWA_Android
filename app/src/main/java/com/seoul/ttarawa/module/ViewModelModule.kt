package com.seoul.ttarawa.module

import com.seoul.ttarawa.ui.calendar.CalendarViewModel
import com.seoul.ttarawa.ui.home.HomeViewModel
import com.seoul.ttarawa.ui.main.MainViewModel
import com.seoul.ttarawa.ui.map.MapViewModel
import com.seoul.ttarawa.ui.path.PathViewModel
import com.seoul.ttarawa.ui.plan.PlanViewModel
import com.seoul.ttarawa.ui.search.SearchViewModel
import com.seoul.ttarawa.ui.setting.SettingViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel { MainViewModel(get()) }
    viewModel { CalendarViewModel() }
    viewModel { HomeViewModel() }
    viewModel { MapViewModel() }
    viewModel { PathViewModel() }
    viewModel { PlanViewModel() }
    viewModel { SearchViewModel() }
    viewModel { SettingViewModel() }
}
