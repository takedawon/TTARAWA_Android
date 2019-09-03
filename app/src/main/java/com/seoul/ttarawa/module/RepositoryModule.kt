package com.seoul.ttarawa.module

import com.seoul.ttarawa.data.repository.WeatherRepository
import com.seoul.ttarawa.data.repository.impl.WeatherRepositoryImpl
import org.koin.dsl.module

val repositoryModule = module {

    single<WeatherRepository> {
        WeatherRepositoryImpl(get())
    }
}
