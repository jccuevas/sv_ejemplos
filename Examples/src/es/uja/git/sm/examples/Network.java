package es.uja.git.sm.examples;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;


public class Network extends Activity {
	private static final String DEBUG_TAG = "NETWORK ACTIVITY";

	private ProgressBar progressBar = null;
	private NetworkWebFragment web = null;
	private DownloadWebpageText task = null;
	private EditText postParams = null;
	private WebView webView = null;

	boolean conectado = false;

	private PostQuery taskPost;

	private BroadcastReceiver mReceiver = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_network);

		FragmentManager fm = getFragmentManager();
		web = (NetworkWebFragment) fm.findFragmentById(R.id.layout_fragment_network_web);

		ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isConnected()) {
			conectado = true;
			Toast.makeText(this, "Conectado", Toast.LENGTH_LONG).show();// fetch
																		// data
																		// }
		} else { // display error }
			conectado = false;
			Toast.makeText(this, "No Conectado", Toast.LENGTH_LONG).show();
		}

		mReceiver = new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
				Bundle extras = intent.getExtras();

				if (extras != null) {
					boolean failover = extras.getBoolean("failover");
					boolean noConnectivity = extras.getBoolean("noConnectivity");
					NetworkInfo ninfo =extras.getParcelable("networkInfo");
					if(ninfo!=null && noConnectivity)
						Toast.makeText(Network.this, "Cambio en la conexión "+ninfo.getTypeName()+" [ACTIVA]", Toast.LENGTH_LONG).show();
					else
						Toast.makeText(Network.this, "Cambio en la conexión "+ninfo.getTypeName()+" [INACTIVA]", Toast.LENGTH_LONG).show();
				} else
					Toast.makeText(Network.this, "Cambio en la conexión", Toast.LENGTH_LONG).show();

			}
		};

		registerReceiver(mReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
		Log.d(DEBUG_TAG, "onCreate()");

		webView = (WebView) findViewById(R.id.network_web_webView);
		// WebSettings webSettings = webView.getSettings();
		// webSettings.setJavaScriptEnabled(true);

		final NetworkURLFragment uri = (NetworkURLFragment) fm.findFragmentById(R.id.layout_fragment_network_URL);

		postParams = (EditText) findViewById(R.id.network_navigation_edittext_postparams);

		progressBar = (ProgressBar) findViewById(R.id.network_url_downloadprogress);

		Button go = (Button) findViewById(R.id.network_url_browse);
		go.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				if (uri != null) {
					webView.setWebViewClient(new WebViewClient() {
						public void onReceivedError(WebView view, int errorCode, String description,
								String failingUrl) {
							Toast.makeText(Network.this, "Oh no! " + description, Toast.LENGTH_SHORT).show();
						}
					});

					webView.loadUrl(uri.getURLString());
				}
			}
		});
	}

	@Override
	protected void onDestroy() {
		if (mReceiver != null)
			unregisterReceiver(mReceiver);
		super.onDestroy();
	}

	// Uses AsyncTask to create a task away from the main UI thread. This task
	// takes a
	// URL string and uses it to create an HttpUrlConnection. Once the
	// connection
	// has been established, the AsyncTask downloads the contents of the webpage
	// as
	// an InputStream. Finally, the InputStream is converted into a string,
	// which is
	// displayed in the UI by the AsyncTask's onPostExecute method.
	private class DownloadWebpageText extends AsyncTask<String, Integer, String> {
		String mMimeType = "";
		String mEncoding = "";

		@Override
		protected String doInBackground(String... urls) {

			InputStream is = null;
			String result = "";

			HttpURLConnection conn = null;
			try {
				String contentAsString = "";
				String tempString = "";
				URL url = new URL(urls[0]);
				System.out.println("Abriendo conexión: " + url.getHost() + " puerto=" + url.getPort());
				conn = (HttpURLConnection) url.openConnection();
				conn.setReadTimeout(10000 /* milliseconds */);
				conn.setConnectTimeout(15000 /* milliseconds */);
				conn.setRequestMethod("GET");
				conn.setDoInput(true);
				// Starts the query
				conn.connect();
				final int response = conn.getResponseCode();
				final int contentLength = conn.getHeaderFieldInt("Content-length", 1000);
				mMimeType = conn.getHeaderField("Content-Type");
				mEncoding = mMimeType.substring(mMimeType.indexOf(";"));
				progressBar.setMax(contentLength);

				Log.d(DEBUG_TAG, "The response is: " + response);
				is = conn.getInputStream();

				BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));

				while ((tempString = br.readLine()) != null) {
					contentAsString = contentAsString + tempString;
					this.publishProgress(contentAsString.length());
				}

				// Convert the InputStream into a string
				is.close();
				conn.disconnect();
				return contentAsString;
			} catch (IOException e) {
				result = "Excepción: " + e.getMessage();
				System.out.println(result);

				// Makes sure that the InputStream is closed after the app is
				// finished using it.
			}
			return result;
		}

		// onPostExecute displays the results of the AsyncTask.
		protected void onPostExecute(final String result) {
			webView.loadData(result, mMimeType, mEncoding);
			web.setText(result);

		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			progressBar.setProgress(values[0]);
			progressBar.postInvalidate();
			super.onProgressUpdate(values);
		}

	}

	private class PostQuery extends AsyncTask<String, Integer, String> {
		@Override
		protected String doInBackground(String... urls) {

			InputStream is = null;
			String result = "";

			HttpURLConnection conn = null;
			try {
				String contentAsString = "";
				String tempString = "";
				URL url = new URL(urls[0]);
				System.out.println("Abriendo conexión: " + url.getHost() + " puerto=" + url.getPort());
				conn = (HttpURLConnection) url.openConnection();
				conn.setReadTimeout(10000 /* milliseconds */);
				conn.setConnectTimeout(15000 /* milliseconds */);
				conn.setRequestMethod("POST");

				conn.setDoInput(true);
				conn.setDoOutput(true);

				// Send request
				OutputStream os = conn.getOutputStream();
				BufferedWriter wr = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));

				wr.write(urls[1]);
				wr.flush();
				wr.close();
				// Starts the query
				conn.connect();
				final int response = conn.getResponseCode();
				final int contentLength = conn.getHeaderFieldInt("Content-length", 1000);
				progressBar.setMax(contentLength);

				Log.d(DEBUG_TAG, "The response is: " + response);
				is = conn.getInputStream();

				BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));

				while ((tempString = br.readLine()) != null) {
					contentAsString = contentAsString + tempString;
					publishProgress(contentAsString.length());
				}

				if (is != null) {
					is.close();
					conn.disconnect();
				}
				return contentAsString;
			} catch (IOException e) {
				result = "Excepción: " + e.getMessage();
				System.out.println(result);

				// Makes sure that the InputStream is closed after the app is
				// finished using it.
			}
			return result;
		}

		// onPostExecute displays the results of the AsyncTask.
		protected void onPostExecute(final String result) {
			web.setText(result);

		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			progressBar.setProgress(values[0]);
			progressBar.postInvalidate();
			super.onProgressUpdate(values);
		}

	}

	private class SocketConnection extends AsyncTask<URL, String, String> {
		ProgressDialog pbar = null;

		@Override
		protected String doInBackground(URL... urls) {

			// params comes from the execute() call: params[0] is the url.

			return conectaSocket(urls[0]);

		}

		@Override
		protected void onPreExecute() {

			pbar = new ProgressDialog(Network.this);
			pbar.setIndeterminate(true);
			pbar.setMessage(getBaseContext().getString(R.string.socket_downloading));

			pbar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			pbar.setCancelable(false);
			if (!pbar.isShowing()) {
				pbar.show();
			}
			super.onPreExecute();
		}

		@Override
		protected void onProgressUpdate(String... values) {
			// TODO Auto-generated method stub
			super.onProgressUpdate(values);
		}

		// onPostExecute displays the results of the AsyncTask.
		protected void onPostExecute(final String result) {
			web.setText(result);
			if (pbar != null)
				pbar.dismiss();

		}

	}

	/**
	 * Se ejecuta al pulsar el botón conectar con get
	 * 
	 * @param view
	 */
	public void onConnectHTTP(View view) {
		FragmentManager fm = getFragmentManager();

		NetworkURLFragment uri = (NetworkURLFragment) fm.findFragmentById(R.id.layout_fragment_network_URL);

		if (uri != null) {

			progressBar.setProgress(0);
			web.setText("");
			task = new DownloadWebpageText();
			task.execute(uri.getURLString());

		}

	}

	/**
	 * Se ejecuta al pulsar el botón conectar con get
	 * 
	 * @param view
	 */
	public void onConnectHTTPPost(View view) {
		FragmentManager fm = getFragmentManager();

		NetworkURLFragment uri = (NetworkURLFragment) fm.findFragmentById(R.id.layout_fragment_network_URL);

		if (uri != null) {

			progressBar.setProgress(0);
			web.setText("");
			taskPost = new PostQuery();
			taskPost.execute(uri.getURLString(), postParams.getEditableText().toString());

		}

	}

	/**
	 * Se ejecuta al pulsar el botón conectar
	 * 
	 * @param view
	 */
	public void onConnectSocket(View view) {
		FragmentManager fm = getFragmentManager();
		NetworkURLFragment urifragment = (NetworkURLFragment) fm.findFragmentById(R.id.layout_fragment_network_URL);

		if (urifragment != null) {

			web.setText("");
			URL url = urifragment.getURI();

			SocketConnection task = new SocketConnection();
			task.execute(url);

		}

	}

	/**
	 * Se ejecuta al pulsar el botón conectar
	 * 
	 * @param view
	 */
	public void onNetworkService(View view) {
		FragmentManager fm = getFragmentManager();
		NetworkURLFragment urifragment = (NetworkURLFragment) fm.findFragmentById(R.id.layout_fragment_network_URL);

		if (urifragment != null) {

			// web.setText("");
			// URL url = urifragment.getURI();

			Intent intent = new Intent(this, Download.class);
			intent.putExtra(Download.PARAMETER_URL, urifragment.getURI().toString());
			startService(intent);

		}

	}

	public String conectaSocket(URL url) {

		if (url != null) {
			String contentAsString = "";
			Socket s = new Socket();
			InputStream is;
			DataOutputStream dos;

			try {
				String line = null;
				int port = url.getPort();
				s = new Socket(url.getHost(), port);

				is = s.getInputStream();
				dos = new DataOutputStream(s.getOutputStream());

				dos.writeUTF("GET / HTTP/1.1\r\n" + "HOST=www10.ujaen.es\r\n" + "Connection: close\r\n"
						+ "Accept: text/*\r\n" + "User-Agent: UJAClient (Windows NT 10.0; WOW64)\r\n"
						+ "Accept-Language: es-ES,es;q=0.8,en;q=0.6");
				dos.flush();

				BufferedReader reader = new BufferedReader(new InputStreamReader(is));

				while ((line = reader.readLine()) != null) {
					line = line + "\r\n";

					if (line.contains("Content-Length")) {
						String datalength = line.substring(line.indexOf("Content-Length"));
					}
					contentAsString = contentAsString + line;
				}
				dos.close();
				is.close();
				s.close();
				return contentAsString;
			} catch (IOException e) {
				return e.getMessage();

			} catch (IllegalArgumentException e) {
				return e.getMessage();

			}
		}
		return "Conexión fallida";

	}

}
