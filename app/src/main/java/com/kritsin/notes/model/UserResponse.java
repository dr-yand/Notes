package com.kritsin.notes.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

public class UserResponse {
	private ServerResponseStatus responseCode = ServerResponseStatus.ERROR;
	private User user;

	public UserResponse(){

	}

	public UserResponse(JSONObject jsonObject){
		try{
			responseCode = ServerResponseStatus.valueOf(jsonObject.getInt("response"));
			int serverId = jsonObject.getInt("id");
			String name = jsonObject.getString("name");
			user = new User();
			user.setServerId(serverId);
			user.setName(name);
		}
		catch (Exception e){

		}
	}

	public ServerResponseStatus getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(ServerResponseStatus responseCode) {
		this.responseCode = responseCode;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}
