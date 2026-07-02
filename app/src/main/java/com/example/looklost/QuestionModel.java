package com.example.looklost;

public class QuestionModel {
    private String questionID ;
    private String itemid ;
    private String userid ;
    private String question ;
    private String opt1;
    private String opt2;
    private String opt3;
    private String Canswer;

    public QuestionModel(String questionID, String itemid, String userid, String question, String opt1, String opt2, String opt3, String canswer) {
        this.userid = userid;
        this.itemid = itemid;
        this.questionID = questionID;
        this.question = question;
        this.opt1 = opt1;
        this.opt2 = opt2;
        this.opt3 = opt3;
        Canswer = canswer;
    }

    public QuestionModel() {
    }

    public String getQuestionID() {
        return questionID;
    }

    public void setQuestionID(String questionID) {
        this.questionID = questionID;
    }

    public String getItemid() {
        return itemid;
    }

    public void setItemid(String itemid) {
        this.itemid = itemid;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getOpt1() {
        return opt1;
    }

    public void setOpt1(String opt1) {
        this.opt1 = opt1;
    }

    public String getOpt2() {
        return opt2;
    }

    public void setOpt2(String opt2) {
        this.opt2 = opt2;
    }

    public String getOpt3() {
        return opt3;
    }

    public void setOpt3(String opt3) {
        this.opt3 = opt3;
    }

    public String getCanswer() {
        return Canswer;
    }

    public void setCanswer(String canswer) {
        Canswer = canswer;
    }
}
