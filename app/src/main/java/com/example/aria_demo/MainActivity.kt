package com.example.aria_demo

import android.Manifest
import android.content.Context
import android.content.Intent
import kotlinx.android.synthetic.main.activity_main.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View

import android.view.ViewGroup

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.aria_demo.m3u8.M3u8VodDownload
import kotlinx.android.synthetic.main.item_text.view.*

import android.Manifest.permission.WRITE_EXTERNAL_STORAGE

import androidx.core.app.ActivityCompat
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat







class MainActivity : AppCompatActivity() {

    private val codeId: Int = 111
    private var context: Context = this
    private var videoUrl = "https://rbv01.ku6.com/wifi/o_1evgq63in1sfq14s21tu4bt61ohbg"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initPermission()

        rv_main.adapter = MainRvAdapter(this)
        rv_main.layoutManager = LinearLayoutManager(this)
        (rv_main.adapter as MainRvAdapter).setOnItemClickListener(object :
            MainRvAdapter.OnItemClickListener {
            override fun onItemClick(view: View, position: Int) {
                jumpPage(position)
            }

            private fun jumpPage(position: Int) {

                when (position) {
                    0 -> intent = Intent(context, HttpDownload::class.java)
                    1 -> intent = Intent(context, MultiplyDownload::class.java)
                    2 -> intent = Intent(context, HttpDownload::class.java)
                    3 -> intent = Intent(context, M3u8VodDownload::class.java)
                    4 -> intent = Intent(context, HttpDownload::class.java)
                    5 -> intent = Intent(context, HttpDownload::class.java)
                    6 -> intent = Intent(context, FragmentDialogDownload::class.java)

                    else -> intent = Intent(context, HttpDownload::class.java)

                }
                startActivity(intent)
            }
        })
    }

    private fun initPermission() {
        val hasWriteStoragePermission = ContextCompat.checkSelfPermission(
            application,
            WRITE_EXTERNAL_STORAGE
        )
        if (hasWriteStoragePermission == PackageManager.PERMISSION_GRANTED) {
            //拥有权限，执行操作
        } else {
            //没有权限，向用户请求权限
            ActivityCompat.requestPermissions(
                this,
                arrayOf(WRITE_EXTERNAL_STORAGE),
                codeId
            )
        }
    }



}


class MainRvAdapter(private val context: Context) : RecyclerView.Adapter<MainRvAdapter.Holder>() {

    private lateinit var onItemClickListener: OnItemClickListener
    private val mData = mutableListOf(
        "HTTP下载",
        "多任务下载",
        "FTP下载",
        "M3U8点播文件下载",
        "M3U8直播文件下载",
        "SFTP下载",
        "DialogFragment下载"
    )


    override fun onBindViewHolder(holder: Holder, position: Int) {

//        val textView: TextView = holder.itemView as TextView
//        textView.text = mData[position]

        holder.itemView.tv_title.text = mData[position]
        holder.itemView.setOnClickListener {
            onItemClickListener.onItemClick(holder.itemView, position)
        }

    }

    override fun getItemCount(): Int {
        return mData.size
    }

    /**
     * 在RecyclerView中不能动态设置itemView的宽高
     *      原因？
     * */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {

//        val view = TextView(context)
//        view.layoutParams.height = dip2px(context, 60)
//        view.layoutParams.width = dip2px(context, 200)

        val view = LayoutInflater.from(context)
            .inflate(R.layout.item_text, parent, false)

        return Holder(view)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.onItemClickListener = listener
    }


    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {}


    interface OnItemClickListener {
        fun onItemClick(view: View, position: Int)

    }


    //根据手机的分辨率从 dp 的单位 转成为 px(像素)
    fun dip2px(context: Context, dpValue: Int): Int {
        val scale = context.resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }

    //根据手机的分辨率从 px(像素) 的单位 转成为 dp
    fun px2dip(context: Context, pxValue: Int): Int {
        val scale = context.resources.displayMetrics.density
        return (pxValue / scale + 0.5f).toInt()
    }


}
