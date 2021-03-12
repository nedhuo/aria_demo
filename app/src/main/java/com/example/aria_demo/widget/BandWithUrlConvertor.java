package com.example.aria_demo.widget;

import com.arialyy.aria.core.processor.IBandWidthUrlConverter;

 class BandWidthUrlConvertor implements IBandWidthUrlConverter {


    @Override
    public String convert(String m3u8Url, String bandWidthUrl) {
        int index = m3u8Url.lastIndexOf("/");
        return m3u8Url.substring(0, index + 1) + bandWidthUrl;
    }
}