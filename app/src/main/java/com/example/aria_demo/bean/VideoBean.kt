package com.emmmm.nice.func_main.web.bean

/**
 * Author: nedhuo
 * Date  : 2021/3/10
 * email : nedhuo@163.com
 */

/**
 * Aria方面多个视频下载的话，不知道文件大小加载时间会比较长
 * */
class VideoBean {
    /**
     * poster : https://rbv01.ku6.com/wifi/o_1dtm02cqhmq9cue1d201h51hh5o
     * videoUrl : https://rbv01.ku6.com/wifi/o_1dtm02cqgafe19331j2ijeksg7nkvs
     * videoId : M0cn22-SPc_6_I3kdiY6xd4r4pg.
     * title : 《辛弃疾1162》精彩片花-竖版
     */

    private var poster: String? = null
    private var videoUrl: String? = null
    private var videoId: String? = null
    private var title: String? = null

    fun getPoster(): String? {
        return poster
    }

    fun setPoster(poster: String) {
        this.poster = poster
    }

    fun getVideoUrl(): String? {
        return videoUrl
    }

    fun setVideoUrl(videoUrl: String) {
        this.videoUrl = videoUrl
    }

    fun getVideoId(): String? {
        return videoId
    }

    fun setVideoId(videoId: String) {
        this.videoId = videoId
    }

    fun getTitle(): String? {
        return title
    }

    fun setTitle(title: String) {
        this.title = title
    }
}