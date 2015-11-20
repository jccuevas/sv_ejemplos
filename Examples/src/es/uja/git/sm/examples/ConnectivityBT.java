package es.uja.git.sm.examples;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import android.annotation.TargetApi;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


public final class ConnectivityBT extends Activity {

	private static final String TAG = "Bluetooth";
	private static final int REQUEST_ENABLE_BT = 0;
	public static final int MESSAGE_READ = 0;
	public static final String MESSAGE_KEY_DATAREAD = "dataread";

	private ListView mList;
	private BluetoothAdapter mBluetoothAdapter;
	private boolean mIsBTEnabled = false;
	private BroadcastReceiver mReceiver = null;
	private BluetoothListAdapter mArrayAdapter = null;

	private List<BluetoothDevice> mDeviceList = new ArrayList<BluetoothDevice>(0);

	private final Handler mHandler = new Handler() {

		public void handleMessage(Message msg) {

			// String aResponse = msg.getData().getString("message");

			switch (msg.what) {
			case ConnectivityBT.MESSAGE_READ:

				Bundle data = msg.getData();
				// ALERT MESSAGE
				Toast.makeText(getBaseContext(),
						"Server Response: " + new String(data.getByteArray(MESSAGE_KEY_DATAREAD)), Toast.LENGTH_SHORT)
						.show();
				break;
			}

		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_bluetooth);

		// Setup UI
		mList = (ListView) findViewById(R.id.bluetooth_listview_result);
		mArrayAdapter = new BluetoothListAdapter(this, null);

		mList.setAdapter(mArrayAdapter);
		// Setup Bluetooth
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if (mBluetoothAdapter == null) {
			// Device does not support Bluetooth
			Toast.makeText(this, getString(R.string.connectivity_bt_notsupported), Toast.LENGTH_LONG).show();

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
					BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
					BluetoothClass btclass = intent.getParcelableExtra(BluetoothDevice.EXTRA_CLASS);
					int rssi = intent.getShortExtra(BluetoothDevice.EXTRA_RSSI, Short.MIN_VALUE);

					// Add the name and address to an array adapter to
					// show in a ListView

					mDeviceList.add(device);

					mArrayAdapter = new BluetoothListAdapter(getApplicationContext(), mDeviceList);
					mList.setAdapter(mArrayAdapter);

				}
			}

		};
		// Register the BroadcastReceiver
		IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
		registerReceiver(mReceiver, filter); // Don't forget to
												// unregister during
												// onDestroy

	}

	@Override
	protected void onDestroy() {
		if (mReceiver != null)
			unregisterReceiver(mReceiver);
		super.onDestroy();
	}

	protected void checkBT() {
		// Se comprueba si BT es activo
		if (!mBluetoothAdapter.isEnabled()) {
			Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
		} else
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
			Toast.makeText(this, this.getResources().getString(R.string.bluetooth_scanningdevices), Toast.LENGTH_LONG)
					.show();

			((TextView) findViewById(R.id.bluetooth_textview_devicelist)).setText(R.string.bluetooth_devicelist);
			if (this.mIsBTEnabled && mReceiver != null) {
				mArrayAdapter = new BluetoothListAdapter(this, null);

				mDeviceList.clear();
				mBluetoothAdapter.startDiscovery();
				Log.d(TAG, "menu buscar - Buscando dispositivos");
			}
			return true;
		case R.id.menu_searchBonded:
			if (this.mIsBTEnabled && mReceiver != null) {

				((TextView) findViewById(R.id.bluetooth_textview_devicelist))
						.setText(R.string.bluetooth_bondeddevicelist);
				Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
				// If there are paired devices
				if (pairedDevices.size() > 0) {

					// Loop through paired devices
					for (BluetoothDevice device : pairedDevices) {

						// Adds the device to the list of the adapter
						mDeviceList.add(device);
					}
					mArrayAdapter = new BluetoothListAdapter(getApplicationContext(), mDeviceList);
					mList.setAdapter(mArrayAdapter);
					mList.invalidate();
				} else {
					Toast.makeText(this, getString(R.string.bluetooth_nobondeddevices), Toast.LENGTH_LONG).show();

				}

			}

			return true;
		case R.id.menu_startBTserver:
			if (this.mIsBTEnabled && mReceiver != null) {

				new AcceptThread().start();

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

	private class ConnectThread extends Thread {
		private final BluetoothSocket mmSocket;
		private final BluetoothDevice mmDevice;
		private final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

		public ConnectThread(BluetoothDevice device) {
			// Use a temporary object that is later assigned to mmSocket,
			// because mmSocket is final
			BluetoothSocket tmp = null;
			mmDevice = device;

			// Get a BluetoothSocket to connect with the given BluetoothDevice
			try {
				// MY_UUID is the app's UUID string, also used by the server
				// code
				// 00001101-0000-1000-8000-00805F9B34FB

				tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
			} catch (IOException e) {
			}
			mmSocket = tmp;
		}

		public void run() {
			// Cancel discovery because it will slow down the connection
			mBluetoothAdapter.cancelDiscovery();

			try {
				// Connect the device through the socket. This will block
				// until it succeeds or throws an exception
				mmSocket.connect();
			} catch (IOException connectException) {
				// Unable to connect; close the socket and get out
				try {
					mmSocket.close();
				} catch (IOException closeException) {
				}
				return;
			}

			// Do work to manage the connection (in a separate thread)
			new ConnectedThread(mmSocket).start();
		}

		/** Will cancel an in-progress connection, and close the socket */
		public void cancel() {
			try {
				mmSocket.close();
			} catch (IOException e) {
			}
		}
	}

	private class AcceptThread extends Thread {
		private final BluetoothServerSocket mmServerSocket;
		private final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
		private final String SERVICE_NAME = "BTDATA";

		public AcceptThread() {
			// Use a temporary object that is later assigned to mmServerSocket,
			// because mmServerSocket is final
			BluetoothServerSocket tmp = null;
			try {
				// MY_UUID is the app's UUID string, also used by the client
				// code
				tmp = mBluetoothAdapter.listenUsingRfcommWithServiceRecord(SERVICE_NAME, MY_UUID);
			} catch (IOException e) {
			}
			mmServerSocket = tmp;
		}

		public void run() {
			BluetoothSocket socket = null;
			// Keep listening until exception occurs or a socket is returned
			while (true) {
				try {
					socket = mmServerSocket.accept();
					Log.i(TAG + " SERVER", "Incoming connection from: " + socket.getRemoteDevice().getAddress());
				} catch (IOException e) {
					break;
				}
				// If a connection was accepted
				while (socket != null) {
					// Do work to manage the connection (in a separate thread)
					new ServerThread(socket).start();
					// mmServerSocket.close();
					break;
				}
			}
		}

		/** Will cancel the listening socket, and cause the thread to finish */
		public void cancel() {
			try {
				mmServerSocket.close();
			} catch (IOException e) {
			}
		}
	}

	private class ConnectedThread extends Thread {
		private final BluetoothSocket mmSocket;
		private final InputStream mmInStream;
		private final OutputStream mmOutStream;

		public ConnectedThread(BluetoothSocket socket) {
			mmSocket = socket;
			InputStream tmpIn = null;
			OutputStream tmpOut = null;

			// Get the input and output streams, using temp objects because
			// member streams are final
			try {
				tmpIn = socket.getInputStream();
				tmpOut = socket.getOutputStream();
			} catch (IOException e) {
			}

			mmInStream = tmpIn;
			mmOutStream = tmpOut;
		}

		public void run() {
			byte[] buffer = new String("Mensaje Bluetooth desde android").getBytes();
			int bytes; // bytes returned from read()

			Looper.prepare();

			// Keep listening to the InputStream until an exception occurs
			// while (true) {
			try {

				buffer = new String("Mensaje Bluetooth desde un cliente android").getBytes();

				mmOutStream.write(buffer);

				bytes = mmInStream.read(buffer);
				String data = new String(buffer);

				Log.i(TAG + " CLIENT", "Received " + bytes + " bytes from: " + mmSocket.getRemoteDevice().getAddress()
						+ "(" + mmSocket.getRemoteDevice().getName() + ")");
				Log.i(TAG + " CLIENT", "Received " + data);

				Message msgObj = mHandler.obtainMessage();
				msgObj.what = ConnectivityBT.MESSAGE_READ;
				Bundle b = new Bundle();
				b.putByteArray(MESSAGE_KEY_DATAREAD, buffer);
				msgObj.setData(b);
				mHandler.sendMessage(msgObj);

				Toast.makeText(getApplicationContext(), new String(buffer), Toast.LENGTH_LONG).show();

			} catch (IOException e) {
				Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
			}
			// }
			try {
				mmSocket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		/* Call this from the main activity to send data to the remote device */
		public void write(byte[] bytes) {
			try {
				mmOutStream.write(bytes);
			} catch (IOException e) {
			}
		}

		/* Call this from the main activity to shutdown the connection */
		public void cancel() {
			try {
				mmSocket.close();
			} catch (IOException e) {
			}
		}
	}

	private class ServerThread extends Thread {
		private final BluetoothSocket mmSocket;
		private final InputStream mmInStream;
		private final OutputStream mmOutStream;

		public ServerThread(BluetoothSocket socket) {
			mmSocket = socket;
			InputStream tmpIn = null;
			OutputStream tmpOut = null;

			// Get the input and output streams, using temp objects because
			// member streams are final
			try {
				tmpIn = socket.getInputStream();
				tmpOut = socket.getOutputStream();
			} catch (IOException e) {

			}

			mmInStream = tmpIn;
			mmOutStream = tmpOut;
		}

		public void run() {
			byte[] buffer = new String("Bienvenido Bluetooth desde android").getBytes(); // buffer
																							// store
																							// for
			Looper.prepare(); // the
			// stream
			int bytes; // bytes returned from read()

			// Keep listening to the InputStream until an exception occurs
			// while (true) {
			try {
				// Read from the InputStream
				// mmOutStream.write(buffer);
				// Send the obtained bytes to the UI activity

				bytes = mmInStream.read(buffer);

				String data = new String(buffer);

				Log.i(TAG + " SERVER", "Received " + bytes + " bytes from: " + mmSocket.getRemoteDevice().getAddress()
						+ "(" + mmSocket.getRemoteDevice().getName() + ")");
				Log.i(TAG + " SERVER", "Received " + data);

				Message msgObj = mHandler.obtainMessage();
				msgObj.what = ConnectivityBT.MESSAGE_READ;
				Bundle b = new Bundle();
				b.putByteArray(MESSAGE_KEY_DATAREAD, buffer);
				msgObj.setData(b);
				mHandler.sendMessage(msgObj);

				// Toast.makeText(getApplicationContext(), data,
				// Toast.LENGTH_LONG).show();

				buffer = ("OK " + data).getBytes();

				mmOutStream.write(buffer);

			} catch (IOException e) {

				Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
			}
			// }
			try {
				mmSocket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		/* Call this from the main activity to send data to the remote device */
		public void write(byte[] bytes) {
			try {
				mmOutStream.write(bytes);
			} catch (IOException e) {
			}
		}

		/* Call this from the main activity to shutdown the connection */
		public void cancel() {
			try {
				mmSocket.close();
			} catch (IOException e) {
			}
		}
	}

	static class ViewHolder {
		TextView name;
		TextView mac;
		ImageButton btlogo;

	}

	/**
	 * Adaptador para el la lista de dispositivos bluetooth
	 * 
	 * @author Juan Carlos
	 * 
	 */
	public class BluetoothListAdapter extends BaseAdapter {

		private final Context mContext;
		private List<BluetoothDevice> mValues = new ArrayList<BluetoothDevice>(1);

		public static final int ROW_BACKGORUND_ALPHA = 50;
		public static final int ROW_SELECTED_ALPHA = 80;

		public static final String TAG = "CustomAdapter";
		public int mConvertViewCounter = 0;

		private LayoutInflater mInflater = null;

		public BluetoothListAdapter(Context context, List<BluetoothDevice> values) {
			super();
			this.mContext = context;
			mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			if (values != null)
				this.mValues = values;

		}

		@TargetApi(Build.VERSION_CODES.LOLLIPOP_MR1)
		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {

			ViewHolder holder;

			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.lisview_row_btdevices, null);

				// convertViewCounter++;
				// Log.v(TAG, convertViewCounter + " convertViews have been
				// created");

				holder = new ViewHolder();

				holder.name = (TextView) convertView.findViewById(R.id.row_bt_name);
				holder.mac = (TextView) convertView.findViewById(R.id.row_bt_mac);
				holder.btlogo = (ImageButton) convertView.findViewById(R.id.row_bt_connect);

				holder.btlogo.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Log.d(TAG, "Click BT logo - iniciando conexión con " + mValues.get(position).getAddress() + " "
								+ mValues.get(position).getName());
						new ConnectThread(mValues.get(position)).start();

					}
				});
				convertView.setTag(holder);

			} else {
				holder = (ViewHolder) convertView.getTag();

				holder.name.setText(mValues.get(position).getName());
				holder.mac.setText(mValues.get(position).getAddress());
			}

			return convertView;
		}

		public int add(BluetoothDevice value) {
			this.mValues.add(value);
			return 0;
		}

		@Override
		public int getCount() {
			if (this.mValues != null) {
				return mValues.size();
			} else {
				return 0;
			}
		}

		@Override
		public Object getItem(int position) {

			if (this.mValues != null)

				if (position < mValues.size())
					return mValues.get(position);

			return null;
		}

		@Override
		public long getItemId(int arg0) {
			if (this.mValues != null)
				if (arg0 < mValues.size())
					return arg0;

			return -1;
		}

	}
}
