package com.krm.tmdb.ui.fragments.people

import com.krm.tmdb.data.model.ObjectPerson
import com.krm.tmdb.ui.activities.base.InterfaceInteractor
import com.krm.tmdb.ui.activities.base.InterfaceView

interface InterfacePeople {

    interface Interactor<V : View> : InterfaceInteractor<V> {
        fun getParams(): MutableMap<String, String>
        fun getPersons()
        fun getPage() : Int
    }

    interface View : InterfaceView {
        fun setAdapterRecycler()
        fun addItemRecycler(data: ObjectPerson)
        fun showLoading()
        fun hiddenLoading()
        fun hiddenLoadingFailed()
    }

}