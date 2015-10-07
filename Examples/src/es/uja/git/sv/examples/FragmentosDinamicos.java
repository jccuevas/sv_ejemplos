package es.uja.git.sv.examples;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import es.uja.git.sv.examples.FragmentLista.OnFragmentInteractionListener;

public class FragmentosDinamicos extends Activity implements OnFragmentInteractionListener{
	public static final String TAG_PANEL = "panel";
	public static final String TAG_BARRA = "barra";

	private FragmentManager mManager = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_fragment_dynamic);

//		mManager = getFragmentManager();
//
//		FragmentBarra barra = new FragmentBarra();
//		FragmentTransaction ft = mManager.beginTransaction();
//		ft.add(R.id.fragment_container, barra, TAG_BARRA);
//		ft.commit();

	}

	public void show() {

		Fragment fragment = mManager.findFragmentByTag(TAG_PANEL);

		FragmentTransaction ft = mManager.beginTransaction();

		if (fragment == null) {

			FragmentPanel panel = new FragmentPanel();
			// Barra barra = new Barra();
			ft.add(R.id.fragment_container, panel, TAG_PANEL);

			ft.setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
			ft.addToBackStack(null);
			ft.commit();

		} else
		{
			
			// Barra barra = new Barra();
			ft.show(fragment);
			ft.setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
			ft.addToBackStack(null);
			ft.commit();
		}

	}

	public void replace() {

		Fragment fragment = mManager.findFragmentByTag(TAG_BARRA);

		FragmentTransaction ft = mManager.beginTransaction();

		if (fragment != null) {

			FragmentPanel panel = new FragmentPanel();

			ft.replace(R.id.fragment_container, panel);
			ft.setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
			ft.addToBackStack(null);
			ft.commit();

		}

	}

	public void hide() {

		Fragment fragment = mManager.findFragmentByTag(TAG_PANEL);

		FragmentTransaction ft = mManager.beginTransaction();

		if (fragment != null) {
			ft.hide(fragment);
			ft.setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
			ft.addToBackStack(null);
			ft.commit();
			
		}
			
		

	}

	
	private void swapBarFragment() {
		FragmentManager fragmentManager = getFragmentManager();
		Fragment f = fragmentManager.findFragmentById(R.id.fragmentBarra);
		if (f != null) {
			FragmentTransaction ft = fragmentManager.beginTransaction();
			ft.setCustomAnimations(R.anim.slide_in_up,
					R.anim.slide_out_down);
			if (f.isHidden())
				ft.show(f);
			else
				ft.hide(f);
			ft.commit();
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_fragmentos, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_hidePanel:
			swapBarFragment();
			return true;
		case R.id.menu_showPanel:
			show();
			return true;
		}
		return false;
	}

	@Override
	public void onFragmentInteraction(int position) {
		// TODO Auto-generated method stub
		
	}

}
