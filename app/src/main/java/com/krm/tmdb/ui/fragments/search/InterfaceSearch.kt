package com.krm.tmdb.ui.fragments.search

import com.krm.tmdb.data.model.ObjectsSearch
import com.krm.tmdb.ui.activities.base.InterfaceInteractor
import com.krm.tmdb.ui.activities.base.InterfaceView
import com.google.gson.JsonArray

interface InterfaceSearch {

    interface Interactor<V : View> : InterfaceInteractor<V> {
        fun getParamsSearch(query: String): MutableMap<String, String>
        fun getDataSearch(query: String)
    }

    interface View : InterfaceView {
        fun setSearch(query: String)
        fun setDataSearch(json: JsonArray)
        fun showLoading()
        fun hiddenLoading()
        fun hiddenLoadingFailed()
        fun hiddenLoadingNotData()
        fun resultData(data: ObjectsSearch)
    }

}