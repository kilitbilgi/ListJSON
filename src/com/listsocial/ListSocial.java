package com.listsocial;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ListActivity;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

public class ListSocial extends ListActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list_social);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);
		/*
		 * Tabhost objesi olustur ve view ile baglatisini bagli oldugu id ile
		 * yap
		 */
		TabHost tabhost = (TabHost) findViewById(android.R.id.tabhost);
		tabhost.setup();

		/*
		 * Yeni bir sekme ekle ve view ile baglatisini bagli oldugu id ile yap
		 */
		TabSpec spec1 = tabhost.newTabSpec("sk");
		spec1.setContent(R.id.tab1);
		spec1.setIndicator("Son Kurları Listele");

		/*
		 * Yeni bir sekme ekle ve view ile baglatisini bagli oldugu id ile yap
		 
		TabSpec spec2 = tabhost.newTabSpec("su");
		spec2.setContent(R.id.tab2);
		spec2.setIndicator("Son Ucuşları Listele");
		*/
		/*
		 * Olusturulan iki sekmeyi tabhost yapisina ekle
		 */
		tabhost.addTab(spec1);
		//tabhost.addTab(spec2);

		String readFeed = readFeedsk();
		TextView txtView = (TextView) findViewById(R.id.textView1);

		List<String> foexList = new ArrayList<String>();
		List<String> buyList = new ArrayList<String>();
		List<String> sellList = new ArrayList<String>();

		try {
			JSONArray jsonArray = new JSONArray(readFeed);
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				foexList.add(jsonObject.get("foex").toString());
				buyList.add(jsonObject.get("buy").toString());
				sellList.add(jsonObject.get("sell").toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		String temp = "";

		for (int i = 0; i < buyList.size(); i++) {
			temp += foexList.get(i) + "-> TR - Alış : " + buyList.get(i)
					+ " Satış:" + sellList.get(i) + "\n";
		}
		txtView.setText(temp);
	}

	public String readFeedsk() {
		StringBuilder builder = new StringBuilder();
		HttpClient client = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(
				"http://api.piyasa.com/json/?kaynak=doviz_guncel_serb");
		try {
			HttpResponse response = client.execute(httpGet);
			StatusLine statusLine = response.getStatusLine();
			int statusCode = statusLine.getStatusCode();
			if (statusCode == 200) {
				HttpEntity entity = response.getEntity();
				InputStream content = entity.getContent();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(content));
				String line;
				while ((line = reader.readLine()) != null) {
					builder.append(line);
				}
			} else {
				//
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return builder.toString();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.list_social, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
