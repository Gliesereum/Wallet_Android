package com.gliesereum.coupler.data.network.json.avatar;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UploadResponse {
    @SerializedName("filename")
    private String filename;

    @SerializedName("originalFilename")
    private String originalFilename;

    @SerializedName("url")
    private String url;

    @SerializedName("mediaType")
    private String mediaType;

    @SerializedName("size")
    private Long size;

    @SerializedName("userId")
    private String userId;

    @SerializedName("open")
    private Boolean open;

    @SerializedName("crypto")
    private Boolean crypto;

    @SerializedName("keys")
    private List<String> keys;

    @SerializedName("readerIds")
    private List<String> readerIds;
}
