package com.example.aria_demo.widget

import com.arialyy.aria.core.processor.IBandWidthUrlConverter


/**
 * Author: nedhuo
 * Date  : 2021/3/10
 * email : nedhuo@163.com
 */


object BandWidthUrlConverter : IBandWidthUrlConverter {


    override fun convert(m3u8Url: String, bandWidthUrl: String): String {
        val index = m3u8Url.lastIndexOf("/")
        return m3u8Url.substring(0, index + 1) + bandWidthUrl
    }
}