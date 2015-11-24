package es.uja.git.sm.examples;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.widget.Toast;
import es.uja.git.sm.utils.Sounds;

public class AlarmService extends IntentService {

	public static final String SLEEPTIME = "sleeptime";
	private AudioManager mAudioManager;
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
		long sleepTime = intent.getLongExtra(SLEEPTIME, 10000);
		int times = 1;
		// Normally we would do some work here, like download a file.
		// For our sample, we just sleep for 5 seconds.
		long endTime = System.currentTimeMillis() + sleepTime * 5;
		
		
		
		
		mAudioManager = (AudioManager) getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
		// Request audio focus for playback
		int result = mAudioManager.requestAudioFocus(new AudioManager.OnAudioFocusChangeListener(){

			@Override
			public void onAudioFocusChange(int focusChange) {
				if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT) {
					Sounds.pause();
				} else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
					
					Sounds.resume();
				} else if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
					// am.unregisterMediaButtonEventReceiver(RemoteControlReceiver);
					mAudioManager.abandonAudioFocus(null);
					Sounds.stopLoop();
				}
				
			}},
		// Use the music stream.
				AudioManager.STREAM_MUSIC,
				// Request permanent focus.
				AudioManager.AUDIOFOCUS_GAIN);
		
		Sounds.initSounds(getApplicationContext());
		while (System.currentTimeMillis() < endTime) {
			synchronized (this) {
				try {
					wait(sleepTime);
					Sounds.playSound(getApplicationContext(), Sounds.SOUND_1);
					times++;

				} catch (Exception e) {
				}
			}
		}
	}

	@Override
	public void onDestroy() {
		mAudioManager.abandonAudioFocus(null);
		super.onDestroy();
	}

	
}
