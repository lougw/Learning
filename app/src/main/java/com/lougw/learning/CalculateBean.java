package com.lougw.learning;

public class CalculateBean {
    private int firstNum;
    private int secondNum;
    private int result = -999;
    private Algorithm algorithm = Algorithm.PLUS;
    private AnswerState state = AnswerState.NOT_ANSWERED;

    public int getFirstNum() {
        return firstNum;
    }

    public void setFirstNum(int firstNum) {
        this.firstNum = firstNum;
    }

    public int getSecondNum() {
        return secondNum;
    }

    public void setSecondNum(int secondNum) {
        this.secondNum = secondNum;
    }

    public AnswerState getState() {
        return state;
    }

    public void setState(AnswerState state) {
        this.state = state;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public Algorithm getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(Algorithm algorithm) {
        this.algorithm = algorithm;
    }
}
