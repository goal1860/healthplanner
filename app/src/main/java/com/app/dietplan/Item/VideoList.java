package com.app.dietplan.Item;

import java.io.Serializable;

public class VideoList implements Serializable {

    private String num,id,video_type,video_title,video_url,video_id,video_thumbnail_b,video_thumbnail_s,video_duration;

    public VideoList(String num, String id, String video_type, String video_title, String video_url, String video_id, String video_thumbnail_b, String video_thumbnail_s, String video_duration) {
        this.num = num;
        this.id = id;
        this.video_type = video_type;
        this.video_title = video_title;
        this.video_url = video_url;
        this.video_id = video_id;
        this.video_thumbnail_b = video_thumbnail_b;
        this.video_thumbnail_s = video_thumbnail_s;
        this.video_duration = video_duration;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVideo_type() {
        return video_type;
    }

    public void setVideo_type(String video_type) {
        this.video_type = video_type;
    }

    public String getVideo_title() {
        return video_title;
    }

    public void setVideo_title(String video_title) {
        this.video_title = video_title;
    }

    public String getVideo_url() {
        return video_url;
    }

    public void setVideo_url(String video_url) {
        this.video_url = video_url;
    }

    public String getVideo_id() {
        return video_id;
    }

    public void setVideo_id(String video_id) {
        this.video_id = video_id;
    }

    public String getVideo_thumbnail_b() {
        return video_thumbnail_b;
    }

    public void setVideo_thumbnail_b(String video_thumbnail_b) {
        this.video_thumbnail_b = video_thumbnail_b;
    }

    public String getVideo_thumbnail_s() {
        return video_thumbnail_s;
    }

    public void setVideo_thumbnail_s(String video_thumbnail_s) {
        this.video_thumbnail_s = video_thumbnail_s;
    }

    public String getVideo_duration() {
        return video_duration;
    }

    public void setVideo_duration(String video_duration) {
        this.video_duration = video_duration;
    }
}
