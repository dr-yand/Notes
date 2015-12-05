package com.kritsin.notes.api;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.kritsin.notes.util.Config;


public class GeneralTask extends AsyncTask<Void, String, String>{

	private Map<String, Object> mParams;
  
	public interface OnResultListener {
		public void onParseResult(String response);
        public void onReturnResult();
	}

    public enum RequestType{
        GET, POST;
    }

	private OnResultListener mListener;
	private Context mContext;
    private String mUrl;

    private RequestType mRequestType;

    public GeneralTask(OnResultListener listener, Context context, Map<String, Object> params){
        this(listener, Config.API_URL,context,params,RequestType.POST);
    }

	public GeneralTask(OnResultListener listener, Context context, Map<String, Object> params, RequestType requestType){
        this(listener, Config.API_URL,context,params,requestType);
    }


    public GeneralTask(OnResultListener listener, String url, Context context, Map<String, Object> params, RequestType requestType){
        this.mListener=listener;
        this.mContext=context;
        this.mParams = params;
        this.mUrl = url;
        this.mRequestType = requestType;
    }

    @Override
    protected String doInBackground(Void... par) {
        if(mRequestType==RequestType.POST){
            return postRequest();
        }
        else{
            return getRequest();
        }
    }

    private String postRequest(){
        HttpClient httpclient = new DefaultHttpClient();

        HttpPost httprequest = new HttpPost(mUrl);


        HttpResponse response = null;
        String responseString = null;
        try {
            httprequest.setEntity(new StringEntity(createBody(), "UTF-8"));
            response = httpclient.execute(httprequest);

            StatusLine statusLine = response.getStatusLine();
            if(statusLine.getStatusCode() == HttpStatus.SC_OK){
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                response.getEntity().writeTo(out);
                out.close();
                responseString = out.toString();

                Log.i("responseString",responseString);

                if(mListener!=null)
                    mListener.onParseResult(responseString);
            } else{
                response.getEntity().getContent().close();
                throw new IOException(statusLine.getReasonPhrase());
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return responseString;
    }

    private String getRequest(){
        HttpClient httpclient = new DefaultHttpClient();

        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
        for(String key:mParams.keySet()){
            nameValuePairs.add(new BasicNameValuePair(key, mParams.get(key).toString()));
        }
        String paramsString = URLEncodedUtils.format(nameValuePairs, "UTF-8");

        HttpGet httprequest = new HttpGet(mUrl+"?"+paramsString);


        HttpResponse response = null;
        String responseString = null;
        try {
//            httprequest.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
            response = httpclient.execute(httprequest);

            StatusLine statusLine = response.getStatusLine();
            if(statusLine.getStatusCode() == HttpStatus.SC_OK){
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                response.getEntity().writeTo(out);
                out.close();
                responseString = out.toString();

                Header[] headers = response.getAllHeaders();
                for (Header header : headers) {
                    Log.i("header","Key : " + header.getName()
                            + " ,Value : " + header.getValue());
                }

//                String server = response.getFirstHeader("Server").getValue();

                Log.i("responseString",responseString);

                if(mListener!=null)
                    mListener.onParseResult(responseString);
            } else{
                response.getEntity().getContent().close();
                throw new IOException(statusLine.getReasonPhrase());
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return responseString;
    }

    private String createBody(){
        JSONObject jsonObject = new JSONObject();
        try {
            for (String key : mParams.keySet()) {
                Object object = mParams.get(key);
                if(object instanceof JSONObject)
                    jsonObject.put(key, object);
                else if(object instanceof JSONArray)
                    jsonObject.put(key, object);
                else
                    jsonObject.put(key, object);
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    @Override
	protected void onCancelled() {
		mListener = null;
		super.onCancelled();
	}

	@Override
    protected void onPostExecute(String response) {
        super.onPostExecute(response);
        if(mListener!=null)
            mListener.onReturnResult();
    }
}