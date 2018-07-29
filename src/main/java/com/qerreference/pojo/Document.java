package com.qerreference.pojo;

public class Document {

	private String Question;
	private String Intent;
	private String Confidence;
	private String Entity;
	private String ConvoOutput;
	private String ConvoId;
	private String RetrieveAndRankOutput;
	private String Time;
	
	
	
	public String getQuestion() {
		return Question;
	}
	public void setQuestion(String question) {
		Question = question;
	}
	public String getIntent() {
		return Intent;
	}
	public void setIntent(String intent) {
		Intent = intent;
	}
	public String getConfidence() {
		return Confidence;
	}
	public void setConfidence(String confidence) {
		Confidence = confidence;
	}
	public String getEntity() {
		return Entity;
	}
	public void setEntity(String entity) {
		Entity = entity;
	}
	public String getConvoOutput() {
		return ConvoOutput;
	}
	public void setConvoOutput(String convoOutput) {
		ConvoOutput = convoOutput;
	}
	public String getConvoId() {
		return ConvoId;
	}
	public void setConvoId(String convoId) {
		ConvoId = convoId;
	}
	public String getRetrieveAndRankOutput() {
		return RetrieveAndRankOutput;
	}
	public void setRetrieveAndRankOutput(String retrieveAndRankOutput) {
		RetrieveAndRankOutput = retrieveAndRankOutput;
	}
	public String getTime() {
		return Time;
	}
	public void setTime(String time) {
		Time = time;
	}
}
