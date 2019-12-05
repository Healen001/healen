package com.tencent.interact.domain;

import java.io.Serializable;

/**
 * @Description TODO
 * @Author healen
 * @Date 2019/7/23 14:14
 */
public class Option implements Serializable {
    // 操作id
    private String optionId;
    // 投票人数
    private String voteNum;
    // 选项内容
    private String content;
    // 百分比
    private String percent;

    public String getOptionId() {
        return optionId;
    }

    public void setOptionId(String optionId) {
        this.optionId = optionId;
    }

    public String getVoteNum() {
        return voteNum;
    }

    public void setVoteNum(String voteNum) {
        this.voteNum = voteNum;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPercent() {
        return percent;
    }

    public void setPercent(String percent) {
        this.percent = percent;
    }
}
