package com.cqgk.clerk.bean.normal;

import cn.finalteam.galleryfinal.model.PhotoInfo;

/**
 * Created by duke on 16/5/9.
 */
public class EditBean {
    private String title;
     private PhotoInfo photoInfo;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public PhotoInfo getPhotoInfo() {
        return photoInfo;
    }

    public void setPhotoInfo(PhotoInfo photoInfo) {
        this.photoInfo = photoInfo;
    }
}
