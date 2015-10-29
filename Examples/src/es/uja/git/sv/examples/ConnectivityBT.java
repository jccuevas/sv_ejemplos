package es.uja.git.sv.examples;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public final class ConnectivityBT extends Activity {

	private static final String TAG = "Bluetooth";
	private static final int REQUEST_ENABLE_BT = 0;

	private TextView mTextStatus;
	private BluetoothAdapter mBluetoothAdapter;
	private boolean mIsBTEnabled = false;
	private BroadcastReceiver mReceiver = null;
	private ArrayAdapter<String> mArrayAdapter = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_bluetooth);

		// Setup UI
		mTextStatus = (TextView) findViewById(R.id.bluetooth_textView_result);
		mArrayAdapter = new ArrayAdapter<String>(this, 0);
		// Setup Bluetooth
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if (mBluetoothAdapter == null) {
			// Device does not support Bluetooth
			Toast.makeText(this,
					getString(R.string.connectivity_bt_notsupported),
					Toast.LENGTH_LONG).show();

			setResult(RESULT_CANCELED);
			finish();

		}

		checkBT();
		
		// Si el BT se activa se crea un BroadcastReceiver para
		// ACTION_FOUND
		mReceiver = new BroadcastReceiver() {
			public void onReceive(Context context, Intent intent) {
				String action = intent.getAction();
				// When discovery finds a device
				if (BluetoothDevice.ACTION_FOUND.equals(action)) {
					// Get the BluetoothDevice object from the Intent
					BluetoothDevice device = intent
							.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
					// Add the name and address to an array adapter to
					// show in a ListView
					mArrayAdapter.add(device.getName() + "\n"
							+ device.getAddress());
					Toast.makeText(
							getApplicationContext(),
							device.getName() + "\n"
									+ device.getAddress(),
							Toast.LENGTH_LONG).show();
				}
			}

		};
		// Register the BroadcastReceiver
		IntentFilter filter = new IntentFilter(
				BluetoothDevice.ACTION_FOUND);
		registerReceiver(mReceiver, filter); // Don't forget to
												// unregister during
												// onDestroy

	}

	protected void checkBT() {
		// Se comprueba si BT es activo
		if (!mBluetoothAdapter.isEnabled()) {
			Intent enableBtIntent = new Intent(
					BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
		}
		else
			this.mIsBTEnabled = true;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_conectividad_bt, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

		case R.id.menu_searchBT:
			Toast.makeText(
					this,
					this.getResources().getString(
							R.string.bluetooth_scanningdevices),
					Toast.LENGTH_LONG).show();

			if (this.mIsBTEnabled && mReceiver != null) {
				mBluetoothAdapter.startDiscovery();
				Log.d(TAG, "onClick() bluetooth look for devices");
			}
			return true;
		}

		return false;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case REQUEST_ENABLE_BT:
			if (resultCode == RESULT_OK) {
				this.mIsBTEnabled = true;

			
			} else
				this.mIsBTEnabled = false;
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

}
