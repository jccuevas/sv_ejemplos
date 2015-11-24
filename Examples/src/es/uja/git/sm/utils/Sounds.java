package es.uja.git.sm.utils;

import java.util.HashMap;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import es.uja.git.sm.examples.R;



public class Sounds {
	private static SoundPool soundPool;
	private static HashMap<Integer,Integer> soundPoolMap;


	public static final int SOUND_1 = R.raw.s1;
	public static final int SOUND_2 = R.raw.s2;
	public static final int SOUND_3 = R.raw.s3;
	
	/** Populate the SoundPool */

	public static void initSounds(Context context) {

		
		soundPool = new SoundPool(3, AudioManager.STREAM_MUSIC, 0);

		soundPoolMap = new HashMap<Integer,Integer>(3);

		soundPoolMap.put(SOUND_1, soundPool.load(context, R.raw.s1, 1));
		soundPoolMap.put(SOUND_2, soundPool.load(context, R.raw.s2, 2));
		soundPoolMap.put(SOUND_3, soundPool.load(context, R.raw.s3, 3));

	}

	
	public static void playSound(Context context, int soundID) {
		if (soundPool == null || soundPoolMap == null) {
			initSounds(context);
		}
		float volume = 1.0f;// whatever in the range = 0.0 to 1.0

		// play sound with same right and left volume, with a priority of 1,
		// zero repeats (i.e play once), and a playback rate of 1f
		soundPool.play((Integer) soundPoolMap.get(soundID), volume, volume, 0,
				0, 1f);
	}

	public static void playSoundLoop(Context context, int soundID) {
		if (soundPool == null || soundPoolMap == null) {
			initSounds(context);
		}
		float volume = 1.0f;// whatever in the range = 0.0 to 1.0

		// play sound with same right and left volume, with a priority of 1,
		// zero repeats (i.e play once), and a playback rate of 1f
		soundPool.play((Integer) soundPoolMap.get(soundID), volume, volume, 1,
				-1, 1f);
	}
	
	public static void stopLoop()
	{
		soundPool.stop(SOUND_1);
	}

	public static void pause()
	{
		soundPool.autoPause();
	}

	public static void resume()
	{
		soundPool.autoResume();
	}

}
