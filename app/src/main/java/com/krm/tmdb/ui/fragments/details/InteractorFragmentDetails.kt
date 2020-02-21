package com.krm.tmdb.ui.fragments.details

import android.content.Context
import androidx.fragment.app.FragmentManager
import com.krm.tmdb.data.api.NewsApi
import com.krm.tmdb.ui.activities.base.BaseInteractor
import com.krm.tmdb.utils.ConstStrings
import com.krm.tmdb.utils.ConstFun.e
import com.krm.tmdb.utils.ConstFun.getApiKeyTmdb
import com.krm.tmdb.utils.schedulers.SchedulerProvider
import javax.inject.Inject

class InteractorFragmentDetails<V : InterfaceFragmentDetails.View> @Inject constructor(supportFragment: FragmentManager, context: Context, api: NewsApi) : BaseInteractor<V>(supportFragment, context,api), InterfaceFragmentDetails.Interactor<V> {

    override fun getParams(): MutableMap<String, String> = getApi().getParams(1)

    override fun getDetailsMovie(id: Int){
        getView().addDisposable(getApi().getDetailsMovies(id, getParams())
                .subscribeOn(SchedulerProvider.io())
                .observeOn(SchedulerProvider.ui())
                .doOnSubscribe { if (isViewNull()) getView().showLoading() }
                .doFinally { if (isViewNull()) getView().hiddenLoading() }
                .subscribe({ if (isViewNull()) getView().setDataMovies(it) }, { if (isViewNull()) getView().hiddenFailed() }))
    }

    override fun getDetailsTv(id: Int){
        getView().addDisposable(getApi().getDetailsTv(id, getParams())
                .subscribeOn(SchedulerProvider.io())
                .observeOn(SchedulerProvider.ui())
                .doOnSubscribe { if (isViewNull()) getView().showLoading() }
                .doFinally { if (isViewNull()) getView().hiddenLoading() }
                .subscribe({ if (isViewNull()) getView().setDataTv(it) }, { if (isViewNull()) getView().hiddenFailed() }))
    }

    override fun getExternalIds(url: String) {
        getView().addDisposable(getApi().getExternalIds(url,getParams())
                .subscribeOn(SchedulerProvider.io())
                .observeOn(SchedulerProvider.ui())
                .subscribe({ if (isViewNull()) getView().setDataExternalIds(it) }, { e(it) }))
    }

    override fun getReviews(url: String) {
        getView().addDisposable(getApi().getReview(url, getParams())
                .subscribeOn(SchedulerProvider.io())
                .observeOn(SchedulerProvider.ui())
                .doOnSubscribe { if (isViewNull()) getView().clearListReviews() }
                .subscribe({ if (isViewNull()) getView().setReviews(it.results) }, { e(it) }))
    }

    override fun getCast(url: String){
        getView().addDisposable(getApi().getCast(url, getParams())
                .subscribeOn(SchedulerProvider.io())
                .observeOn(SchedulerProvider.ui())
                .doOnSubscribe { if (isViewNull()) getView().clearListCast() }
                .subscribe({ if (isViewNull()) getView().setCast(it.cast) }, { e(it) }))
    }

    override fun getTrailers(url: String){
        getView().addDisposable(getApi().getTrailer(url, getParams())
                .subscribeOn(SchedulerProvider.io())
                .observeOn(SchedulerProvider.ui())
                .doOnSubscribe {
                    if (isViewNull()) getView().showLoading()
                    if (isViewNull()) getView().clearListTrailers()
                }
                .subscribe({
                    if (isViewNull()) getView().setDataTrailer(it)
                    if (isViewNull()) getView().setTrailers(it.results)
                }, { if (isViewNull()) getView().hiddenFailed() }))
    }

    override fun getSimilarMovie(id: Int) {
        getView().addDisposable(getApi().getSimilarMovie(id, getParams())
                .subscribeOn(SchedulerProvider.io())
                .observeOn(SchedulerProvider.ui())
                .doOnSubscribe { if (isViewNull()) getView().clearListSimilarMovies() }
                .subscribe({ if (isViewNull()) getView().setDataSimilarMovie(it.results) }, { e(it) }))
    }

    override fun getSimilarTv(id: Int) {
        getView().addDisposable(getApi().getSimilarTv(id, getParams())
                .subscribeOn(SchedulerProvider.io())
                .observeOn(SchedulerProvider.ui())
                .doOnSubscribe { if (isViewNull()) getView().clearListSimilarTv() }
                .subscribe({ if (isViewNull()) getView().setDataSimilarTv(it.results) }, { e(it) }))
    }

    override fun getImages(url: String) {
        val params = mutableMapOf<String, String>()
        params[ConstStrings.API_KEY] = getApiKeyTmdb()
        getView().addDisposable(getApi().getImages(url, params)
                .subscribeOn(SchedulerProvider.io())
                .observeOn(SchedulerProvider.ui())
                .doOnSubscribe { if (isViewNull()) getView().clearListImages() }
                .subscribe({ if (isViewNull()) getView().setImages(it) }, { e(it) }))
    }

}