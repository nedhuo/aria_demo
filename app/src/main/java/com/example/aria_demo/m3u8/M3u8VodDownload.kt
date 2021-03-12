package com.example.aria_demo.m3u8

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import com.example.aria_demo.R
import com.example.aria_demo.dialog.DialogFragmentDownload
import com.example.aria_demo.dialog.HttpsDialogFragment

class M3u8VodDownload : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_m3u8download)

        initView()
    }

    private fun initView() {
//        val httpsDialogFragment = HttpsDialogFragment()
//        httpsDialogFragment.show(supportFragmentManager, "")
        val dialogFragmentDownload = DialogFragmentDownload()
        dialogFragmentDownload.show(supportFragmentManager, "")
    }
}