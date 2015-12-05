package com.kritsin.notes.api;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.kritsin.notes.R;
import com.kritsin.notes.model.ServerResponseStatus;
import com.kritsin.notes.model.User;
import com.kritsin.notes.model.UserResponse;
import com.kritsin.notes.util.Config;
import com.kritsin.notes.util.PreferenceUtils;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UserLoginTask extends AsyncTask<Void, Void, UserResponse> {

    private final String login;
    private final String password;
    private OnUserLoginListener listener;
    private Context context;

    public interface OnUserLoginListener{
        public void onUserLoginResult(ServerResponseStatus responseStatus);
    }

    public UserLoginTask(Context context, String login, String password, OnUserLoginListener listener) {
        this.context = context;
        this.login = login;
        this.password = password;
        this.listener = listener;
    }

    @Override
    protected UserResponse doInBackground(Void... params) {
        UserResponse result = new UserResponse();
        result.setResponseCode(ServerResponseStatus.ERROR);

        HttpClient httpclient = new DefaultHttpClient();

        HttpPost httprequest = new HttpPost(Config.API_URL+"login.aspx");
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
        nameValuePairs.add(new BasicNameValuePair("login", login));
        nameValuePairs.add(new BasicNameValuePair("password", password));

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

                result = new UserResponse(new JSONObject(responseString));
                User user = result.getUser();
                user.setLogin(login);
                user.setPassword(password);
                PreferenceUtils.saveUser(context, user);
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
    protected void onPostExecute(UserResponse response) {
        if(listener!=null)
            listener.onUserLoginResult(response.getResponseCode());
    }

}