package com.krm.tmdb.ui.activities.splash

import android.os.Bundle
import androidx.multidex.MultiDex
import com.krm.tmdb.R
import com.krm.tmdb.data.model.ObjectGenders
import com.krm.tmdb.ui.activities.base.BaseActivity
import com.krm.tmdb.data.preference.DataSharePreference.setGenderMovie
import com.krm.tmdb.data.preference.DataSharePreference.setGenderTvShow
import com.krm.tmdb.utils.ConstFun.fadeZoomTransitionActivity
import com.krm.tmdb.utils.ConstFun.openActivityMain
import kotlinx.android.synthetic.main.activity_splash.*
import javax.inject.Inject

class SplashActivity : BaseActivity() , InterfaceSplashView {

    @Inject lateinit var interactorSplash: InteractorSplash<InterfaceSplashView>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        getActivityComponent()!!.inject(this)
        interactorSplash.onAttach(this)
        MultiDex.install(this)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        fadeZoomTransitionActivity(this, logo_splash)
        interactorSplash.onGetGendersMovies()
    }

    override fun onDestroy() {
        interactorSplash.onDetach()
        clearDisposable()
        super.onDestroy()
    }

    override fun setGenderMovies(data: ObjectGenders){
        (0 until data.genres.size).asSequence().map { data.genres[it] }
                .forEach { setGenderMovie(it.id.toString(), it.name) }
    }

    override fun setGenderTv(data: ObjectGenders){
        (0 until data.genres.size).asSequence().map { data.genres[it] }
                .forEach { setGenderTvShow(it.id.toString(), it.name) }
    }

    override fun openActivityHome() {
        openActivityMain()
    }

}