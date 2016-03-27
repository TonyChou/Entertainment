package com.union.fmdouban.api.bean;

/**
 * 频道分类
 * Created by zhouxiaming on 16/3/26.
 */
public class FMChannelType {
    private String id;
    private String name;
    public FMChannelType(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
