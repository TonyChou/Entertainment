package com.union.fmdouban;

/**
 * Created by zhouxiaming on 2017/4/16.
 */

public enum CategoryType {
    CHANNEL_LIST(1, "兆赫"),
    SONG_LIST(2, "歌单"),
    RED_LIST(3, "红心");

    private int id;
    private String name;
    
    CategoryType(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
