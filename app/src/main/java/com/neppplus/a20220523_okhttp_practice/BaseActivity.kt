package com.neppplus.a20220523_okhttp_practice

import androidx.appcompat.app.AppCompatActivity

abstract class BaseActivity : AppCompatActivity() {

    val mContext = this

    val TAG = javaClass.simpleName

    abstract fun setupEvents()
    abstract fun setValues()

}