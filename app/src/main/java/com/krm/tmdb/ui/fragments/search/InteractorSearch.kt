package com.krm.tmdb.ui.fragments.search

import android.content.Context
import androidx.fragment.app.FragmentManager
import com.krm.tmdb.data.api.NewsApi
import com.krm.tmdb.ui.activities.base.BaseInteractor
import com.krm.tmdb.utils.schedulers.SchedulerProvider
import javax.inject.Inject

class InteractorSearch<V : InterfaceSearch.View> @Inject constructor(supportFragment: FragmentManager, context: Context, api: NewsApi) : BaseInteractor<V>(supportFragment, context,api),
    InterfaceSearch.Interactor<V> {

    override fun getParamsSearch(query: String): MutableMap<String, String> = getApi().getParamsSearch(1, query)

    override fun getDataSearch(query: String){
        getView().addDisposable(getApi().getDataSearch(getParamsSearch(query))
                .subscribeOn(SchedulerProvider.io())
                .observeOn(SchedulerProvider.ui())
                .subscribe({ if (isViewNull()) getView().resultData(it) }, { if (isViewNull()) getView().hiddenLoadingFailed() }))
    }

}