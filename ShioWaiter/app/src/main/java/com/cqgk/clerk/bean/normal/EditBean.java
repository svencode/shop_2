package com.cqgk.clerk.bean.normal;

import com.cqgk.clerk.utils.AppUtil;
import com.cqgk.clerk.utils.CheckUtils;

import cn.finalteam.galleryfinal.model.PhotoInfo;

/**
 * Created by duke on 16/5/9.
 */
public class EditBean {
    private String title;
    private PhotoInfo photoInfo;
    private String uploadId;

    public String getUploadId() {
        return uploadId;
    }

    public void setUploadId(String uploadId) {
        this.uploadId = uploadId;
    }

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


    public String getFileName() {
        if (photoInfo != null)
            return AppUtil.getFileName(photoInfo.getPhotoPath());

        return "";
    }


}
