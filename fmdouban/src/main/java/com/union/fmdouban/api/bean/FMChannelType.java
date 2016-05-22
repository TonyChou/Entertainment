package com.union.fmdouban.api.bean;

import java.io.Serializable;

/**
 * 频道分类
 * Created by zhouxiaming on 16/3/26.
 */
public class FMChannelType implements Serializable{
    public static final String hotTypeId = "0x0001";
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

    @Override
    public boolean equals(Object o) {
        if (o == null || !(o instanceof FMChannelType)) {
            return false;
        }

        if (((FMChannelType)o).getId() != null && ((FMChannelType)o).getId().equals(this.getId())) {
            return true;
        }

        return super.equals(o);
    }
}
