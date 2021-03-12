package com.example.aria_demo.dialog

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.arialyy.aria.core.Aria
import com.arialyy.aria.core.common.HttpOption
import com.arialyy.aria.core.download.m3u8.M3U8VodOption
import com.arialyy.aria.core.processor.IVodTsUrlConverter
import com.arialyy.aria.core.task.DownloadGroupTask
import com.example.aria_demo.R
import java.lang.Exception
import java.util.ArrayList

class FileListAdapter(
    httpsDialogFragment: HttpsDialogFragment,
    data: MutableList<FileListEntity>
) : BaseAdapter(), ItemClickListener {
    private var itemclick: ItemClickListener? = null
    override fun onClick(v: View, position: Int) {
        when (v?.id) {
            R.id.btn_start -> {
                startDownload(position)
            }
            R.id.btn_cancel -> {
            }
        }
    }

    private fun startDownload(position: Int) {

        val item = mData[position]
        if (item.type == 0) {
            Toast.makeText(mActivity, "http任务启动", Toast.LENGTH_LONG).show()
            Aria.download(mFragment)
                .load(item.downloadUrl)
                .setFilePath(item.downloadPath)
                .create()
        } else if (item.type == 1) {
            Toast.makeText(mActivity, "m3u8任务启动", Toast.LENGTH_LONG).show()
            val option = M3U8VodOption()
            option.setVodTsUrlConvert(IVodTs())
            option.generateIndexFile()

            Aria.download(mFragment)
                .load(item.downloadUrl)
                .setFilePath(item.downloadPath, true)
                .option(HttpOption().useServerFileName(true))
                .m3u8VodOption(option)
                .create()
        }
    }

    private lateinit var tvSpeed: TextView
    private lateinit var progress: ProgressBar
    private var mData: MutableList<FileListEntity> = data
    private var mFragment: HttpsDialogFragment? = httpsDialogFragment
    private var mActivity: FragmentActivity? = httpsDialogFragment.activity

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        val inflate = LayoutInflater.from(mActivity)
            .inflate(R.layout.item_multiply, parent, false)

        var btnStart: Button = inflate.findViewById(R.id.btn_start)
        var btnCancel: Button = inflate.findViewById(R.id.btn_cancel)
        tvSpeed = inflate.findViewById(R.id.tv_speed)
        progress = inflate.findViewById(R.id.progress)


        btnStart.setOnClickListener {
            itemclick?.onClick(convertView!!, position)
        }
        return inflate
    }

    override fun getItem(position: Int): Any {
        return mData[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        Log.i("AAA", "AAA")
        return mData.size
    }


    /**
     * 留给前台控制修改下载状态
     * */
    fun onTaskCancel(task: DownloadGroupTask?) {
        for (data in mData) {
            if (data.downloadUrl == task?.key) {
                tvSpeed.text = task?.convertSpeed
                progress.progress = 0
                notifyDataSetChanged()
                break
            }
        }
    }

    fun onPre(task: DownloadGroupTask?) {
        for (data in mData) {
            if (data.downloadUrl == task?.key) {

                break
            }
        }
    }

    fun onTaskStart(task: DownloadGroupTask?) {
        for (data in mData) {
            if (data.downloadUrl == task?.key) {
                tvSpeed.text = task?.convertSpeed
                progress.progress = task!!.percent
                notifyDataSetChanged()
                break
            }
        }
    }

    fun onTaskPre(task: DownloadGroupTask?) {
        for (data in mData) {
            if (data.downloadUrl == task?.key) {
                tvSpeed.text = task?.convertSpeed
                progress.progress = task!!.percent
                break
            }
        }
    }

    fun onTaskRunning(task: DownloadGroupTask?) {
        for (data in mData) {
            if (data.downloadUrl == task?.key) {
                tvSpeed.text = task?.convertSpeed
                progress.progress = task!!.percent
                notifyDataSetChanged()
                break
            }
        }
    }

    fun onWait(task: DownloadGroupTask?) {
        for (data in mData) {
            if (data.downloadUrl == task?.key) {

                break
            }
        }
    }

    fun onTaskResume(task: DownloadGroupTask?) {
        for (data in mData) {
            if (data.downloadUrl == task?.key) {
                tvSpeed.text = task?.convertSpeed
                progress.progress = task!!.percent
                break
            }
        }
    }

    fun onNoSupportBreakPoint(task: DownloadGroupTask?) {

    }

    fun onTaskStop(task: DownloadGroupTask?) {
        for (data in mData) {
            if (data.downloadUrl == task?.key) {
                tvSpeed.text = task?.convertSpeed
                progress.progress = task!!.percent
                break
            }
        }
    }

    fun onTaskFail(task: DownloadGroupTask?, e: Exception?) {
        var i = 0
        for (data in mData) {
            if (data.downloadUrl == task?.key) {
                tvSpeed.text = task?.convertSpeed
                progress.progress = task!!.percent
                Toast.makeText(mActivity, mData[i].name + "下载失败", Toast.LENGTH_LONG).show()
                break
            }
            i++
        }
    }

    fun onPeerStart(m3u8Url: String?, peerPath: String?, peerIndex: Int) {
        Toast.makeText(mActivity, "m3u8下载开始", Toast.LENGTH_LONG).show()
    }

    fun onPeerFail(m3u8Url: String?, peerPath: String?, peerIndex: Int) {
        Toast.makeText(mActivity, "m3u8下载失败", Toast.LENGTH_LONG).show()
    }

    fun onPeerComplete(m3u8Url: String?, peerPath: String?, peerIndex: Int) {
        Toast.makeText(mActivity, "m3u8下载完成", Toast.LENGTH_LONG).show()
    }


    fun onTaskComplete(task: DownloadGroupTask?) {
        var i = 0
        for (data in mData) {
            if (data.downloadUrl == task?.key) {
                tvSpeed.text = task?.convertSpeed
                progress.progress = 100
                Toast.makeText(mActivity, mData[i].name + "下载完成", Toast.LENGTH_LONG).show()
                break
            }
            i++
        }
    }

    internal class FileListHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var btnStart: Button
        var btnCancel: Button
        var tvSpeed: TextView
        var progress: ProgressBar

        init {
            btnStart = itemView.findViewById(R.id.btn_start)
            btnCancel = itemView.findViewById(R.id.btn_cancel)
            tvSpeed = itemView.findViewById(R.id.tv_speed)
            progress = itemView.findViewById(R.id.progress)
        }
    }

    internal class IVodTs : IVodTsUrlConverter {

        override fun convert(m3u8Url: String, tsUrls: List<String>): List<String> {
            val temp = ArrayList<String>()
            for (tsUrl in tsUrls) {
                temp.add("http://qn.shytong.cn$tsUrl")
            }
            return temp
        }
    }

}


interface ItemClickListener {
    fun onClick(v: View, position: Int)
}

