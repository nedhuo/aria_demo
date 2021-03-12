package com.example.aria_demo.widget

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.arialyy.aria.core.Aria
import com.arialyy.aria.core.common.HttpOption
import com.arialyy.aria.core.download.DownloadTaskListener
import com.arialyy.aria.core.task.DownloadTask
import com.example.aria_demo.R
import kotlinx.android.synthetic.main.item_download.*
import java.lang.Exception

class MultiplyAdapter(context: Context, urls: List<String>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>(), DownloadTaskListener, View.OnClickListener {
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_start -> {
                if (Aria.download(mCtx).load(mTaskId).isRunning) {
                    Aria.download(mCtx).load(mTaskId).stop()
                } else {
                    if (mTaskId == -1L) {
                        mTaskId = Aria.download(mCtx)
                            .load(mUrls[mPosition])
                            //使用服务器端名字
                            .option(HttpOption().useServerFileName(true))
                            .setFilePath(mFilePath)    // 设置该组合任务的文件夹路径
                            .create()
                    } else {
                        Aria.download(mCtx).load(mTaskId).resume()
                    }
                }

            }
            R.id.btn_cancel -> {
                //cancel调用之后可能会出现问题，需要深入源码
                Aria.download(this).load(mTaskId).cancel()
            }
        }
    }

    private lateinit var btnStart: Button
    private var mPosition: Int = -1
    private var mFilePath: String = ""
    private var mTaskId: Long = -1L
    private lateinit var progressBar: ProgressBar
    private lateinit var tvSpeed: TextView

    override fun onTaskCancel(task: DownloadTask?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onPre(task: DownloadTask?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onTaskStart(task: DownloadTask?) {
        if (task?.key == mUrls[mPosition]) {


            tvSpeed.text = task?.convertSpeed
            btnStart.text = mCtx.getString(R.string.download_stop)
            progressBar.progress = 0
        }
    }

    override fun onTaskPre(task: DownloadTask?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onTaskRunning(task: DownloadTask?) {
        if (task?.key == mUrls[mPosition]) {
            val len: Long? = task.fileSize
            var progress = if (len == 0L) {
                0
            } else {
                task?.percent
            }

            progressBar.progress = progress
            tvSpeed.text = task.convertSpeed
            btnStart.text = mCtx.getString(R.string.download_start)
        }
    }

    override fun onWait(task: DownloadTask?) {

    }

    override fun onTaskResume(task: DownloadTask?) {
        if (task?.key == mUrls[mPosition]) {

            tvSpeed.text = task?.convertSpeed
            btnStart.text = mCtx.getString(R.string.download_stop)
            progressBar.progress = task?.percent!!
        }
    }

    override fun onNoSupportBreakPoint(task: DownloadTask?) {

    }

    override fun onTaskStop(task: DownloadTask?) {
        if (task?.key == mUrls[mPosition]) {


            progressBar.progress = task?.percent!!
            tvSpeed.text = ""
            btnStart.text = mCtx.getString(R.string.download_resume)
        }
    }

    override fun onTaskFail(task: DownloadTask?, e: Exception?) {
        if (task?.key == mUrls[mPosition]) {
            Toast.makeText(mCtx, mCtx.getString(R.string.toast_fail), Toast.LENGTH_SHORT)
                .show()

        }
    }

    override fun onTaskComplete(task: DownloadTask?) {
        if (task?.key == mUrls[mPosition]) {

            progressBar.progress = 100
            tvSpeed.text = ""
            btnStart.text = mCtx.getString(R.string.re_start)
        }
    }


    private var mUrls: List<String> = urls
    private var mCtx: Context = context

    init {
        //如果设置文件名字与服务器保持一致，此处应该可以随便设置
        mFilePath = mCtx.filesDir.absolutePath + "/aaa"
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(mCtx)
            .inflate(R.layout.item_multiply, parent, false)
        return MultiplyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mUrls.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        mPosition = position
        val itemView = holder.itemView
        btnStart = itemView.findViewById<Button>(R.id.btn_start)
        val btnCancel = itemView.findViewById<Button>(R.id.btn_cancel)
        progressBar = itemView.findViewById(R.id.progress)
        tvSpeed = itemView.findViewById(R.id.tv_speed)
        btnStart.setOnClickListener(this)
        btnCancel.setOnClickListener(this)
    }


    class MultiplyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    }
}