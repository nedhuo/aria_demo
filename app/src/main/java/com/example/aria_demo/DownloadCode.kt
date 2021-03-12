package com.example.aria_demo

class DownloadCode {
    //0：未下载 1：下载中 2：暂停 3：已完成 4：下载失败
    companion object {
        val STATE_FREE = 0
        val STATE_DOWNING = 1
        val STATE_PAUSE = 2
        val STATE_DONE = 3
        val STATE_ERROR = 4
        val STATE_WAIT = 5
    }

}