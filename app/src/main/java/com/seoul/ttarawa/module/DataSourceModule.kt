package com.seoul.ttarawa.module

import com.seoul.ttarawa.data.remote.datasource.WeatherRemoteDataSource
import com.seoul.ttarawa.data.remote.datasource.impl.WeatherRemoteDataSourceImpl
import org.koin.dsl.module

val dataSourceModule = module {

    single<WeatherRemoteDataSource> {
        WeatherRemoteDataSourceImpl(get())
    }
}
