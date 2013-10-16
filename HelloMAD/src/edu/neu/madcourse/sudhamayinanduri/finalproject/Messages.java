package edu.neu.madcourse.sudhamayinanduri.finalproject;


public class Messages {

	private String from;
	private String location;
	private String date;
	private String message;
	private String time;
	private String type;
	private String received;
    private String unRead;
	
	
	
	
	public  String getUnRead() {
		return unRead;
	}
	public  void setUnRead(String unRead) {
		this.unRead = unRead;
	}
	
	
	public String getReceived() {
		return received;
	}


	public void setReceived(String received) {
		this.received = received;
	}


	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}


	public String getMessage() {
		return message;
	}


	public void setMessage(String message) {
		this.message = message;
	}


	public String getTime() {
		return time;
	}


	public void setTime(String time) {
		this.time = time;
	}


	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
}
