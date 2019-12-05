package com.tencent.cba.domain;

import java.io.Serializable;

/**
 * @Description TODO
 * @Author healen
 * @Date 2019/6/3 17:20
 */
public class Game implements Serializable {
    // 客队总分
    private String awayGoal;
    // 客队ID
    private String awayId;
    // 客队名称
    private String awayName;
    // 结束时间
    private String endTime;
    // 小组名称
    private String groupName;
    // 主队总分
    private String homeGoal;
    // 主队ID
    private String homeId;
    // 主队logo
    // private String homeLogo;
    // 主队名称
    private String homeName;
    // 比赛唯一ID（由赛事ID和比赛ID组成）
    private String mid;
    // 进行中还是已结束
    private String period;
    // 第几节
    private String quarter;
    // 当前节剩余时间
    private String quarterTime;
    // 开始时间
    private String startTime;

    public String getAwayGoal() {
        return awayGoal;
    }

    public void setAwayGoal(String awayGoal) {
        this.homeGoal = awayGoal;
    }

    public String getAwayId() {
        return awayId;
    }

    public void setAwayId(String awayId) {
        this.homeId = awayId;
    }

    public String getAwayName() {
        return awayName;
    }

    public void setAwayName(String awayName) {
        if (null == awayName || "".equals(awayName)) {
            this.homeName = homeId;
        } else {
            this.homeName = awayName;
        }
    }


    public String getHomeGoal() {
        return homeGoal;
    }

    public void setHomeGoal(String homeGoal) {
        this.awayGoal = homeGoal;
    }

    public String getHomeId() {
        return homeId;
    }

    public void setHomeId(String homeId) {
        this.awayId = homeId;
    }

    public String getHomeName() {
        return homeName;
    }

    public void setHomeName(String homeName) {
        if (null == homeName || "".equals(homeName)) {
            this.awayName = this.awayId;
        } else {
            this.awayName = homeName;
        }
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getQuarter() {
        return quarter;
    }

    public void setQuarter(String quarter) {
        this.quarter = quarter;
    }

    public String getQuarterTime() {
        return quarterTime;
    }

    public void setQuarterTime(String quarterTime) {
        this.quarterTime = quarterTime;
    }

    @Override
    public String toString() {
        return "mid=" + mid +
                ";awayGoal=" + awayGoal +
                ";awayId=" + awayId +
                ";awayName=" + awayName +
                ";startTime=" + startTime +
                ";endTime=" + endTime +
                ";groupName=" + groupName +
                ";homeGoal=" + homeGoal +
                ";homeId=" + homeId +
                ";homeName=" + homeName +
                ";period=" + period +
                ";quarter=" + quarter +
                ";quarterTime=" + quarterTime;
    }
}
