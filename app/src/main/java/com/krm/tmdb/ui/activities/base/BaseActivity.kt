package com.krm.tmdb.ui.activities.base

import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.krm.tmdb.app.TmdbApp
import com.krm.tmdb.di.components.ActivityComponent
import com.krm.tmdb.di.components.DaggerActivityComponent
import com.krm.tmdb.di.modules.ActivityModule
import com.krm.tmdb.utils.KeyboardUtils
import com.pawegio.kandroid.longToast
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import com.pawegio.kandroid.alert

abstract class BaseActivity : AppCompatActivity(), InterfaceView, BaseFragment.Callback {

    companion object {
        @JvmStatic var component: ActivityComponent? = null

        @JvmStatic var compositeDisposable: CompositeDisposable?=null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component = DaggerActivityComponent.builder().activityModule(ActivityModule(this))
                .appsComponent(TmdbApp.component).build()
        compositeDisposable = CompositeDisposable()
    }

    fun getWindowFlagStable(){
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE)
    }

    fun getActivityComponent(): ActivityComponent? {
        return if (component != null) component else null
    }

    override fun hideKeyboard() {
        KeyboardUtils.hiddenKeyboard(this)
    }

    override fun showMessage(message: String) {
        longToast(message)
    }

    override fun showDialog(title: Int, message: String, btnMsgOk: Int, btnOk: (DialogInterface) -> Unit) : AlertDialog =
            alert(message,getString(title)){
                positiveButton(btnMsgOk){ btnOk(this) }
            }.builder.create()

    override fun addDisposable(disposable: Disposable){
        compositeDisposable!!.add(disposable)
    }

    override fun clearDisposable() {
        compositeDisposable!!.clear()
    }

    override fun setMinimizeDraggable() {}
    override fun setMaximizeDraggable(id: Int, name: String, type: String, image: String) {}
    override fun setCueVideo(key: String) {}
    override fun isMaximized(): Boolean = true
    override fun setSelectedNavigationMovie(itemId: Int) {}
    override fun setSelectedNavigationTv(itemId: Int) {}
    override fun moveActivityToBack() {}

}