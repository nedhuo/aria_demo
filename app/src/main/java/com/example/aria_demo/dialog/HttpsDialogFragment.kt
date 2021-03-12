package com.example.aria_demo.dialog

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ListView
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.arialyy.aria.core.Aria
import com.arialyy.aria.core.download.DownloadGroupTaskListener
import com.arialyy.aria.core.scheduler.M3U8PeerTaskListener
import com.arialyy.aria.core.task.DownloadGroupTask
import com.example.aria_demo.R
import kotlinx.android.synthetic.main.fragment_https.*
import java.lang.Exception
import java.util.ArrayList
import kotlin.math.log

/**
 * Author: nedhuo
 * Date  : 2021/3/10
 * email : nedhuo@163.com
 */

/**
 * Http列表下载
 *
 * 一个urls集合，点击单个下载则从集合中移除，让其下载，加入停止集合
 * 点击全部下载则将集合中剩余的url下载
 *
 * 任务组可以操纵停止单个
 *
 * */
class HttpsDialogFragment : DialogFragment(), View.OnClickListener, DownloadGroupTaskListener,
    M3U8PeerTaskListener{


    private lateinit var listView: ListView
    private lateinit var mAdapter: FileListAdapter
    internal var mData: MutableList<FileListEntity> = ArrayList<FileListEntity>()
    private val TAG: String = "HttpsDialogFragment"


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //注册
        Aria.download(this).register()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val createDialog = super.onCreateDialog(savedInstanceState)

        val layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        createDialog.addContentView(View(activity), layoutParams)
        val window = createDialog.window
        val attributes = window?.attributes
        attributes?.height = ViewGroup.LayoutParams.WRAP_CONTENT
        attributes?.width = ViewGroup.LayoutParams.MATCH_PARENT
        window?.attributes = attributes
        window?.setGravity(Gravity.BOTTOM)
        Log.i(TAG, "onCreateDialog")
        return createDialog
    }

    /**
     * 只有在onCreateView中返回View,才可以使用kotlin的控件属性名调用
     * */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater
            .inflate(R.layout.fragment_https, container, false)
        listView = view.findViewById(R.id.lv_https)
        val layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        view.layoutParams = layoutParams
        Log.i(TAG, "onCreateView")


        initData()
//        lv_https.setOnItemClickListener(AdapterView.OnItemClickListener { parent, view, position, id ->
//            Log.i(TAG, position.toString())
//
//        })
        return view
    }

    /**
     *
     * */
    private fun initData() {

        var entity: FileListEntity = FileListEntity()
        entity.downloadUrl = "https://rbv01.ku6.com/wifi/o_1dtm02cqgafe19331j2ijeksg7nkvs"
        entity.downloadPath = activity?.filesDir?.absolutePath + "/adasd"
        entity.name = "zhangsan"
        entity.type = 0
        mData.add(entity)
        entity = FileListEntity()
        entity.downloadUrl = "http://devimages.apple.com/iphone/samples/bipbop/bipbopall.m3u8"
        entity.downloadPath = activity?.filesDir?.absolutePath + "/vewefwE"
        entity.name = "lisi"
        entity.type = 1
        mData.add(entity)

        mAdapter = FileListAdapter(this, mData)
        listView.adapter = mAdapter
        mAdapter.notifyDataSetChanged()
    }


    override fun onDestroy() {
        super.onDestroy()
        Aria.download(this).unRegister()
    }

    override fun onClick(v: View?) {

    }

    /**
     * 调用执行adapter内的方法
     * */

    override fun onTaskCancel(task: DownloadGroupTask?) {
        mAdapter.onTaskCancel(task)
    }

    override fun onPre(task: DownloadGroupTask?) {
        mAdapter.onPre(task)
    }

    override fun onTaskStart(task: DownloadGroupTask?) {
        mAdapter.onTaskStart(task)
    }

    override fun onTaskPre(task: DownloadGroupTask?) {
        mAdapter.onTaskPre(task)
    }

    override fun onTaskRunning(task: DownloadGroupTask?) {
        Log.i("AAAAA", "AAAAA")
        mAdapter.onTaskRunning(task)
    }

    override fun onWait(task: DownloadGroupTask?) {
        mAdapter.onWait(task)
    }

    override fun onTaskResume(task: DownloadGroupTask?) {
        Log.i(TAG, "onTaskResume")
        mAdapter.onTaskResume(task)
    }

    override fun onNoSupportBreakPoint(task: DownloadGroupTask?) {
        mAdapter.onNoSupportBreakPoint(task)
    }

    override fun onTaskStop(task: DownloadGroupTask?) {
        Log.i(TAG, "onTaskStop")
        mAdapter.onTaskStop(task)
    }

    override fun onTaskFail(task: DownloadGroupTask?, e: Exception?) {
        mAdapter.onTaskFail(task, e)
    }

    override fun onTaskComplete(task: DownloadGroupTask?) {
        mAdapter.onTaskComplete(task)
    }


    override fun onPeerStart(m3u8Url: String?, peerPath: String?, peerIndex: Int) {
        Log.i(TAG, "onPeerStart")
        mAdapter.onPeerStart(m3u8Url, peerPath, peerIndex)
    }

    override fun onPeerFail(m3u8Url: String?, peerPath: String?, peerIndex: Int) {
        mAdapter.onPeerFail(m3u8Url, peerPath, peerIndex)
    }

    override fun onPeerComplete(m3u8Url: String?, peerPath: String?, peerIndex: Int) {
        mAdapter.onPeerComplete(m3u8Url, peerPath, peerIndex)
    }

}