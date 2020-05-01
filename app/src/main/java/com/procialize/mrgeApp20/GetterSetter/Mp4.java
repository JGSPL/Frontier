package com.procialize.mrgeApp20.GetterSetter;

/**
 * Created by Naushad on 1/22/2018.
 */


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Mp4 {

    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("dims")
    @Expose
    private List<Integer> dims = null;
    @SerializedName("duration")
    @Expose
    private Double duration;
    @SerializedName("preview")
    @Expose
    private String preview;
    @SerializedName("size")
    @Expose
    private Integer size;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<Integer> getDims() {
        return dims;
    }

    public void setDims(List<Integer> dims) {
        this.dims = dims;
    }

    public Double getDuration() {
        return duration;
    }

    public void setDuration(Double duration) {
        this.duration = duration;
    }

    public String getPreview() {
        return preview;
    }

    public void setPreview(String preview) {
        this.preview = preview;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

}