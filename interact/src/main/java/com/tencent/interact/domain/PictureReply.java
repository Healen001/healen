package com.tencent.interact.domain;

import java.io.Serializable;

/**
 * @Description TODO 图片互动回帖
 * @Author healen
 * @Date 2019/6/11 16:04
 */
public class PictureReply implements Serializable {
    // 回帖id
    private String id;
    // uid
    private String uid;
    // tid
    private String tid;
    // 内容
    private String content;
    // 楼层号
    private String floorNum;
    // 用户头像
    private String handUrl;
    // 用户回帖图片
    private String imageUrl;
    // 姓名
    private String name;
    //会员标识
    private String vipFlag;

    public String getVipFlag() {
        return vipFlag;
    }

    public void setVipFlag(String vipFlag) {
        this.vipFlag = vipFlag;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getFloorNum() {
        return floorNum;
    }

    public void setFloorNum(String floorNum) {
        this.floorNum = floorNum;
    }

    public String getHandUrl() {
        return handUrl;
    }

    public void setHandUrl(String handUrl) {
        this.handUrl = handUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
