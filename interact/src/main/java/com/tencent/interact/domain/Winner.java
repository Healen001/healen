package com.tencent.interact.domain;

import java.io.Serializable;

/**
 * @Description TODO 中奖名单
 * @Author healen
 * @Date 2019/7/23 15:38
 */
public class Winner implements Serializable {
    // 用户昵称
    private String userNick;
    // 用户头像
    private String userIcon;
    // vip标识
    private String userVip;

    public String getUserNick() {
        return userNick;
    }

    public void setUserNick(String userNick) {
        this.userNick = userNick;
    }

    public String getUserIcon() {
        return userIcon;
    }

    public void setUserIcon(String userIcon) {
        this.userIcon = userIcon;
    }

    public String getUserVip() {
        return userVip;
    }

    public void setUserVip(String userVip) {
        this.userVip = userVip;
    }
}
