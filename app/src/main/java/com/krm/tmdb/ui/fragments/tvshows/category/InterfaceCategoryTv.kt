package com.krm.tmdb.ui.fragments.tvshows.category

import com.krm.tmdb.data.model.ObjectTv
import com.krm.tmdb.ui.activities.base.InterfaceInteractor
import com.krm.tmdb.ui.activities.base.InterfaceView

interface InterfaceCategoryTv {


    interface Interactor<V : ViewCategoryTv> : InterfaceInteractor<V> {
        fun setCategory(category: String)
        fun getParams(): MutableMap<String, String>
        fun getTvShow()
        fun getPage() : Int
    }

    interface ViewCategoryTv : InterfaceView {
        fun setAdapterRecycler()
        fun addItemRecycler(data: ObjectTv)
        fun showLoading()
        fun hiddenLoading()
        fun hiddenLoadingFailed()

    }

}