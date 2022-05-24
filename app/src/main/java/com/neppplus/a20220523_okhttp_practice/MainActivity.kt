package com.neppplus.a20220523_okhttp_practice

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.neppplus.a20220523_okhttp_practice.Adapters.TopicRecyclerAdapter
import com.neppplus.a20220523_okhttp_practice.databinding.ActivityMainBinding
import com.neppplus.a20220523_okhttp_practice.models.TopicData
import com.neppplus.a20220523_okhttp_practice.utils.ContextUtil
import com.neppplus.a20220523_okhttp_practice.utils.ServerUtil
import org.json.JSONObject
import kotlin.math.log

class MainActivity : BaseActivity() {

    lateinit var binding : ActivityMainBinding
    lateinit var mTopicAdapter : TopicRecyclerAdapter

    val mTopicList = ArrayList<TopicData>()

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
        getTopicListFromServer()
        mTopicAdapter = TopicRecyclerAdapter(mContext,mTopicList)
        binding.topicRecyclerView.adapter = mTopicAdapter
        binding.topicRecyclerView.layoutManager = LinearLayoutManager(mContext)
    }

    fun getTopicListFromServer() {
        ServerUtil.getRequestMainInfo(mContext, object : ServerUtil.Companion.JsonResponseHandler{
            override fun onResponse(jsonObj: JSONObject) {
                val dataObj = jsonObj.getJSONObject("data")
                val topicArr = dataObj.getJSONArray("topics")

                for(i in 0 until topicArr.length()) {
                    // 배열에서 하나씩 계속 뽑아줘야 하는 과정
                    val topicObj = topicArr.getJSONObject(i)
                    //               ㄴ Json 파싱의 {} -> JSONArray -> JsonObject로 추출
                    Log.d("받아낸 주제",topicObj.toString())

//                    val topicData = TopicData()
//                    topicData.id = topicObj.getInt("id")
//                    topicData.title = topicObj.getString("title")
//                    topicData.imageUrl = topicObj.getString("img_url")
//                    topicData.replyCount = topicObj.getInt("reply_count")
                    val topicData = TopicData.getTopicDataFromJson(topicObj)
                    mTopicList.add (topicData)

                    runOnUiThread {
                        mTopicAdapter.notifyDataSetChanged()

                    }

                }
            }
        })

    }
}