package com.procialize.frontier.GetterSetter;

public class Quiz {

	private String id;
	private String question;

	private String folder_name;
	private String correct_answer;
	private String replied;
	private String selected_option;

	public String getSelected_option() {
		return selected_option;
	}

	public void setSelected_option(String selected_option) {
		this.selected_option = selected_option;
	}

	public String getQuiz_type() {
		return quiz_type;
	}

	public void setQuiz_type(String quiz_type) {
		this.quiz_type = quiz_type;
	}

	private String quiz_type;

	public String getFolder_name() {
		return folder_name;
	}

	public void setFolder_name(String folder_name) {
		this.folder_name = folder_name;
	}
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public String getCorrect_answer() {
		return correct_answer;
	}

	public void setCorrect_answer(String correct_answer) {
		this.correct_answer = correct_answer;
	}

	public String getReplied() {
		return replied;
	}

	public void setReplied(String replied) {
		this.replied = replied;
	}

}
