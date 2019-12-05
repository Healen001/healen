package com.tencent.interact.domain;

import java.io.Serializable;

/**
 * @Description TODO
 * @Author healen
 * @Date 2019/7/23 14:14
 */
public class Opreation implements Serializable {
    // 选项
    private String choiceId;
    // 内容
    private String text;
    // 人数
    private String cntDisplay;
    // 百分比
    private String ratio;

    public String getChoiceId() {
        return choiceId;
    }

    public void setChoiceId(String choiceId) {
        this.choiceId = choiceId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getCntDisplay() {
        return cntDisplay;
    }

    public void setCntDisplay(String cntDisplay) {
        this.cntDisplay = cntDisplay;
    }

    public String getRatio() {
        return ratio;
    }

    public void setRatio(String ratio) {
        this.ratio = ratio;
    }
}
