package com.kritsin.notes.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

public class NoteResponse {

    private ServerResponseStatus responseCode;
    private Note note;


    public NoteResponse(){

    }

    public NoteResponse(JSONObject jsonObject){
        try{
            responseCode = ServerResponseStatus.valueOf(jsonObject.getInt("response"));
            if(jsonObject.has("note"))
                note = new Note(jsonObject.getJSONObject("note"));
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

    public Note getNote() {
        return note;
    }

    public void setNote(Note note) {
        this.note = note;
    }
}
