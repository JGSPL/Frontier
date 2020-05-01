package com.procialize.mrgeApp20.GetterSetter;

public class SelectedImages {
    /**
     * File path.
     */
    private String mPath;
    /**
     * Thumb path.
     */
    private String mThumbPath;
    /**
     * Video compressed or not
     */
    private Boolean mIsCompressed;
    /**
     * Original Path
     */
    private String mOriginalFilePath;
    /**
     * Media Type
     */
    private String mMediaType;

    public String getmMediaType() {
        return mMediaType;
    }

    public void setmMediaType(String mMediaType) {
        this.mMediaType = mMediaType;
    }

    public SelectedImages(String originalFilePath, String path, String thumbPath, Boolean isCompressed, String mediaType) {
        this.mOriginalFilePath = originalFilePath;
        this.mPath = path;
        this.mThumbPath = thumbPath;
        this.mIsCompressed = isCompressed;
        this.mMediaType = mediaType;
    }

    public String getmOriginalFilePath() {
        return mOriginalFilePath;
    }

    public Boolean getmIsCompressed() {
        return mIsCompressed;
    }

    public String getmPath() {
        return mPath;
    }

    public String getmThumbPath() {
        return mThumbPath;
    }

}
