package edu.neu.madcourse.sudhamayinanduri.finalproject;

import java.util.ArrayList;

import android.location.LocationManager;

public class Manager {

	private static String username;
	private static String friend;
	private static String message;
	private static String type;
	private static String feature;
	private static String timer;
	private static String latitude;
	private static String longitude;
	private static String from;
	private static String location;
	private static String time;
	private static String date;
	private static String latitude2;
	private static String longitude2;
	
	private static Messages messages;
	private static MessageAdapter ma;
	private static ArrayList<Messages> msgList;
    private static MenuScreen menuScreen;	
    private static LocationManager locationManager;
	private static String cameFrom;
	
	
    
	
	public static String getCameFrom() {
		return cameFrom;
	}
	public static void setCameFrom(String cameFrom) {
		Manager.cameFrom = cameFrom;
	}
	public static LocationManager getLocationManager() {
		return locationManager;
	}
	public static void setLocationManager(LocationManager locationManager) {
		Manager.locationManager = locationManager;
	}
	public static MenuScreen getMenuScreen() {
		return menuScreen;
	}
	public static void setMenuScreen(MenuScreen menuScreen) {
		Manager.menuScreen = menuScreen;
	}
	public static ArrayList<Messages> getMsgList() {
		return msgList;
	}
	public static void setMsgList(ArrayList<Messages> msgList) {
		Manager.msgList = msgList;
	}
	public static MessageAdapter getMa() {
		return ma;
	}
	public static void setMa(MessageAdapter ma) {
		Manager.ma = ma;
	}
	
	public static String getFeature() {
		return feature;
	}
	public static void setFeature(String feature) {
		Manager.feature = feature;
	}
	public static Messages getMessages() {
		return messages;
	}
	public static void setMessages(Messages messages) {
		Manager.messages = messages;
	}
	public static String getLatitude2() {
		return latitude2;
	}
	public static void setLatitude2(String latitude2) {
		Manager.latitude2 = latitude2;
	}
	public static String getLongitude2() {
		return longitude2;
	}
	public static void setLongitude2(String longitude2) {
		Manager.longitude2 = longitude2;
	}
	public static String getFrom() {
		return from;
	}
	public static void setFrom(String from) {
		Manager.from = from;
	}
	public static String getLocation() {
		return location;
	}
	public static void setLocation(String location) {
		Manager.location = location;
	}
	public static String getTime() {
		return time;
	}
	public static void setTime(String time) {
		Manager.time = time;
	}
	public static String getDate() {
		return date;
	}
	public static void setDate(String date) {
		Manager.date = date;
	}
	public static String getUsername() {
		return username;
	}
	public static void setUsername(String username) {
		Manager.username = username;
	}
	public static String getFriend() {
		return friend;
	}
	public static void setFriend(String friend) {
		Manager.friend = friend;
	}
	public static String getMessage() {
		return message;
	}
	public static void setMessage(String message) {
		Manager.message = message;
	}
	public static String getType() {
		return type;
	}
	public static void setType(String type) {
		Manager.type = type;
	}
	public static String getTimer() {
		return timer;
	}
	public static void setTimer(String timer) {
		Manager.timer = timer;
	}
	public static String getLatitude() {
		return latitude;
	}
	public static void setLatitude(String latitude) {
		Manager.latitude = latitude;
	}
	public static String getLongitude() {
		return longitude;
	}
	public static void setLongitude(String longitude) {
		Manager.longitude = longitude;
	}
	
	
}
