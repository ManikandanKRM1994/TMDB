package com.krm.tmdb.ui.activities.splash

import android.content.Context
import androidx.fragment.app.FragmentManager
import com.krm.tmdb.data.api.NewsApi
import com.krm.tmdb.ui.activities.base.BaseInteractor
import com.krm.tmdb.utils.ConstStrings
import com.krm.tmdb.utils.schedulers.SchedulerProvider
import javax.inject.Inject

class InteractorSplash <V : InterfaceSplashView> @Inject constructor(supportFragment: FragmentManager, context: Context, api: NewsApi) : BaseInteractor<V>(supportFragment, context,api),
    InterfaceSplashInteractor<V> {

    private fun getParams(): MutableMap<String, String> = getApi().getParams(1)

    override fun onGetGendersMovies() {
        getView().addDisposable(getApi().getGenders(ConstStrings.GENDER_MOVIE,getParams())
                .doOnSuccess { if (isViewNull()) getView().setGenderMovies(it) }
                .subscribeOn(SchedulerProvider.io())
                .observeOn(SchedulerProvider.ui())
                .subscribe({ onGetGendersTv() }, { onGetGendersTv() }))
    }

    override fun onGetGendersTv(){
        getView().addDisposable(getApi().getGenders(ConstStrings.GENDER_TV,getParams())
                .doOnSuccess { if (isViewNull()) getView().setGenderTv(it) }
                .subscribeOn(SchedulerProvider.io())
                .observeOn(SchedulerProvider.ui())
                .subscribe({ if (isViewNull()) getView().openActivityHome() }, { if (isViewNull()) getView().openActivityHome() }))

    }
}