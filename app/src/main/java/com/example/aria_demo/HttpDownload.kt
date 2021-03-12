package com.example.aria_demo

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.arialyy.aria.core.Aria
import com.arialyy.aria.core.common.HttpOption
import com.arialyy.aria.core.download.DownloadEntity
import com.arialyy.aria.core.download.DownloadTaskListener
import com.arialyy.aria.core.task.DownloadTask
import com.example.aria_demo.widget.HttpDownloadModule
import kotlinx.android.synthetic.main.item_download.*

/**
 *  每一个下载任务都有一个TaskId,ARIA通过这个taskId来判断当前任务来区分各个下载任务
 *  并且下载推退出之后，重新进入下载页面点击下来，会自动读取之前的下载记录（具体实现原理未查找）
 *
 *  另外 下载完成之后或者点击cancel之后, restart 都会出现异常，具体原因需要查看底层实现
 * */
class HttpDownload : AppCompatActivity(), View.OnClickListener, DownloadTaskListener {
    private lateinit var stateStr: String
    private lateinit var httpDownloadInfo: DownloadEntity
    private lateinit var mModule: HttpDownloadModule
    private var progress: Int? = 0
    private var TAG: String = "HttpDownload"

    private var mUrl: String? = null
    private var mFilePath: String? = null
    private var mTaskId: Long = -1
    /**
     * 第一次点击返回的系统时间
     */
    private var mFirstClickTime: Long = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.item_download)
        init(savedInstanceState)
    }


    fun init(savedInstanceState: Bundle?) {
        initView()

        initData()

        Aria.download(this).register()
        //返回转换后的单位速度
        Aria.get(this).downloadConfig.isConvertSpeed = true
        //设置限速4096
        Aria.download(this).setMaxSpeed(4096)

        mModule = HttpDownloadModule()
        httpDownloadInfo = mModule.getHttpDownloadInfo(this)
        mUrl = httpDownloadInfo.url
    }

    private fun initData() {
        mFilePath = filesDir.absolutePath + "/aaa.zip"

        title = "单任务下载"

        item_progressBar.max = 100
        item_progressBar.progress = 0

    }

    private fun initView() {
        btn_start.setOnClickListener(this)
        btn_stop.setOnClickListener(this)
    }


    override fun onClick(view: View) {
        when (view.id) {
            R.id.btn_start ->
                if (Aria.download(this).load(mTaskId).isRunning) {
                    Aria.download(this)
                        .load(mTaskId)
                        .option(HttpOption().useServerFileName(true))
                        .stop()
                } else {
                    startDownload()
                }
            R.id.btn_stop ->
                // Aria.download(this).load(mTaskId).stop()
                Aria.download(this).load(mTaskId).cancel(true)
        }
    }

    fun startDownload() {
        if (mTaskId == -1L) {

            mTaskId = Aria.download(this)
                .load(mUrl!!)
                .setFilePath(mFilePath!!, true)
                .create()
        } else {
            Aria.download(this)
                .load(mTaskId)
                .resume()
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        //unregisterReceiver(receiver);
        Aria.download(this).unRegister()
    }


//     + "1、你可以在注解中增加链接，用于指定被注解的方法只能被特定的下载任务回调，以防止progress乱跳\n"
//     + "2、当遇到网络慢的情况时，你可以先使用onPre()更新UI界面，待连接成功时，再在onTaskPre()获取完整的task数据，然后给UI界面设置正确的数据\n"
//     + "3、你可以在界面初始化时通过Aria.download(this).load(URL).getPercent()等方法快速获取相关任务的一些数据")


    override fun onTaskCancel(task: DownloadTask?) {
        if (task?.key == mUrl) {
            stateStr = getString(R.string.download_start)

            tv_speed.text = ""
            btn_start.text = stateStr
            item_progressBar.progress = 0
        }
    }

    /**
     * 这个函数会在其他函数回调前都会去调用
     * */
    override fun onPre(task: DownloadTask?) {
        if (task?.key == mUrl) {


        }
    }

    override fun onTaskStart(task: DownloadTask?) {
        if (task?.key == mUrl) {
            stateStr = getString(R.string.download_stop)

            tv_speed.text = task?.convertSpeed
            btn_start.text = stateStr
            item_progressBar.progress = 0
        }
    }

    override fun onTaskPre(task: DownloadTask?) {

    }

    override fun onTaskRunning(task: DownloadTask?) {
        if (task?.key == mUrl) {
            val len: Long? = task?.fileSize
            progress = if (len == 0L) {
                0
            } else {
                task?.percent
            }

            item_progressBar.progress = this.progress!!
            tv_speed.text = task?.convertSpeed
            btn_start.text = stateStr
        }
    }

    override fun onWait(task: DownloadTask?) {
        if (task?.key == mUrl) {
        }
    }

    override fun onTaskResume(task: DownloadTask?) {
        if (task?.key == mUrl) {
            stateStr = getString(R.string.download_stop)

            tv_speed.text = task?.convertSpeed
            btn_start.text = stateStr
            item_progressBar.progress = task?.percent!!
        }
    }

    override fun onNoSupportBreakPoint(task: DownloadTask?) {

    }

    override fun onTaskStop(task: DownloadTask?) {
        if (task?.key == mUrl) {
            stateStr = getString(R.string.download_resume)

            item_progressBar.progress = task?.percent!!
            tv_speed.text = ""
            btn_start.text = stateStr
        }
    }

    override fun onTaskFail(task: DownloadTask?, e: java.lang.Exception?) {
        if (task?.key == mUrl) {
            Toast.makeText(this, getString(R.string.toast_fail), Toast.LENGTH_SHORT)
                .show()
            stateStr = getString(R.string.download_start)
        }
    }


    /**
     * 下载完成
     *
     *      修改下载按钮状态
     *      修改当前速度
     *      修改进度为完成
     *      提示下载完成信息
     *
     * */
    override fun onTaskComplete(task: DownloadTask?) {
        if (task?.key == mUrl) {
            stateStr = getString(R.string.re_start)


            item_progressBar.progress = 100
            tv_speed.text = ""
            btn_start.text = stateStr
        }
    }

}