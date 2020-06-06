package com.procialize.mrgeApp20.GetterSetter;

/**
 * Created by Naushad on 2/14/2018.
 */

public class QuizFolder {

    public String getFolder_name() {
        return folder_name;
    }

    public void setFolder_name(String folder_name) {
        this.folder_name = folder_name;
    }

    String folder_name;

    String timer;
    String answered;

    String total_quiz;
    String total_correct;
    String folder_id;

    public String getFolder_id() {
        return folder_id;
    }

    public void setFolder_id(String folder_id) {
        this.folder_id = folder_id;
    }

    public String getAnswered() {
        return answered;
    }

    public void setAnswered(String answered) {
        this.answered = answered;
    }

    public String getTotal_quiz() {
        return total_quiz;
    }

    public void setTotal_quiz(String total_quiz) {
        this.total_quiz = total_quiz;
    }

    public String getTotal_correct() {
        return total_correct;
    }

    public void setTotal_correct(String total_correct) {
        this.total_correct = total_correct;
    }

    public String getTimer() {
        return timer;
    }

    public void setTimer(String timer) {
        this.timer = timer;
    }
}
