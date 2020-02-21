package com.krm.tmdb.ui.fragments.home

import com.krm.tmdb.data.model.PojoResultsMovie
import com.krm.tmdb.data.model.PojoResultsTv
import com.krm.tmdb.ui.activities.base.InterfaceInteractor
import com.krm.tmdb.ui.activities.base.InterfaceView

interface InterfaceFragmentHome {

    interface Interactor<V : View> : InterfaceInteractor<V> {

        fun getMoviesPopular()
        fun getMoviesNowPlaying()
        fun getOnTv()
        fun getParams(): MutableMap<String, String>

    }

    interface View : InterfaceView {
        fun showLoadingMoviesPopular()
        fun hideLoadingMoviesPopular()
        fun setDataMoviesPopular(list: MutableList<PojoResultsMovie>)

        fun showLoadingMoviesNowPlaying()
        fun hideLoadingMoviesNowPlaying()
        fun setDataMoviesNowPlaying(list: MutableList<PojoResultsMovie>)

        fun showLoadingOnTv()
        fun hideLoadingOntTv()
        fun setDataOnTv(list: MutableList<PojoResultsTv>)
    }

}