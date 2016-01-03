package com.orkunduzgun.mongodbandamazonconnection;


public class TweetDatas {
    private String name;
    private String tweet;
    private String img;
    private String time;

    public TweetDatas(String name, String tweet, String img, String time) {
        super();
        this.name = name;
        this.tweet = tweet;
        this.img = img;
        this.time = time;
    }

    @Override
    public String toString() {
        return name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTweet() {
        return tweet;
    }

    public void setTweet(String tweet) {
        this.tweet = tweet;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String img) {
        this.time = time;
    }
}
