package com.krm.tmdb.ui.activities.splash

import com.krm.tmdb.data.model.ObjectGenders
import com.krm.tmdb.ui.activities.base.InterfaceView

interface InterfaceSplashView : InterfaceView {

    fun openActivityHome()
    fun setGenderTv(data: ObjectGenders)
    fun setGenderMovies(data: ObjectGenders)

}