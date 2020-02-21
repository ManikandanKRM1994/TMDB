package com.krm.tmdb.di.components

import com.krm.tmdb.app.TmdbApp
import com.krm.tmdb.data.api.NewsApi
import com.krm.tmdb.di.modules.NetModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [NetModule::class])
interface AppsComponent {
    fun inject(app: TmdbApp)
    fun getNewsApi() : NewsApi
}