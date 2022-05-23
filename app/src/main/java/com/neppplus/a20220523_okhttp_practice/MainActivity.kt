package com.neppplus.a20220523_okhttp_practice

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.neppplus.a20220523_okhttp_practice.databinding.ActivityMainBinding
import com.neppplus.a20220523_okhttp_practice.utils.ServerUtil
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

class MainActivity : AppCompatActivity() {

    lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setupEvent()
        setValues()
    }

    fun setupEvent() {
        binding.loginBtn.setOnClickListener {
            val inputEmail = binding.emailEdt.text.toString()
            val inputPw = binding.passwordEdt.text.toString()

            ServerUtil.postRequestLogin(inputEmail, inputPw, object : ServerUtil.Companion.JsonResponseHandler{
                override fun onResponse(jsonObj: JSONObject) {

//                    UI 입장에서, 로그인 결과를 받아서 처리할 코드
//                    서버에 다녀오고 나서 실행 : OkHttp 라이브러리가 자동으로 백그라운드에서 돌도록 만들어둔 코드
                    val code = jsonObj.getInt("code")

                    if (code == 200) {

                        runOnUiThread {
                            Toast.makeText(this@MainActivity, "로그인 성공", Toast.LENGTH_SHORT).show()
                        }
                    }
                    else {
                        val message = jsonObj.getString("message")

                        runOnUiThread {
                            Toast.makeText(this@MainActivity, message, Toast.LENGTH_SHORT).show()
                        }
                    }

                }
            })

        }
    }

    fun setValues() {

    }
}