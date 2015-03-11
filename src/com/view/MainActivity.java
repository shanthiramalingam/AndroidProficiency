package com.view;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.adapter.CountryAdapter;
import com.country.R;
import com.model.Country;

public class MainActivity extends Activity implements OnItemClickListener {

	private ArrayList<Country> CountryList;
	public CountryAdapter adapter;
	public String title = null;
	public  String URL = "https://dl.dropboxusercontent.com/u/746330/facts.json";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		CountryList = new ArrayList<Country>();

		ListView listview = (ListView) findViewById(R.id.list);

		new JSONAsyncTask().execute(URL);

		adapter = new CountryAdapter(getApplicationContext(), R.layout.row,
				CountryList);

		listview.setAdapter(adapter);
		listview.setOnItemClickListener(this);

	}

	// JSONAsyncTask class for get data from JSON
	class JSONAsyncTask extends AsyncTask<String, Void, Boolean> {

		ProgressDialog dialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = new ProgressDialog(MainActivity.this);
			dialog.setMessage("Loading, please wait");
			dialog.setTitle("Connecting server");
			dialog.show();
			dialog.setCancelable(false);
		}

		@Override
		protected Boolean doInBackground(String... urls) {

			try {
				URL urlToRequest = new URL(URL);
				HttpURLConnection urlConnection = (HttpURLConnection) urlToRequest
						.openConnection();
				urlConnection.setConnectTimeout(10000);
				urlConnection.setReadTimeout(15000);

				int statusCode = urlConnection.getResponseCode();
				if (statusCode == 200) {
					InputStream in = new BufferedInputStream(
							urlConnection.getInputStream());

					JSONParsing(in);

				} else {

					return false;

				}

			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return true;

		}

		protected void onPostExecute(Boolean result) {
			dialog.cancel();
			adapter.notifyDataSetChanged();
			setTitle(title);
			if (result == false)
				Toast.makeText(getApplicationContext(),
						"Unable to fetch data from server", Toast.LENGTH_LONG)
						.show();

		}

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Toast.makeText(getApplicationContext(),
				CountryList.get(position).getName(), Toast.LENGTH_LONG).show();
	}

	private void JSONParsing(InputStream in) {

		try {
			JSONObject jsono = new JSONObject(getResponseText(in));
			title = jsono.getString("title");

			JSONArray jarray = jsono.getJSONArray("rows");

			for (int i = 0; i < jarray.length(); i++) {
				JSONObject object = jarray.getJSONObject(i);

				Country actor = new Country();
				if (!object.getString("title").equalsIgnoreCase("null")
						&& !object.getString("description").equalsIgnoreCase(
								"null")
						&& !object.getString("imageHref").equalsIgnoreCase(
								"null")) {
					actor.setName(object.getString("title"));
					actor.setDescription(object.getString("description"));
					actor.setImage(object.getString("imageHref"));

					CountryList.add(actor);

				}

			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	private static String getResponseText(InputStream inStream) {

		return new Scanner(inStream).useDelimiter("\\A").next();
	}
}
