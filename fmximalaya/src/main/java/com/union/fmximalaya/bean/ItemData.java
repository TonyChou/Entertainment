package com.union.fmximalaya.bean;

/**
 * Created by zhouxiaming on 2016/3/23.
 */

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ItemData {
    static String[] imageUrls = new String[] {"http://img3.douban.com/img/fmadmin/chlBanner/27675.jpg", "http://img3.doubanio.com/img/fmadmin/chlBanner/26376.jpg", "http://img3.doubanio.com/img/fmadmin/chlBanner/26379.jpg"};
    private String name;
    private int type;
    private String imageUrl;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public static List<ItemData> genItemData() {
        Random random = new Random();
        List<ItemData> list = new ArrayList<ItemData>();
        for (int i = 0; i < 50; i++) {
            ItemData item = new ItemData();
            item.setName("Item: " + i);
            int type = random.nextInt(3);
            item.setType(random.nextInt(3));
            item.setImageUrl(imageUrls[type]);
            list.add(item);
        }
        return list;
    }
}
