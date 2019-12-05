package com.tencent.interact.domain;

import java.io.Serializable;

/**
 * @Description TODO 文字互动帖子标题
 * @Author healen
 * @Date 2019/6/11 16:04
 */
public class FontTopic implements Serializable {
    // tid
    private String id;
    // 标题
    private String title;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
