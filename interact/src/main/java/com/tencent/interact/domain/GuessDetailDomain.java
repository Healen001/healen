package com.tencent.interact.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description TODO 竞猜domain
 * @Author healen
 * @Date 2019/7/23 14:38
 */
public class GuessDetailDomain implements Serializable {
    // 中奖人数
    private String winnerNum;
    // 可得金额
    private String amountAvg;
    // guessDetail集合
    private List<GuessDetail> guessDetailList = new ArrayList<>();

    public String getWinnerNum() {
        return winnerNum;
    }

    public void setWinnerNum(String winnerNum) {
        this.winnerNum = winnerNum;
    }

    public String getAmountAvg() {
        return amountAvg;
    }

    public void setAmountAvg(String amountAvg) {
        this.amountAvg = amountAvg;
    }

    public List<GuessDetail> getGuessDetailList() {
        return guessDetailList;
    }

    public void setGuessDetailList(List<GuessDetail> guessDetailList) {
        this.guessDetailList = guessDetailList;
    }
}
