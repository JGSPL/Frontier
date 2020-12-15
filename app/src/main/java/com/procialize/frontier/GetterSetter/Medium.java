package com.procialize.frontier.GetterSetter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Naushad on 1/22/2018.
 */

public class Medium {
    @SerializedName("nanomp4")
    @Expose
    private Nanomp4 nanomp4;
    @SerializedName("nanowebm")
    @Expose
    private Nanowebm nanowebm;
    @SerializedName("tinygif")
    @Expose
    private Tinygif tinygif;
    @SerializedName("tinymp4")
    @Expose
    private Tinymp4 tinymp4;
    @SerializedName("tinywebm")
    @Expose
    private Tinywebm tinywebm;
    @SerializedName("webm")
    @Expose
    private Webm webm;
    @SerializedName("gif")
    @Expose
    private Gif gif;
    @SerializedName("mp4")
    @Expose
    private Mp4 mp4;
    @SerializedName("loopedmp4")
    @Expose
    private Loopedmp4 loopedmp4;
    @SerializedName("mediumgif")
    @Expose
    private Mediumgif mediumgif;
    @SerializedName("nanogif")
    @Expose
    private Nanogif nanogif;

    public Nanomp4 getNanomp4() {
        return nanomp4;
    }

    public void setNanomp4(Nanomp4 nanomp4) {
        this.nanomp4 = nanomp4;
    }

    public Nanowebm getNanowebm() {
        return nanowebm;
    }

    public void setNanowebm(Nanowebm nanowebm) {
        this.nanowebm = nanowebm;
    }

    public Tinygif getTinygif() {
        return tinygif;
    }

    public void setTinygif(Tinygif tinygif) {
        this.tinygif = tinygif;
    }

    public Tinymp4 getTinymp4() {
        return tinymp4;
    }

    public void setTinymp4(Tinymp4 tinymp4) {
        this.tinymp4 = tinymp4;
    }

    public Tinywebm getTinywebm() {
        return tinywebm;
    }

    public void setTinywebm(Tinywebm tinywebm) {
        this.tinywebm = tinywebm;
    }

    public Webm getWebm() {
        return webm;
    }

    public void setWebm(Webm webm) {
        this.webm = webm;
    }

    public Gif getGif() {
        return gif;
    }

    public void setGif(Gif gif) {
        this.gif = gif;
    }

    public Mp4 getMp4() {
        return mp4;
    }

    public void setMp4(Mp4 mp4) {
        this.mp4 = mp4;
    }

    public Loopedmp4 getLoopedmp4() {
        return loopedmp4;
    }

    public void setLoopedmp4(Loopedmp4 loopedmp4) {
        this.loopedmp4 = loopedmp4;
    }

    public Mediumgif getMediumgif() {
        return mediumgif;
    }

    public void setMediumgif(Mediumgif mediumgif) {
        this.mediumgif = mediumgif;
    }

    public Nanogif getNanogif() {
        return nanogif;
    }

    public void setNanogif(Nanogif nanogif) {
        this.nanogif = nanogif;
    }

}