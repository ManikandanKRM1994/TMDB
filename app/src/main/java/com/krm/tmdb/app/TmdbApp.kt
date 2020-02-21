package com.krm.tmdb.app

import android.app.Application
import com.krm.tmdb.di.components.AppsComponent
import com.krm.tmdb.di.components.DaggerAppsComponent
import com.krm.tmdb.di.modules.NetModule

class TmdbApp : Application() {

    companion object {
        @JvmStatic lateinit var component: AppsComponent
    }

    override fun onCreate() {
        super.onCreate()
        component = DaggerAppsComponent.builder().netModule(NetModule(this)).build()
        component.inject(this)
    }
}