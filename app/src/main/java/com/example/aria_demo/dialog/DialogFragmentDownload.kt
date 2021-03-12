package com.example.aria_demo.dialog

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.arialyy.aria.core.Aria
import com.arialyy.aria.core.common.HttpOption
import com.arialyy.aria.core.download.DownloadEntity
import com.arialyy.aria.core.download.DownloadTaskListener
import com.arialyy.aria.core.download.m3u8.M3U8VodOption
import com.arialyy.aria.core.inf.IEntity
import com.arialyy.aria.core.scheduler.M3U8PeerTaskListener
import com.arialyy.aria.core.task.DownloadTask
import com.arialyy.aria.util.ALog
import com.arialyy.aria.util.CommonUtil
import com.emmmm.nice.func_main.web.bean.VideoBean
import com.example.aria_demo.R
import com.example.aria_demo.bean.DataUtil
import com.example.aria_demo.widget.BandWidthUrlConverter
import java.lang.Exception


/**
 * Author: nedhuo
 * Date  : 2021/3/10
 * email : nedhuo@163.com
 */

/**
 * 摘离FragmentDialogDownload代码
 *
 * 一个m3u8 加 一个图片，图片应该是随着m3u8一块下载的
 *
 * 还是先测试一下回调注释
 *
 * m3u8链接与普通http的下载链接不一样，需要进行分开下载，通过匹配后缀
 * */
class DialogFragmentDownload : DialogFragment(), View.OnClickListener, DownloadTaskListener,
    M3U8PeerTaskListener {


    private val TAG: String = "DialogFragmentDownload"
    private var speed: String? = null
    private var mProgress: Int = 0
    private var mFileSize: String? = null
    private lateinit var stateStr: String
    private var mEntity: DownloadEntity? = null
    private var mFilePath: String = activity?.filesDir?.absolutePath + ""
    private var mUrl: String = "http://devimages.apple.com/iphone/samples/bipbop/bipbopall.m3u8"
    private var mTaskId: Long = -1L
    private lateinit var mVideoLists: List<VideoBean>
    //控件
    private var btnStart: Button? = null
    private var btnCancel: Button? = null
    private var tvSpeed: TextView? = null
    private var progressBar: ProgressBar? = null

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_start -> {
                if (mTaskId == -1L) {
                    mTaskId = Aria.download(this)
                        .load(mUrl)
                        .setFilePath(mFilePath)
                        .option(HttpOption().useServerFileName(true))
                        .ignoreFilePathOccupy()
                        .m3u8VodOption(getM3U8Option())
                        .create()
                } else {
                    Aria.download(this)
                        .load(mTaskId)
                        .m3u8VodOption(getM3U8Option())
                        .resume()
                }
            }
            R.id.btn_cancel -> {
                Aria.download(this).load(mTaskId).cancel(true)
                mTaskId = -1
            }
        }
    }

    private fun getM3U8Option(): M3U8VodOption? {
        val option = M3U8VodOption()
        //option.setBandWidth(200000);
        //.generateIndexFile()
        //.merge(true)
        //.setVodTsUrlConvert(new VodTsUrlConverter());
        //.setMergeHandler(new TsMergeHandler());
        option.setUseDefConvert(true)
        option.generateIndexFile()
        //option.setKeyUrlConverter(new KeyUrlConverter());
        //option.setVodTsUrlConvert(new VodTsUrlConverter());
        option.setBandWidthUrlConverter(BandWidthUrlConverter)
        //option.setUseDefConvert(true)
        return option
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val createDialog = super.onCreateDialog(savedInstanceState)
        val view = LayoutInflater.from(activity)
            .inflate(R.layout.fragment_dialogdownload, null, false)
        initView(view)
        val layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        createDialog.addContentView(view, layoutParams)
        val window = createDialog.window
        val attributes = window?.attributes
        attributes?.height = ViewGroup.LayoutParams.WRAP_CONTENT
        attributes?.width = ViewGroup.LayoutParams.MATCH_PARENT
        window?.attributes = attributes
        window?.setGravity(Gravity.BOTTOM)
        return createDialog
    }

    private fun initView(view: View?) {
        btnStart = view?.findViewById<Button>(R.id.btn_start)
        btnCancel = view?.findViewById<Button>(R.id.btn_cancel)
        tvSpeed = view?.findViewById<TextView>(R.id.tv_speed)
        progressBar = view?.findViewById<ProgressBar>(R.id.progress)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Aria.download(this).register()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onStart() {
        super.onStart()

        btnStart?.setOnClickListener(this)
        btnCancel?.setOnClickListener(this)


        //不限速
        Aria.download(this).setMaxSpeed(0)
        mVideoLists = DataUtil().loadData()
        checkUrl(mVideoLists)
    }

    /**
     * 测试链接是否能正常下载
     *
     * getFirstDownloadEntity()这个方法刚开始好像从来获取不到数据
     * */
    private fun checkUrl(lists: List<VideoBean>) {

            mEntity = Aria.download(this).getFirstDownloadEntity(mUrl)
            if (mEntity != null) {
                if (mEntity?.state == IEntity.STATE_RUNNING) {
                    stateStr = getString(R.string.download_stop)
                } else {
                    stateStr = getString(R.string.download_resume)
                }
                //处理文件大小和进度条
                mFileSize = CommonUtil.formatFileSize(mEntity?.fileSize!!.toDouble())
                mProgress = (mEntity?.currentProgress!! * 100 / mEntity?.fileSize!!).toInt()
            } else {
                Toast.makeText(activity, "下载链接异常", Toast.LENGTH_LONG).show()
            }

    }


    override fun onDestroyView() {
        super.onDestroyView()
        Aria.download(this).unRegister()
    }

    /**
     * m3u8点播源监听
     * */

    override fun onTaskCancel(task: DownloadTask?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onPre(task: DownloadTask?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    /**
     *
     * */
    override fun onTaskStart(task: DownloadTask?) {
        if (task?.key == mUrl) {
            ALog.d(TAG, "isComplete = " + task.isComplete + ", state = " + task.state)
            progressBar?.max = task.entity.m3U8Entity.peerNum
            btnStart?.text = getString(R.string.download_stop)
            tvSpeed?.text = task.convertSpeed
        }
    }

    override fun onTaskPre(task: DownloadTask?) {

    }

    override fun onTaskRunning(task: DownloadTask?) {
        if (task?.key == mUrl) {
            ALog.d(
                TAG,
                "m3u8 void running, p = " + task.percent + ", speed  = " + task.convertSpeed
            )
            progressBar?.max = task.percent
            btnStart?.text = getString(R.string.download_stop)
            tvSpeed?.text = task.convertSpeed
        }
    }

    override fun onWait(task: DownloadTask?) {
        if (task?.key == mUrl) {
            ALog.d(
                TAG,
                "m3u8 onWait, p = " + task.percent + ", speed  = " + task.convertSpeed
            )

        }
    }

    override fun onTaskResume(task: DownloadTask?) {
        if (task?.key == mUrl) {
            ALog.d(TAG, "m3u8 vod resume")

            progressBar?.max = task.percent
            btnStart?.text = getString(R.string.download_stop)
            tvSpeed?.text = task.convertSpeed
        }
    }

    override fun onNoSupportBreakPoint(task: DownloadTask?) {

    }

    override fun onTaskStop(task: DownloadTask?) {
        if (task?.key == mUrl) {
            ALog.d(TAG, "m3u8 vod resume")

            progressBar?.progress = task.percent
            btnStart?.text = getString(R.string.download_stop)
            tvSpeed?.text = task.convertSpeed
        }
    }

    override fun onTaskFail(task: DownloadTask?, e: Exception?) {
        if (task?.key == mUrl) {
            ALog.d(TAG, "m3u8 vod onTaskFail")

            progressBar?.progress = task.percent
            btnStart?.text = getString(R.string.download_resume)
            tvSpeed?.text = task.convertSpeed
        }
    }

    override fun onTaskComplete(task: DownloadTask?) {
        if (task?.key == mUrl) {
            ALog.d(TAG, "m3u8 vod onTaskFail")

            progressBar?.progress = task.percent
            btnStart?.text = getString(R.string.download_resume)
            tvSpeed?.text = task.convertSpeed
        }
    }

    /**
     * m3u8直播源监听
     * */
    override fun onPeerStart(m3u8Url: String?, peerPath: String?, peerIndex: Int) {
        Log.i(TAG, m3u8Url + ":peerIndex:" + peerIndex + ":peerPath:" + peerPath)
        Toast.makeText(activity, "开始下载", Toast.LENGTH_LONG).show()
    }

    override fun onPeerFail(m3u8Url: String?, peerPath: String?, peerIndex: Int) {
        Log.i(TAG, m3u8Url + ":peerIndex:" + peerIndex + ":peerPath:" + peerPath)
        Toast.makeText(activity, "下载失败", Toast.LENGTH_LONG).show()
    }

    override fun onPeerComplete(m3u8Url: String?, peerPath: String?, peerIndex: Int) {
        Log.i(TAG, m3u8Url + ":peerIndex:" + peerIndex + ":peerPath:" + peerPath)
        Toast.makeText(activity, "下载完成", Toast.LENGTH_LONG).show()
    }
}

