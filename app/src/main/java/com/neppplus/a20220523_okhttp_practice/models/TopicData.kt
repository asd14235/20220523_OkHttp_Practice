package com.neppplus.a20220523_okhttp_practice.models

import org.json.JSONObject
import java.io.Serializable

class TopicData : Serializable {

    var id = 0
    var title = ""
    var imageUrl = ""
    var replyCount = 0

    val sideList = ArrayList<SideData>()

    // 주제 정보를 담고있는 JSONOBJECT가 들어오면 TopicData가 로 변환해 주는 함수 -> Static 메쏘드

    companion object {
        fun getTopicDataFromJson(jsonObj : JSONObject) : TopicData {
            val topicData = TopicData()

            topicData.id = jsonObj.getInt("id")
            topicData.title = jsonObj.getString("title")
            topicData.imageUrl = jsonObj.getString("img_url")
            topicData.replyCount = jsonObj.getInt("reply_count")

            val SideArr = jsonObj.getJSONArray("sides")

            for ( i in 0 until SideArr.length()) {

            }
            return topicData
        }
    }
}