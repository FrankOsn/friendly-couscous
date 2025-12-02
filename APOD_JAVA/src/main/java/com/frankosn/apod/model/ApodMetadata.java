package com.frankosn.apod.model;

import com.google.gson.annotations.SerializedName;

/**
 * Model class for APOD metadata.
 */
public class ApodMetadata {

    private String title;
    private String explanation;
    private String date;
    
    @SerializedName("media_type")
    private String mediaType;
    private String url;
    private String hdurl;
    
    @SerializedName("thumbnail_url")
    private String thumbnailUrl;
    
    @SerializedName("image_path")
    private String imagePath;

    // Getters and Setters

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getHdurl() {
        return hdurl;
    }

    public void setHdurl(String hdurl) {
        this.hdurl = hdurl;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    @Override
    public String toString() {
        return "ApodMetadata{" +
                "title='" + title + '\'' +
                ", date='" + date + '\'' +
                ", mediaType='" + mediaType + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
