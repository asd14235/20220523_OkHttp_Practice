package com.neppplus.a20220523_okhttp_practice.models

import org.json.JSONObject
import java.io.Serializable

class SideData : Serializable {

    var id = 0
    var title = ""
    var voteCount = 0

    companion object {
        fun getSideDataFromJson ( jsonObject: JSONObject) : SideData {

            val sideData = SideData()

            sideData.id = jsonObject.getInt("id")
            sideData.title = jsonObject.getString("title")

            sideData.voteCount = jsonObject.getInt("vote_count")

            return sideData
        }

    }


}