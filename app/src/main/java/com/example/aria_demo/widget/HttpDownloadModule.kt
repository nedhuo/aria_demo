package com.example.aria_demo.widget

import android.content.Context
import android.os.Environment
import android.text.TextUtils
import com.arialyy.aria.core.Aria
import com.arialyy.aria.core.download.DownloadEntity
import com.arialyy.aria.util.ALog
import java.io.File

/**
 * 注解不生效问题解决
 *
 * 3.8.12版本起，支持通过实现监听器来回调下载进度更新
 *
 * */
class HttpDownloadModule {
    private val TAG = "HttpDownloadModule"

    private val defUrl = "http://hzdown.muzhiwan.com/2017/05/08/nl.noio.kingdom_59104935e56f0.apk"
    private val defFilePath =
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).path + "/update.zip"


    private var singDownloadInfo: DownloadEntity? = null

    /**
     * 单任务下载的信息
     */
    internal fun getHttpDownloadInfo(context: Context): DownloadEntity {
        var url = "http://devimages.apple.com/iphone/samples/bipbop/bipbopall.m3u8"
        // val url = "https://y.qq.com/download/import/QQMusic-import-1.2.1.zip"
        val filePath = "/mnt/sdcard/qq.zip"

        singDownloadInfo = Aria.download(context).getFirstDownloadEntity(url)
        if (singDownloadInfo == null) {
            //当前没有该任务的下载记录或者url为null
            singDownloadInfo = DownloadEntity()
            singDownloadInfo!!.url = url
            val file = File(filePath)
            singDownloadInfo!!.filePath = filePath
            singDownloadInfo!!.fileName = file.name
        } else {
        }

        return singDownloadInfo as DownloadEntity
    }

    /**
     * 更新文件保存路径
     *
     * @param filePath 文件保存路径
     */
    internal fun updateFilePath(context: Context, filePath: String) {
        if (TextUtils.isEmpty(filePath)) {
            ALog.e(TAG, "文件保存路径为空")
            return
        }
        val temp = File(filePath)
        // AppUtil.setConfigValue(context, HTTP_PATH_KEY, filePath)
        singDownloadInfo!!.fileName = temp.name
        singDownloadInfo!!.filePath = filePath

    }

    /**
     * 更新url
     */
    internal fun uploadUrl(context: Context, url: String) {
        if (TextUtils.isEmpty(url)) {
            ALog.e(TAG, "下载地址为空")
            return
        }
        //存入sharedprefenerce
        //AppUtil.setConfigValue(context, HTTP_URL_KEY, url)
        singDownloadInfo!!.url = url
    }
}