package com.krm.tmdb.ui.activities.about

import android.graphics.Paint
import android.os.Bundle
import com.krm.tmdb.ui.activities.base.BaseActivity
import com.krm.tmdb.R
import com.krm.tmdb.utils.ConstFun.openActivityBrowser
import com.krm.tmdb.utils.ConstFun.version
import kotlinx.android.synthetic.main.activity_about.*

class AboutActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        txt_version.text = version

        btn_web_page.setOnClickListener { openActivityBrowser(getString(R.string.link_github)) }
        txt_web_page.paintFlags = Paint.UNDERLINE_TEXT_FLAG
        btn_policy.setOnClickListener { openActivityBrowser(getString(R.string.link_tmdb)) }
        txt_policy.paintFlags = Paint.UNDERLINE_TEXT_FLAG

    }

}