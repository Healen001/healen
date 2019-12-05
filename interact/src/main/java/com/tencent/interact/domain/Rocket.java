package com.tencent.interact.domain;

import java.io.Serializable;

/**
 * @Description TODO 文字互动回帖
 * @Author healen
 * @Date 2019/6/11 16:04
 */
public class Rocket implements Serializable {
    // nick
    private String nick;
    // 图片
    private String icon;
    // 内容
    private String comment;
    // VIP类型
    private String vipType;

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getVipType() {
        return vipType;
    }

    public void setVipType(String vipType) {
        this.vipType = vipType;
    }
}
