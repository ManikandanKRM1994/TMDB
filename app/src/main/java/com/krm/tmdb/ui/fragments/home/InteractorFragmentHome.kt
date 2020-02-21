package com.krm.tmdb.ui.fragments.home

import android.content.Context
import androidx.fragment.app.FragmentManager
import com.krm.tmdb.data.api.NewsApi
import com.krm.tmdb.ui.activities.base.BaseInteractor
import com.krm.tmdb.utils.ConstStrings.Companion.AIR_TV
import com.krm.tmdb.utils.ConstStrings.Companion.NOW_PLAYING
import com.krm.tmdb.utils.ConstStrings.Companion.POPULAR_MOVIE
import com.krm.tmdb.utils.ConstFun.e
import com.krm.tmdb.utils.schedulers.SchedulerProvider
import javax.inject.Inject

class InteractorFragmentHome<V : InterfaceFragmentHome.View> @Inject constructor(supportFragment: FragmentManager, context: Context, api: NewsApi) : BaseInteractor<V>(supportFragment, context,api),
    InterfaceFragmentHome.Interactor<V> {

    override fun getParams(): MutableMap<String, String> = getApi().getParams(1)

    override fun getMoviesPopular() {
        getView().addDisposable(getApi().getMovies(POPULAR_MOVIE, getParams())
                .subscribeOn(SchedulerProvider.io())
                .observeOn(SchedulerProvider.ui())
                .doOnSubscribe { if (isViewNull()) getView().showLoadingMoviesPopular() }
                .doFinally { if (isViewNull()) getView().hideLoadingMoviesPopular() }
                .subscribe({ if (isViewNull()) getView().setDataMoviesPopular(it.results) }, { e(it) }))
    }

    override fun getMoviesNowPlaying() {
        getView().addDisposable(getApi().getMovies(NOW_PLAYING, getParams())
                .subscribeOn(SchedulerProvider.io())
                .observeOn(SchedulerProvider.ui())
                .doOnSubscribe { if (isViewNull()) getView().showLoadingMoviesNowPlaying() }
                .doFinally { if (isViewNull()) getView().hideLoadingMoviesNowPlaying() }
                .subscribe({ if (isViewNull()) getView().setDataMoviesNowPlaying(it.results) }, { e(it) }))
    }

    override fun getOnTv() {
        getView().addDisposable(getApi().getTvShows(AIR_TV,getParams())
                .subscribeOn(SchedulerProvider.io())
                .observeOn(SchedulerProvider.ui())
                .doOnSubscribe { if (isViewNull()) getView().showLoadingOnTv() }
                .doFinally { if (isViewNull()) getView().hideLoadingOntTv() }
                .subscribe({ if (isViewNull()) getView().setDataOnTv(it.results) }, { e(it) }))
    }

}