package com.krm.tmdb.ui.fragments.movies.category

import android.content.Context
import androidx.fragment.app.FragmentManager
import com.krm.tmdb.data.api.NewsApi
import com.krm.tmdb.ui.activities.base.BaseInteractor
import com.krm.tmdb.utils.schedulers.SchedulerProvider
import javax.inject.Inject

class InteractorCategoryMovies<V : InterfaceCategoryMovies.ViewCategoryMovies> @Inject constructor(supportFragment: FragmentManager, context: Context, api: NewsApi) : BaseInteractor<V>(supportFragment, context,api),
    InterfaceCategoryMovies.Interactor<V> {

    private var category: String? = null
    private var nextPage: Int = 1

    override fun getPage(): Int = nextPage

    override fun setCategory(category: String) {
        this.category = category
    }

    override fun getParams(): MutableMap<String, String> = getApi().getParams(nextPage)

    override fun getMovies() {
        getView().addDisposable(getApi().getMovies(category!!, getParams())
                .doOnSuccess { nextPage = it.page + 1 }
                .subscribeOn(SchedulerProvider.io())
                .observeOn(SchedulerProvider.ui())
                .doOnSubscribe { if (isViewNull()) getView().showLoading() }
                //.doFinally { if (isViewNull()) getView().hiddenLoading() }
                .subscribe({ if (isViewNull()) getView().addItemRecycler(it) }, { if (isViewNull()) getView().hiddenLoadingFailed() }))
    }

}