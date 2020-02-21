package com.krm.tmdb.ui.activities.splash

import com.krm.tmdb.di.PerActivity
import com.krm.tmdb.ui.activities.base.InterfaceInteractor

@PerActivity
interface InterfaceSplashInteractor<V : InterfaceSplashView> : InterfaceInteractor<V> {
    fun onGetGendersMovies()
    fun onGetGendersTv()
}