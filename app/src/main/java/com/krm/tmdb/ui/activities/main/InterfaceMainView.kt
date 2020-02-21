package com.krm.tmdb.ui.activities.main

import com.krm.tmdb.ui.activities.base.InterfaceView

/**
 * Created by luis rafael on 16/02/19.
 */
interface InterfaceMainView : InterfaceView {

    fun setCheckedNavigationItem(id: Int)
    fun closeNavigationDrawer()
    fun setTitleToolbar(title: String)
    fun setDrawerUnLock()
    fun setDrawerLock()
}