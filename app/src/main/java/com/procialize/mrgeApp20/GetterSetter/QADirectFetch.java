package com.procialize.mrgeApp20.GetterSetter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class QADirectFetch {

    @SerializedName("qa_question")
    @Expose
    private List<DirectQuestion> qa_question = null;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("msg")
    @Expose
    private String msg;

    public List<DirectQuestion> getQa_question() {
        return qa_question;
    }

    public void setQa_question(List<DirectQuestion> qa_question) {
        this.qa_question = qa_question;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
