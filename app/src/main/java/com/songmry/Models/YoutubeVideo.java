package com.songmry.Models;

public class YoutubeVideo {
    public String videoId;
    public String title;
    public String thumbnail;

    public YoutubeVideo(String title, String videoId, String thumbnail){
        this.title = title;
        this.videoId = videoId;
        this.thumbnail = thumbnail;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }
}
