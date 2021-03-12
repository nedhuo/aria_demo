package com.example.aria_demo


import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.arialyy.aria.core.Aria
import com.arialyy.aria.core.download.DownloadGroupTaskListener
import com.arialyy.aria.core.task.DownloadGroupTask
import kotlinx.android.synthetic.main.activity_multiply.*
import java.lang.Exception
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.arialyy.aria.core.common.HttpOption
import com.arialyy.aria.core.download.DownloadEntity
import com.example.aria_demo.widget.MultiplyAdapter
import java.io.File


/**
 * 组合任务下载
 *
 * 每个任务生成一个taskId,自主控制
 * 再把所有任务生成一个taskId,同时控制
 *
 * 组合任务下载，这里调用所有的任务单个下载
 *.组合任务会将url集合中的所有文件保存在同一个文件夹中，因此创建任务的时候，你需要设置的文件保存路径是文件夹路径。
 * 如果你不知道url集合中所有文件的总长度，那么你需要使用unknownSize() 告诉Aria框架。
 *
 * 对于只任务非常多的情况，非常不建议使用#unknownSize()，这将会消耗更多的时间才能进入下载流程
 * */

/**
 * 批量下载的两种实现思考
 *
 * 如果批量下载中使用单个下载的话
 * 需要解决一个问题，当url已经在下载的时候，
 * 则不再进行操作,
 * 想实现的话就需要给每一个url设置一个是否下载的状态码，实现稍微繁琐
 *
 * 思考打包下载
 * 在打包下载情况下，对每一个单独下载的文件控制能力有限，只能控制其暂停或者继续，不能取消下载或者删除
 * */

class MultiplyDownload : AppCompatActivity(), View.OnClickListener {
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_allStart -> {

            }

            R.id.btn_allCancel -> {
                val taskId = Aria.download(this)
                    .loadGroup(mUrls) // 设置url集合
                    .setDirPath(mFilePath)    // 设置该组合任务的文件夹路径
                    .unknownSize()            // 如果你不知道组合任务的长度请设置这个，需要注意的是，恢复任务时也有加上这个
                    .create()
            }

        }
    }

    private lateinit var mFilePath: String
    private val mUrls: List<String> = mutableListOf(
        "https://dldir1.qq.com/qqfile/qq/PCTIM/TIM3.3.5/TIM3.3.5.22018.exe",
        "https://down.qq.com/qqweb/TIM/android_apk/tim.apk",
        "https://down.qq.com/qqweb/PCQQ/PCQQ_EXE/PCQQ2021.exe",
        "https://pacakge.cache.wpscdn.cn/wps/download/W.P.S.10314.12012.2019.exe"
    )
    private val TAG: String = "MultiplyDownload"
    private var taskGroupId: Long = -1L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_multiply)
        init()
    }

    private fun init() {
        rv_multiply.layoutManager = LinearLayoutManager(this)
        rv_multiply.adapter = MultiplyAdapter(this, mUrls)

        //下载路径
        mFilePath = filesDir.absolutePath

        Aria.download(this).register()
        title = "多任务下载"

        //设置按钮点击事件
        btn_allStart.setOnClickListener(this)
        btn_allCancel.setOnClickListener(this)

        for (url in this.mUrls!!) {
            var entity = Aria.download(this).getFirstDownloadEntity(url)

            if (entity != null) {
                Log.i(
                    TAG,
                    "url:" + entity.realUrl +
                            "fileName:" + entity.serverFileName +
                            "fileSize:" + entity.convertFileSize
                )
            } else {
                entity = DownloadEntity()
                //此处没有设置文件名，后期需要文件名与服务器端保持一致
                entity.url = url
                val file = File(mFilePath)
                entity.filePath = mFilePath
                entity.fileName = file.name
            }
        }

    }

    fun startDownload(url: String) {
        val taskId = Aria.download(this)
            .load(url) // 下载地址
            .option(HttpOption().useServerFileName(true))
            .setFilePath(mFilePath) // 设置文件保存路径
            .create()
    }


    /**
     * 需要注意的是
     * aria 在activity中注册，在adapter中使用
     * */
    override fun onDestroy() {
        super.onDestroy()
        Aria.download(this).unRegister()
    }
}