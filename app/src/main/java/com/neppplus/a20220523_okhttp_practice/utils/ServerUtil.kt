package com.neppplus.a20220523_okhttp_practice.utils

import android.content.Context
import android.util.Log
import android.widget.Toast
import okhttp3.*
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import org.json.JSONObject
import java.io.IOException

class ServerUtil {

//    서버에 Request 날리는 역할
//    함수를 만들려고 하는데, 어떤 객체가 실행해도 결과만 잘 나오면 그만임 함수
//    코틀린에서 JAVA의 static에 해당하는 개념? Companion object {  } 에 만들자

    companion object {

        //    서버유틸로 돌아온 응답을 => 액티비티에서 처리하도록, 일처리 넘기기
//    나에게 생긴 일을 > 다른 클래스에게 처리 요청 : interface 활용
        interface JsonResponseHandler {
            fun onResponse(jsonObj: JSONObject)
        }

        //        서버 컴퓨터 주소만 변수로 저장 (관리 일원화) => 외부 노출 X
        private val BASE_URL = "http://54.180.52.26"

        //        로그인 기능 호출 함수
        fun postRequestLogin(email: String, pw: String, handler: JsonResponseHandler?) {

//            Request 제작 -> 실제 호출 -> 서버의 응답을 화면에 전달
//            제작 1)어느 주소 (url)로 접근 할지? => 서버주소 + 기능 주소
            val urlString = "${BASE_URL}/user"

//            제작 2) 파라미터 담아두기 => 어떤 이름표 / 어느 공간에
            val formData = FormBody.Builder()
                .add("email", email)
                .add("password", pw)
                .build()

//            제작 3) 모든 Request 정보를 종합한 객체 생성 ( 어떤 주소로 + 어떤 메쏘드로 + 어떤 파라미터로 )
            val request = Request.Builder()
                .url(urlString)
                .post(formData)
                .build()

//            종합한 Request도 실제로 호출을 해줘야 API 호출이 실행(Intent - startActivity 같은 동작 필요)
//            실제 호출 : 앱이 클라이언트로써 동작 > OkHttpClient 클래스활용
            val client = OkHttpClient()

//            OkHttpClient 객체 이용 > 서버에 로그인 기능 실제 호출
//            호출을 했으면, 서버가 수행한 결과(Response)를 받아서 처리
//              => 서버에 다녀와서 할 일을 등록 : enqueue ( Callback )
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
//                    실패 : 서버 연결 자체를 실패, 응답이 아예 오지 않았다.
//                    ex. 인터넷 끊김, 서버 접속 불가 등등 물리적 연결 실패
//                    ex. 비번 틀려서 로그인 실패 : 서버 연결 성공 > 응답도 돌아왔는데 > 내용만 실패 (물리적 실패 X)
                }

                //
                override fun onResponse(call: Call, response: Response) {
//                    어떤 내용이던, 응답자체가 잘 돌아온 경우 (그 내용은 성공 / 실패 일 수 있다.)

//                    응답 : response 변수 > 응답의 본문(body)만 보자

                    val bodyString =
                        response.body!!.string()  // OkHttp는 toString 사용시 이상하게 출력! 꼭!! .string()을 활용해서 작업진행
                    val jsonObj = JSONObject(bodyString)  // 1회용 딱 한번만 사용할 수 있다.

                    Log.d("서버테스트", jsonObj.toString())

//                    연습 : 로그인 성공 / 실패에 따른 로그 추출
//                    "code" 이름표의 Int를 추출, 그 값을 조건문 호가인
//                    val code = jsonObj.getInt("code")
//
//                    if (code == 200) {
////                        로그인 시도 성공
//                        Log.d("로그인 시도", "성공")
//                        val dataObj = jsonObj.getJSONObject("data")
//                        val userObj = dataObj.getJSONObject("user")
//                        val nickname = userObj.getString("nick_name")
//
//                        Log.d("로그인 성공", "닉네임 : $nickname")
//                    }
//                    else {
//                        Log.d("로그인 시도", "실패")
//                        val message = jsonObj.getString("message")
//                        Log.d("실패 사유", message)
//                    }

//                    실제 : handler 변수에 jsonObj를 가지고 화면에서 어떻게 처리할지 계획이 들어있다.
//                    (계획이 있을때만) 해당 계획을 실행하자
                    handler?.onResponse(jsonObj)
                }
            })


        }

        //        회원 가입 기능 호출 함수
        fun putRequestSignUp(
            email: String,
            pw: String,
            nickname: String,
            handler: JsonResponseHandler?
        ) {

            val urlString = "${BASE_URL}/user"

            val formData = FormBody.Builder()
                .add("email", email)
                .add("password", pw)
                .add("nick_name", nickname)
                .build()

            val request = Request.Builder()
                .url(urlString)
                .put(formData)
                .build()

            val client = OkHttpClient()

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {

                }

                override fun onResponse(call: Call, response: Response) {
                    val bodyString = response.body!!.string()

                    val jsonObj = JSONObject(bodyString)

                    handler?.onResponse(jsonObj)
                }
            })

        }

        //        중복 검사 기능 호출 함수
        fun getRequestUserCheck(type: String, value: String, handler: JsonResponseHandler?) {
//            val urlBuilder = HttpUrl.parse("${BASE_URL}/uer_check")
//            => toHttpUrlOrNull이 자동완성 안될경우 작성후 에러 수정(alt + enter)
            val urlBuilder = "${BASE_URL}/user_check".toHttpUrlOrNull()!!.newBuilder()
                .addEncodedQueryParameter("type", type)
                .addEncodedQueryParameter("value", value)
                .build()

            val urlString = urlBuilder.toString()

//            Log.d("완성된 url", urlString)

            val request = Request.Builder()
                .url(urlString)
                .get()
                .build()

            val client = OkHttpClient()

            client.newCall(request).enqueue(object : Callback{
                override fun onFailure(call: Call, e: IOException) {

                }

                override fun onResponse(call: Call, response: Response) {
                    val bodyString = response.body!!.string()
                    val jsonObj = JSONObject(bodyString)
                    Log.d("서버응답", jsonObj.toString())

                    handler?.onResponse(jsonObj)
                }
            })
        }

        // 사용자 정보 조회 (Token 값의 유효성 검사 )

        fun getRequestUserInfo(context: Context, handler: JsonResponseHandler?) {
            val token = ContextUtil.getLoginToken(context)
            val urlString = "${BASE_URL}/user"
            val request = Request.Builder()
                .url(urlString)
                .get()
                .header("X-Http-Token",token)
                .build()

            val client = OkHttpClient()

            client.newCall(request).enqueue(object : Callback{
                override fun onFailure(call: Call, e: IOException) {

                }

                override fun onResponse(call: Call, response: Response) {
                    val jsonObj = JSONObject(response.body!!.string())
                    handler?.onResponse(jsonObj)

                }
            })


        }
        fun getRequestUserIngo() {

        }

        fun getRequestMainInfo(context: Context, handler : JsonResponseHandler? ) {
            val token = ContextUtil.getLoginToken(context)
            val urlString = "${BASE_URL}/v2/main_info"
            val request = Request.Builder()
                .url(urlString)
                .get()
                .header("X-Http-Token",token)
                .build()

            val client = OkHttpClient()
            client.newCall(request).enqueue(object : Callback{
                override fun onFailure(call: Call, e: IOException) {

                }

                override fun onResponse(call: Call, response: Response) {
                    val jsonObj = JSONObject(response.body!!.string())
                    handler?.onResponse(jsonObj)

                }
            })

        }

        fun getTopicDetail(context: Context,topicId : Int, handler : JsonResponseHandler? ) {
            val token = ContextUtil.getLoginToken(context)
            val urlString = "${BASE_URL}/topic/${topicId}"
            val request = Request.Builder()
                .url(urlString)
                .get()
                .header("X-Http-Token",token)
                .build()

            val client = OkHttpClient()
            client.newCall(request).enqueue(object : Callback{
                override fun onFailure(call: Call, e: IOException) {

                }

                override fun onResponse(call: Call, response: Response) {
                    val jsonObj = JSONObject(response.body!!.string())
                    handler?.onResponse(jsonObj)

                }
            })

        }

        fun postRequestVote ( context: Context, sideId : Int, handler : JsonResponseHandler?) {
            val token = ContextUtil.getLoginToken(context)
            val urlString = "${BASE_URL}/topic_vote"
            val formData = FormBody.Builder()
                .add("side_id",sideId.toString())
                .build()

            val request = Request.Builder()
                .url(urlString)
                .post(formData)
                .header("X-Http-Token",token)
                .build()

            val client = OkHttpClient()

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {

                }

                override fun onResponse(call: Call, response: Response) {
                    val jsonObj = JSONObject(response.body!!.string())

                }

            })
        }
    }

}











