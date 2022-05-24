package com.neppplus.a20220523_okhttp_practice

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.neppplus.a20220523_okhttp_practice.databinding.ActivityDetailBinding
import com.neppplus.a20220523_okhttp_practice.models.TopicData
import com.neppplus.a20220523_okhttp_practice.utils.ServerUtil
import org.json.JSONObject

class DetailActivity : BaseActivity() {

    lateinit var binding: ActivityDetailBinding

    var mTopicData = TopicData()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail)
        var mTopicData = intent.getSerializableExtra("topicData") as TopicData
        setupEvents()
        setValues()

    }

    override fun setupEvents() {

        // vote1 클릭 -> 해당 진영의 id 값을 찾아서 거기에 투표
        // 서버에 전달 -> API 활용

        binding.vote1Btn.setOnClickListener {

            // 서버의 투표 API 호출

            ServerUtil.postRequestVote(mContext, mTopicData.sideList[0].id, object : ServerUtil.Companion.JsonResponseHandler{
                override fun onResponse(jsonObj: JSONObject) {
                    val message = jsonObj.getString("message")
                    val code = jsonObj.getInt("code")

                    Toast.makeText(mContext,message,Toast.LENGTH_SHORT).show()

                    if ( code == 200 ) {
                        val dataObjects = jsonObj.getJSONObject("data")
                        val topicObjects = dataObjects.getJSONObject("topic")

                        mTopicData = TopicData.getTopicDataFromJson(topicObjects)

                        setTopicDataToUI()
                    }

                }
            })
        }

    }



    fun getTopicDetail () {
        ServerUtil.getTopicDetail(mContext, mTopicData.id, object : ServerUtil.Companion.JsonResponseHandler{
            override fun onResponse(jsonObj: JSONObject) {
                val dataObj = jsonObj.getJSONObject("data")
                val topicObj = dataObj.getJSONObject("topic")

                val topicData = TopicData.getTopicDataFromJson(topicObj)

                mTopicData = topicData

            }
        })
                }

    fun setTopicDataToUI() {
        binding.topicTitle.text = mTopicData.title
        Glide.with(mContext).load(mTopicData.imageUrl).into(binding.backgroundImg)
        binding.side1Txt.text = mTopicData.sideList[0].title
        binding.side2Txt.text = mTopicData.sideList[1].title
        binding.vote1CountTxt.text = "${mTopicData.sideList[0].voteCount}표"
        binding.vote2CountTxt.text = "${mTopicData.sideList[1].voteCount}표"
    }
}