package com.tencent.interact.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description TODO 竞猜
 * @Author healen
 * @Date 2019/6/11 16:04
 */
public class VoteDetail implements Serializable {
    // 竞猜id
    private String questionId;
    // 竞猜问题
    private String title;
    // 投票总人数
    private String totalVoteNum;
    // 状态
    private String status;
    //
    private String canRevote;
    //
    private String hasVote;
    //
    private String myVote;
    // 总数
    private String num;
    // 选择的序号
    private String select;
    // 选项集合
    private List<Option> options = new ArrayList<>();

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getSelect() {
        return select;
    }

    public void setSelect(String select) {
        this.select = select;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTotalVoteNum() {
        return totalVoteNum;
    }

    public void setTotalVoteNum(String totalVoteNum) {
        this.totalVoteNum = totalVoteNum;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCanRevote() {
        return canRevote;
    }

    public void setCanRevote(String canRevote) {
        this.canRevote = canRevote;
    }

    public String getHasVote() {
        return hasVote;
    }

    public void setHasVote(String hasVote) {
        this.hasVote = hasVote;
    }

    public String getMyVote() {
        return myVote;
    }

    public void setMyVote(String myVote) {
        this.myVote = myVote;
    }

    public List<Option> getOptions() {
        return options;
    }

    public void setOptions(List<Option> options) {
        this.options = options;
    }
}
