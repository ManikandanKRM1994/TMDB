package com.krm.tmdb.ui.fragments.movies.category

import com.krm.tmdb.data.model.ObjectMovies
import com.krm.tmdb.ui.activities.base.InterfaceInteractor
import com.krm.tmdb.ui.activities.base.InterfaceView

interface InterfaceCategoryMovies {

    interface Interactor<V : ViewCategoryMovies> : InterfaceInteractor<V> {
        fun setCategory(category: String)
        fun getParams(): MutableMap<String, String>
        fun getMovies()
        fun getPage() : Int
    }

    interface ViewCategoryMovies : InterfaceView {
        fun setAdapterRecycler()
        fun addItemRecycler(data: ObjectMovies)
        fun showLoading()
        fun hiddenLoading()
        fun hiddenLoadingFailed()
    }
}