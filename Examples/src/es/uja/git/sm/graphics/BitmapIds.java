package es.uja.git.sm.graphics;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import es.uja.git.sm.examples.R;

public class BitmapIds {

	public static final int DISCO_1 = 0;
	public static final int DISCO_2 = 1;

	protected Context context;

	public static final Integer[] mBitmaps = { android.R.drawable.bottom_bar };

	public BitmapIds(Context context) {
		this.context = context;
	}

	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	public Bitmap getBitmap(int id) {
		Bitmap bitmap = null;

		switch (id) {
		case DISCO_1:

			final BitmapDrawable drawable1;
			if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP_MR1) {
				drawable1 = (BitmapDrawable) context.getResources().getDrawable(R.drawable.disco01);

			} else {
				drawable1 = (BitmapDrawable) context.getResources().getDrawable(R.drawable.disco01, context.getTheme());
			}

			bitmap = drawable1.getBitmap();
			break;
		case DISCO_2:

			final BitmapDrawable drawable2;
			if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
				drawable2 = (BitmapDrawable) context.getResources().getDrawable(R.drawable.disco01);

			} else {
				drawable2 = (BitmapDrawable) context.getResources().getDrawable(R.drawable.disco01, context.getTheme());
			}
			bitmap = drawable2.getBitmap();
			break;

		}
		return bitmap;
	}

}
