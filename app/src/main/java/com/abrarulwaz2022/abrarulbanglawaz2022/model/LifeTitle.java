package com.abrarulwaz2022.abrarulbanglawaz2022.model;

public class LifeTitle {
    private String title;
    private String videoId;
    public LifeTitle(){}
    public LifeTitle(String title, String videoId) {
        this.title = title;
        this.videoId = videoId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }
}
