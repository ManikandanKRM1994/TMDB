package com.krm.tmdb.ui.fragments.people

import android.content.Context
import androidx.fragment.app.FragmentManager
import com.krm.tmdb.data.api.NewsApi
import com.krm.tmdb.ui.activities.base.BaseInteractor
import com.krm.tmdb.utils.schedulers.SchedulerProvider
import javax.inject.Inject

class InteractorFragmentPeople<V : InterfacePeople.View> @Inject constructor(supportFragment: FragmentManager, context: Context, api: NewsApi) : BaseInteractor<V>(supportFragment, context,api),
    InterfacePeople.Interactor<V> {


    private var nextPage: Int = 1

    override fun getPage(): Int = nextPage

    override fun getParams(): MutableMap<String, String> = getApi().getParams(nextPage)

    override fun getPersons() {
        getView().addDisposable(getApi().getPersons(getParams())
                .doOnSuccess { nextPage = it.page + 1 }
                .subscribeOn(SchedulerProvider.io())
                .observeOn(SchedulerProvider.ui())
                .doOnSubscribe { if (isViewNull()) getView().showLoading() }
                .doFinally { if (isViewNull()) getView().hiddenLoading() }
                .subscribe({ if (isViewNull()) getView().addItemRecycler(it) }, { if (isViewNull()) getView().hiddenLoadingFailed() }))
    }

}