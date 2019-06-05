package com.gliesereum.coupler.data.network.json.avatar;

import com.google.gson.annotations.SerializedName;

import okhttp3.MultipartBody;

public class UploadBody {

    @SerializedName("file")
    private MultipartBody.Part file;

    @SerializedName("open")
    private Boolean open;

    public UploadBody(MultipartBody.Part file, Boolean open) {
        this.file = file;
        this.open = open;
    }

    public MultipartBody.Part getFile() {
        return file;
    }

    public void setFile(MultipartBody.Part file) {
        this.file = file;
    }

    public Boolean getOpen() {
        return open;
    }

    public void setOpen(Boolean open) {
        this.open = open;
    }
}
