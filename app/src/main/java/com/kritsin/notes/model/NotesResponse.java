package com.kritsin.notes.model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class NotesResponse {

    private ServerResponseStatus responseCode;
    private List<Note> notes;

    public List<Note> getNotes() {
        return notes;
    }

    public void setNotes(List<Note> notes) {
        this.notes = notes;
    }

    public NotesResponse(){

    }

    public NotesResponse(JSONObject jsonObject){
        try{
            responseCode = ServerResponseStatus.valueOf(jsonObject.getInt("response"));
            notes = new ArrayList<>();
            JSONArray notesArray = jsonObject.getJSONArray("data");
            for(int i=0;i<notesArray.length();i++) {
                Note note = new Note(notesArray.getJSONObject(i));
                notes.add(note);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public ServerResponseStatus getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(ServerResponseStatus responseCode) {
        this.responseCode = responseCode;
    }
}
