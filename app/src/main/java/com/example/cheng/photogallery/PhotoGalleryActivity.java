package com.example.cheng.photogallery;

import android.support.v4.app.Fragment;

public class PhotoGalleryActivity extends SingleFragmentActivity {

    /**
     * 实现父类的抽象方法
     * @return
     */
    @Override
    protected Fragment createFragment() {
        return PhotoGalleryFragment.newInstance();
    }
}
