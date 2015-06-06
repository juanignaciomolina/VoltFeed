package com.droidko.voltfeed.entities;

public class RowNews {

    private String title;
    private String content;
    private int imageUrl;
    private boolean like;
    private String date;

    public RowNews(String title, String content, int imageUrl, boolean like, String date) {

        this.title = title;
        this.content = content;
        this.imageUrl = imageUrl;
        this.like = like;
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(int imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean getLike() {
        return like;
    }

    public void setLike(boolean like) {
        this.like = like;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
