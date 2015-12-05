package com.kritsin.notes.api;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.kritsin.notes.db.DbCommonHelper;
import com.kritsin.notes.db.NoteDbAdapter;
import com.kritsin.notes.model.Note;
import com.kritsin.notes.model.NoteResponse;
import com.kritsin.notes.model.NotesResponse;
import com.kritsin.notes.model.ServerResponseStatus;
import com.kritsin.notes.model.User;
import com.kritsin.notes.model.UserResponse;
import com.kritsin.notes.util.Config;
import com.kritsin.notes.util.PreferenceUtils;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SyncTask extends AsyncTask<Void, Void, NotesResponse> {

    private final int userId;
    private OnSyncListener listener;
    private Context context;

    public interface OnSyncListener{
        public void onUserLoginResult(ServerResponseStatus responseStatus);
    }

    public SyncTask(Context context, int userId, OnSyncListener listener) {
        this.context = context;
        this.userId = userId;
        this.listener = listener;
    }

    @Override
    protected NotesResponse doInBackground(Void... params) {
        DbCommonHelper db = new DbCommonHelper(context);
        NoteDbAdapter dbAdapter = new NoteDbAdapter();
        List<Note> data = dbAdapter.getNotesForSending(db.open(),userId);
        for(Note note:data) {
            NoteResponse response = send(note);
            if(response.getResponseCode()==ServerResponseStatus.OK)
                dbAdapter.deleteNoteById(db.open(), note.getId());
        }

        dbAdapter.deleteNotes(db.open(), userId);
        NotesResponse result = load();
        return result;
    }

    private NoteResponse send(Note note){
        NoteResponse result = new NoteResponse();
        result.setResponseCode(ServerResponseStatus.ERROR);

        HttpClient httpclient = new DefaultHttpClient();

        HttpPost httprequest = new HttpPost(Config.API_URL+"put.aspx");
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
        nameValuePairs.add(new BasicNameValuePair("userId", userId+""));
        nameValuePairs.add(new BasicNameValuePair("note", note.getNote()+""));

        HttpResponse response = null;
        String responseString = null;
        try {
            httprequest.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
            response = httpclient.execute(httprequest);

            StatusLine statusLine = response.getStatusLine();
            if(statusLine.getStatusCode() == HttpStatus.SC_OK){
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                response.getEntity().writeTo(out);
                out.close();
                responseString = out.toString();

                Log.i("responseString", responseString);

                result = new NoteResponse(new JSONObject(responseString));
            } else{
                response.getEntity().getContent().close();
                throw new IOException(statusLine.getReasonPhrase());
            }
        }  catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private NotesResponse load(){
        NotesResponse result = new NotesResponse();
        result.setResponseCode(ServerResponseStatus.ERROR);

        HttpClient httpclient = new DefaultHttpClient();

        HttpPost httprequest = new HttpPost(Config.API_URL+"get.aspx");
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
        nameValuePairs.add(new BasicNameValuePair("userId", userId+""));

        HttpResponse response = null;
        String responseString = null;
        try {
            httprequest.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
            response = httpclient.execute(httprequest);

            StatusLine statusLine = response.getStatusLine();
            if(statusLine.getStatusCode() == HttpStatus.SC_OK){
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                response.getEntity().writeTo(out);
                out.close();
                responseString = out.toString();

                Log.i("responseString", responseString);

                result = new NotesResponse(new JSONObject(responseString));
                DbCommonHelper db = new DbCommonHelper(context);
                NoteDbAdapter dbAdapter = new NoteDbAdapter();
                dbAdapter.addNotes(db.open(), result.getNotes(), userId);
            } else{
                response.getEntity().getContent().close();
                throw new IOException(statusLine.getReasonPhrase());
            }
        }  catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    protected void onPostExecute(NotesResponse response) {
        if(listener!=null)
            listener.onUserLoginResult(response.getResponseCode());
    }

}