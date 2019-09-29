package com.seoul.ttarawa.data.local.executor

import android.content.Context
import com.seoul.ttarawa.data.local.LocalDataBaseProvider

fun provideLocalExecutor(context: Context) =
    LocalExecutor.getInstance(
        LocalDataBaseProvider.getInstance(context.applicationContext)
    )