package com.example.cheng.photogallery;

/**
 * Created by cheng on 2016/9/24.
 */

public class GalleryItem {
    private String mCption;
    private String mId;
    private String mUrl;

    @Override
    public String toString() {
        return mCption;
    }

    public String getmCption() {
        return mCption;
    }

    public void setmCption(String mCption) {
        this.mCption = mCption;
    }

    public String getmId() {
        return mId;
    }

    public void setmId(String mId) {
        this.mId = mId;
    }

    public String getmUrl() {
        return mUrl;
    }

    public void setmUrl(String mUrl) {
        this.mUrl = mUrl;
    }


}
