package com.neppplus.a20220523_okhttp_practice

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.neppplus.a20220523_okhttp_practice.utils.ContextUtil
import com.neppplus.a20220523_okhttp_practice.utils.ServerUtil
import org.json.JSONObject

class SplashActivity : BaseActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        setupEvents()
        setValues()

    }

    override fun setupEvents() {

    }

    override fun setValues() {
        val myHandler = Handler(Looper.getMainLooper())

        var isTokenOk = false
        ServerUtil.getRequestUserInfo(mContext , object : ServerUtil.Companion.JsonResponseHandler{
            override fun onResponse(jsonObj: JSONObject) {
                val code = jsonObj.getInt("code")
                isTokenOk = ( code == 200 ) } })

        myHandler.postDelayed({
            val isAutoLoginOk = ContextUtil.getAutoLogin(mContext)

                              //1. 사용자가 자동로그인을 한다고 했는가?
                              //2. token이 있는가? ( 토큰의 유효성 Text )
                              //3. 저장된 token이 있는가?
                              //           ㄴ 유효성 확인


            val myIntent : Intent

            if (isAutoLoginOk && isTokenOk ) {
                myIntent = Intent(mContext, MainActivity::class.java)
            }
            else {
                myIntent = Intent(mContext, LoginActivity::class.java)

            }
            startActivity(myIntent)
            finish()


        },2500)
    }
}