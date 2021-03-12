package com.example.aria_demo.bean

import com.emmmm.nice.func_main.web.bean.VideoBean

/**
 * Author: nedhuo
 * Date  : 2021/3/10
 * email : nedhuo@163.com
 */

/**
 * poster : https://rbv01.ku6.com/wifi/o_1dtm02cqhmq9cue1d201h51hh5o
 * videoUrl : https://rbv01.ku6.com/wifi/o_1dtm02cqgafe19331j2ijeksg7nkvs
 * videoId : M0cn22-SPc_6_I3kdiY6xd4r4pg.
 * title : 《辛弃疾1162》精彩片花-竖版
 */
class DataUtil {
    fun getDataUtil(): DataUtil {
        return DataUtil()
    }

    fun loadData(): List<VideoBean> {
        var entity = VideoBean()
        entity.setTitle("《辛弃疾1162》精彩片花-竖版")
        entity.setPoster("https://rbv01.ku6.com/wifi/o_1dtm02cqhmq9cue1d201h51hh5o")
        entity.setVideoId("M0cn22-SPc_6_I3kdiY6xd4r4pg.")
        entity.setVideoUrl("https://rbv01.ku6.com/wifi/o_1dtm02cqgafe19331j2ijeksg7nkvs")
        val list = mutableListOf<VideoBean>()
        list.add(entity)
        return list
    }
}