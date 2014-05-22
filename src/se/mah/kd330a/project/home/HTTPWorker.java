package se.mah.kd330a.project.home;

/*
 * HTTPWorker.java
 * 
 * Connects to, and interacts with, the stenslie_webeditor database.
 * 
 * Copyright (C) 2011  1scale1 Handelsbolag
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONException;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class HTTPWorker extends AsyncTask<String, Integer, String> {

	/** Unique class identifier, for the log */
	private static final String TAG = "HTTPWorker";

	/** Message types & key/value pairs */

	/** Server url */
	private static final String SERVER = "https://api.instagram.com/v1/";
	
	private static final String CLIENT_ID = "459cd68d82d642bfb73678ceb77290cd";

	/** Timeout for establishing connection */
	private final int connectionTimeoutMillis = 3000;
	
	/** Timeout for socket - how long to wait for data to arrive */
	private final int socketTimeoutMillis = 5000;
	
	/** Context in which the HttpTask is executed */
	private Context mContext;

	/* UI */
	boolean showprogress = true;
	private ProgressDialog mProgressDialog;

	/** Foreground task link */
	private Handler sHandler;

	/** The task */
	private int task;

	/** Task constants */
	public final static int GET_LATEST_TAGS = 100;

	/** Print debug variables */
	private static final boolean DEBUG = true;

	public HTTPWorker(Context ctx, Handler sHandler, int task,
			boolean showprogress) {
		/* Set the context */
		this.mContext = ctx;

		/* Set the handler, used for communication back to activity */
		this.sHandler = sHandler;

		this.task = task;

		this.showprogress = showprogress;
	}

	@Override
	protected void onPreExecute() {
		if (showprogress) {
			/* Init the ProgressDialog here */
			mProgressDialog = new ProgressDialog(HTTPWorker.this.mContext);
			mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			mProgressDialog.setMessage("Working...");
			mProgressDialog.setCancelable(false);

			/* Show the dialog */
			mProgressDialog.show();
		}

		super.onPreExecute();
	}

	@Override
	protected String doInBackground(String... params) {
		/* URL */
		String url = null;

		/* POST parameters */
		List<NameValuePair> arguments = new ArrayList<NameValuePair>();

		switch (task) {
		case GET_LATEST_TAGS:
			url = SERVER + "tags/";
			url += params[0] + "/media/recent"; // First param is the tag to search for
			arguments.add(new BasicNameValuePair("client_id", CLIENT_ID));
			break;
		}

		/* Fire the http post and return the response to onPostExecute */
		try {
			return httpget(url, arguments);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return "";
	}

	@Override
	protected void onPostExecute(String json_response) {
		/* json_response == null, means something went wrong in the http request */
		if (json_response != null) {
			if (DEBUG)
				Log.i(TAG, "Got response: " + json_response);

			/* The package to be returned */
			Bundle data = new Bundle();

			/* Create message */
			Message mMessage = null;

			/* Populate data, attach to message, and send */
			switch (task) {
			case GET_LATEST_TAGS:
				try {
					/* Get the data */
					data.putSerializable("data", JSONParser.getTags(json_response));
					/* Get the message */
					mMessage = sHandler.obtainMessage(GET_LATEST_TAGS);

					/* Attach data to message */
					mMessage.setData(data);
				} catch (JSONException e) {
					/* Exception handling... */
					mMessage = null;

					e.printStackTrace();
				}
				break;
			}

			/* Send message, if construction went well */
			if (mMessage != null) {
				mMessage.sendToTarget();
			}

			/* Dismiss the progress dialog, if it's running */
			if (mProgressDialog != null && mProgressDialog.isShowing())
				mProgressDialog.dismiss();
			
		}else{
			if (DEBUG)
				Log.i(TAG, "No response, closing down.");
			
			/* Dismiss the progress dialog, if it's running */
			if (mProgressDialog != null && mProgressDialog.isShowing())
				mProgressDialog.dismiss();
		}
		
		super.onPostExecute(json_response);
	}

	private String httpget(String url, List<NameValuePair> params) throws URISyntaxException, ClientProtocolException,
			IOException {
		HttpClient client = new DefaultHttpClient();

		if (!url.endsWith("?") && params.size() > 0)
			url += "?";
		
		String paramString = URLEncodedUtils.format(params, "utf-8");
		url += paramString;
		
		if (DEBUG)
			Log.i(TAG, url);
		
		// Skapa metoden (GET)
		HttpGet get_request = new HttpGet();

		// Peka på rätt skript
		get_request.setURI(new URI(url));
		
		// Sätt timeouts
		HttpParams httpParameters = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParameters, connectionTimeoutMillis);
		HttpConnectionParams.setSoTimeout(httpParameters, socketTimeoutMillis);
		
		// Skicka request och få svar från servern
		HttpResponse response = client.execute(get_request);

		// Vi behöver en inputstream för att skapa en reader
		InputStream is = response.getEntity().getContent();
		InputStreamReader isreader = new InputStreamReader(is);
		BufferedReader reader = new BufferedReader(isreader);

		// Buffer för svaret
		StringBuffer sb = new StringBuffer();

		// Läs av hela svaret, rad för rad
		String line = "";
		while ((line = reader.readLine()) != null) {
			sb.append(line);
		}

		return sb.toString();
	}
}
