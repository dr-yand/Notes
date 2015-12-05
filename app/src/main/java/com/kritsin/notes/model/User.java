package com.kritsin.notes.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

public class User implements Parcelable{
	private int id;
	private int serverId;
	private String name;
	private String login;
	private String password;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getServerId() {
		return serverId;
	}

	public void setServerId(int serverId) {
		this.serverId = serverId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public User(){

	}

	public User(JSONObject jsonObject){
		try{
			serverId = jsonObject.getInt("id");
			name = jsonObject.getString("name"); 
		}
		catch (Exception e){

		}
	}

	protected User(Parcel in) {
		id = in.readInt();
		serverId = in.readInt();
		name = in.readString();
		login = in.readString();
		password = in.readString();
	}

	public static final Creator<User> CREATOR = new Creator<User>() {
		@Override
		public User createFromParcel(Parcel in) {
			return new User(in);
		}

		@Override
		public User[] newArray(int size) {
			return new User[size];
		}
	};

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(id);
		dest.writeInt(serverId);
		dest.writeString(name);
		dest.writeString(login);
		dest.writeString(password);
	}

}
