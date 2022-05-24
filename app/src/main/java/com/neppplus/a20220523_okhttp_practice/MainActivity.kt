package com.neppplus.a20220523_okhttp_practice

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.neppplus.a20220523_okhttp_practice.databinding.ActivityMainBinding
import com.neppplus.a20220523_okhttp_practice.utils.ContextUtil

class MainActivity : BaseActivity() {

    lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setupEvents()
        setValues()
    }

    override fun setupEvents() {
        binding.logOutBtn.setOnClickListener {
            ContextUtil.clear(mContext)
            val myIntent = Intent(mContext, LoginActivity::class.java )
            startActivity(myIntent)
            finish()
        }

    }

    override fun setValues() {

    }
}