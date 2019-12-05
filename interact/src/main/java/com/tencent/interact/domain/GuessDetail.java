package com.tencent.interact.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * @Description TODO 竞猜
 * @Author healen
 * @Date 2019/6/11 16:04
 */
public class GuessDetail implements Serializable {
    // 竞猜id
    private String id;
    // 竞猜问题
    private String title;
    // 答案
    private String answer;
    // 选项集合
    private List<Opreation> oList = new ArrayList<>();

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

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public List<Opreation> getoList() {
        return oList;
    }

    public void setoList(List<Opreation> oList) {
        this.oList = oList;
    }
}
