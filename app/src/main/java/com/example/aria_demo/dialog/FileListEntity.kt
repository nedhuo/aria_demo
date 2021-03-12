package com.example.aria_demo.dialog

/**
 * 名字
 * 下载url
 * 存储路径
 * */
class FileListEntity {
    var name: String? = null
    var downloadUrl: String? = null
    var downloadPath: String? = null
    var type = -1
    /**
     * 0：普通任务
     * 1：m3u8任务
     */


    var urls: Array<String>? = null
    var names: Array<String>? = null
}
