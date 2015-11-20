package es.uja.git.sm.examples;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.widget.Toast;

/**
 * Se debe declarar en el manifiesto
 * Si se quiere que sea privado a la aplicación
 * se puede especificar android:exported "false"
 * 
 * IntentService es una clase que hereda de Service incorpora una hebra de trabajo
 * y no hace falta implementarla, tan sólo implementar su método onHandleIntent()
 * que recibirá el intent con los datos de la llamada. Este tipo de servicios
 * tan sólo permite una llamada a la vez, si se necesita un servicio que soporte
 * múltiples llamadas simultáneas se debe crear a través de la clase Service
 * @author Juan Carlos
 *
 */
public class ServiceDownload extends Service {

	public static final String PARAMETER_URL="url";

	
	private String 	url;
	String result="";
	
	
	
	
	/**
	 * SERVICIO INDEPENDIENTE: no finaliza cuando quien lo llamó termina o es cerrado
	 * por eso se debe controlar desde el propio servicio su finalización a través
	 * de una llamada a stopSelf() o a stopService() desde otra aplicación.
	 * 
	 * onStartCommand: Se llama cuando el servicio es creado a través de startService()
	 */
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		SocketConnection task=null;
		if(intent.hasExtra(ServiceDownload.PARAMETER_URL))
			url=intent.getCharSequenceExtra(ServiceDownload.PARAMETER_URL).toString();
		
		
		try {
			task= new SocketConnection();
			task.execute(new URL(url));
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return super.onStartCommand(intent, flags, startId);
	}
	
	
	
	/**
	 * SERVICIO VINCULADO: finaliza si la aplicación o componente que lo creó termina
	 * 
	 * onBind: se llama cuando se ejecuta bindService()
	 */
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Se llama cuando el servicio se incia por primera vez por la llamada a onBind()
	 * o a onStartComand()
	 */
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
	}

	/**
	 * Se llama por el sistema cuando el servicio
	 */
	@Override
	public void onDestroy() {
		Toast.makeText(this, "Servicio terminado: descargados "+result.length()+" bytes", Toast.LENGTH_SHORT).show();
		super.onDestroy();
	}

	private class SocketConnection extends AsyncTask<URL, String, String> {
		@Override
		protected String doInBackground(URL... urls) {

			// params comes from the execute() call: params[0] is the url.

			return conectaSocket(urls[0]);

		}

		// onPostExecute displays the results of the AsyncTask.

		protected void onPostExecute(final String value) {
			setResult(value);
			stopSelf();
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

	void setResult(String value)
	{
		this.result=value;
	}
	
	
	
	

		// Reads an InputStream and converts it to a String.
		public String readIt(InputStream stream, int len) throws IOException,
				UnsupportedEncodingException {
			Reader reader = null;
			reader = new InputStreamReader(stream, "UTF-8");
			char[] buffer = new char[len];
			
			reader.read(buffer);
			return new String(buffer);
		}
}
