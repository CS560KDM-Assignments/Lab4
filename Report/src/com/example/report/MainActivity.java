package com.example.report;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.os.StrictMode;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

@SuppressLint("NewApi")
public class MainActivity extends Activity {
EditText stomp;
EditText rest;
EditText result;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

		StrictMode.setThreadPolicy(policy);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Button report = (Button) findViewById(R.id.report);
		stomp = (EditText) findViewById(R.id.stomp);
		rest = (EditText) findViewById(R.id.rest);
		result = (EditText) findViewById(R.id.result);
		
		report.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//String url = "http://134.193.136.127:8983/solr/collection1_shard1_replica1/select?q=id%3Astomp&wt=json&indent=true";
				String url = "http://10.0.2.2:8080/RestfulService/jaxrs/generic";
				String resultString = getJsonFromUrl(url);
				System.out.println(result);
				String stomp_count= JsonToValue(resultString,"stomp");
				//url="http://134.193.136.127:8983/solr/collection1_shard1_replica1/select?q=id%3Arest%0A&wt=json&indent=true";
				//result  = getJsonFromUrl(url);
				String rest_count = JsonToValue(resultString,"rest");
				stomp.setText(stomp_count);
				rest.setText(rest_count);
				if(Integer.parseInt(stomp_count) <= Integer.parseInt(rest_count))
				result.setText("Work out hard!");
				else
					result.setText("Good workout!!Keep going!!");
				
			}
		});
		
	}
	String JsonToValue(String response,String action)
	{
		String count="";
	try {
		
		JSONObject jsonObj = new JSONObject	(response);
		JSONObject res_header=jsonObj.getJSONObject("responseHeader");
		Integer status = res_header.getInt("status");
		if (status == 0){
			JSONObject res_body=jsonObj.getJSONObject("response");
			JSONArray Results = res_body.getJSONArray("docs");
			if ((Results.getJSONObject(0).getString("id")).equalsIgnoreCase(action))
				count = Results.getJSONObject(0).getJSONArray("title").getString(0);
			else if ((Results.getJSONObject(1).getString("id")).equalsIgnoreCase(action))
				count = Results.getJSONObject(1).getJSONArray("title").getString(0);
			System.out.println("counttttttttttttttttt::"+action+"       "+count);
		}
		
	} catch (JSONException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	return count;
	}
	//getting JSON from URL
		String getJsonFromUrl(String url)
		{System.out.println("url is"+url);
				HttpClient httpclient = new DefaultHttpClient();
			HttpResponse response;
			String responseString = null;
			try {
				HttpGet httpPost = new HttpGet(url);
				response = httpclient.execute(httpPost);
				StatusLine statusLine = response.getStatusLine();
				if(statusLine.getStatusCode() == HttpStatus.SC_OK){
					ByteArrayOutputStream out = new ByteArrayOutputStream();
					response.getEntity().writeTo(out);
					out.close();
					responseString = out.toString();
				} else{
					//Closes the connection.
					response.getEntity().getContent().close();
					throw new IOException(statusLine.getReasonPhrase());
				}
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}catch (Exception e) {
				e.printStackTrace();
			}
			System.out.println("rsponse::"+responseString);
			return responseString;
		}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
