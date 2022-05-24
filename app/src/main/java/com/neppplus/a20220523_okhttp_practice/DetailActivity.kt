package com.neppplus.a20220523_okhttp_practice

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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

    }

    override fun setValues() {

        setTopicDataToUI()

        binding.side1Txt.text = mTopicData.title
        Glide.with(mContext).load(mTopicData.imageUrl).into(binding.backgroundImg)
        binding.side1Txt.text = mTopicData.sideList[0].title
        binding.side2Txt.text = mTopicData.sideList[1].title
        binding.vote1CountTxt.text = "${mTopicData.sideList[0].voteCount}표"
        binding.vote2CountTxt.text = "${mTopicData.sideList[1].voteCount}표"

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