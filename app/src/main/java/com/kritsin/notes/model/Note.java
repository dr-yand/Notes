package com.kritsin.notes.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

public class Note implements Parcelable{

    private int id, serverId, userId, verify;
    private String note;

    @Override
    public String toString() {
        return  note  ;
    }

    public Note(){

    }

    public Note(JSONObject jsonObject){
        try{
            serverId = jsonObject.getInt("id");
            note = jsonObject.getString("note");
        }
        catch (Exception e){

        }
    }

    protected Note(Parcel in) {
        id = in.readInt();
        serverId = in.readInt();
        userId = in.readInt();
        note = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(serverId);
        dest.writeInt(userId);
        dest.writeString(note);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Note> CREATOR = new Creator<Note>() {
        @Override
        public Note createFromParcel(Parcel in) {
            return new Note(in);
        }

        @Override
        public Note[] newArray(int size) {
            return new Note[size];
        }
    };

    public int getServerId() {
        return serverId;
    }

    public void setServerId(int serverId) {
        this.serverId = serverId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getVerify() {
        return verify;
    }

    public void setVerify(int verify) {
        this.verify = verify;
    }
}
