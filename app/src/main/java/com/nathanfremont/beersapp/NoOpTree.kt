package com.nathanfremont.beersapp

import android.util.Log
import timber.log.Timber


class NoOpTree : Timber.Tree() {
    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {}
}