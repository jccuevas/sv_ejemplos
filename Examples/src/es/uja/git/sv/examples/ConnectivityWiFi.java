package es.uja.git.sv.examples;

import java.util.List;


import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import es.uja.git.sv.examples.R;


public class ConnectivityWiFi extends Activity {

	private static final String TAG = "Connectivity";
	WifiManager wifi;
	BroadcastReceiver receiver;

	TextView textStatus;
	Button buttonScan;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_connectivity);

		// Setup UI
		textStatus = (TextView) findViewById(R.id.connectivity_textView_result);

		// Setup WiFi
		wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);

		// Get WiFi status
		WifiInfo info = wifi.getConnectionInfo();
		textStatus.append("\n\nWiFi Status: " + info.toString());

		// List available networks
		List<WifiConfiguration> configs = wifi.getConfiguredNetworks();

		for (WifiConfiguration config : configs) {
			textStatus.append("\n\n" + config.toString());
		}

		// Se registra el Broadcast Receiver para detectar 
		//el final de la búsqueda de redes wifi
		if (receiver == null)
			receiver = new WiFiScanReceiver(this);

		registerReceiver(receiver, new IntentFilter(
				WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
		Log.d(TAG, "onCreate()");

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_conectividad, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

		case R.id.menu_searchWifi:
			Toast.makeText(
					this,
					this.getResources().getString(
							R.string.connectivity_code_startscan),
					Toast.LENGTH_LONG).show();

			Log.d(TAG, "onClick() wifi.startScan()");
			wifi.startScan();
			return true;
		}

		return false;
	}

}
