package com.krm.tmdb.utils

import android.os.CountDownTimer
import com.pawegio.kandroid.i

class MyCountDownTimer(startTime: Long, interval: Long, private val func: () -> Unit) : CountDownTimer(startTime, interval) {
    override fun onFinish() = func()
    override fun onTick(timer: Long) { i("TMDb","timer $timer")  }
}