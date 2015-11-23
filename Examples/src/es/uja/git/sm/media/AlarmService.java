package es.uja.git.sm.media;

import android.app.IntentService;
import android.content.Intent;
import android.widget.Toast;

public class AlarmService extends IntentService {

	/**
	 * A constructor is required, and must call the super IntentService(String)
	 * constructor with a name for the worker thread.
	 */
	public AlarmService() {
		super("PlayService");
	}

	/**
	 * The IntentService calls this method from the default worker thread with
	 * the intent that started the service. When this method returns,
	 * IntentService stops the service, as appropriate.
	 */
	@Override
	protected void onHandleIntent(Intent intent) {
		long sleepTime = intent.getLongExtra("sleeptime", 10000);
		int times = 1;
		// Normally we would do some work here, like download a file.
		// For our sample, we just sleep for 5 seconds.
		long endTime = System.currentTimeMillis() + sleepTime * 5;
		while (System.currentTimeMillis() < endTime) {
			synchronized (this) {
				try {
					wait(sleepTime);
					Toast.makeText(getBaseContext(), times + " Despierta!!!", Toast.LENGTH_LONG).show();
					times++;

				} catch (Exception e) {
				}
			}
		}
	}

}
