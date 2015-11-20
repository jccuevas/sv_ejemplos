package es.uja.git.sm.examples;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import es.uja.git.sm.examples.FragmentLista.OnFragmentInteractionListener;


public class Fragmentos extends Activity implements OnFragmentInteractionListener, Registros {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_fragments);

		FragmentManager fm = getFragmentManager();
		Fragment fragment = fm.findFragmentById(R.id.fragment_viewer);
		if (fragment == null) {
			FragmentTransaction ft = fm.beginTransaction();
			FragmentPanel panel = new FragmentPanel();
			ft.add(R.id.fragment_viewer, panel);
			ft.commit();
		}

	}

	public void show() {
		FragmentManager fm = getFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();

		Fragment fragment = fm.findFragmentById(R.id.fragment_viewer);

		if (fragment != null) {

			if (!fragment.isVisible()) {
				ft.show(fragment);
				ft.setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
				// ft.addToBackStack(null);
				ft.commit();
			}

		}

	}

	public void replace() {
		FragmentManager fm = getFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();

		Object fragment = fm.findFragmentById(R.id.fragment_viewer);

		if (fragment == null) {

			FragmentOther otro = new FragmentOther();
			ft.add(R.id.fragment_viewer, otro);
			ft.setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
			ft.addToBackStack(null);
			ft.commit();

		} else {

			ft.remove((Fragment) fragment);
			if (fragment instanceof FragmentPanel) {
				FragmentOther f = new FragmentOther();
				ft.replace(R.id.fragment_viewer, f);

			} else {
				FragmentPanel f = new FragmentPanel();
				ft.replace(R.id.fragment_viewer, f);
			}

			ft.setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
			ft.addToBackStack(null);
			ft.commit();

		}

	}

	public void hide() {
		FragmentManager fm = getFragmentManager();
		Fragment fragment = fm.findFragmentById(R.id.fragment_viewer);

		FragmentTransaction ft = fm.beginTransaction();

		if (fragment != null) {

			ft.hide(fragment);
			ft.setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
			// ft.addToBackStack(null);
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
			hide();
			return true;
		case R.id.menu_showPanel:
			show();
			return true;
		case R.id.menu_replacePanel:
			replace();
			return true;
		}
		return false;
	}

	@Override
	public void onFragmentInteraction(int position) {
		Object panel = getFragmentManager().findFragmentById(R.id.fragment_viewer);
		position = position + 1;
		if (panel != null)
			if (panel instanceof FragmentPanel) {
				((FragmentPanel) panel).publica("Pulsada opción " + position);
			} else {
				((FragmentOther) panel).publica("Pulsada opción " + position);
			}

	}

	@Override
	public boolean nuevoRegistro(String key, String value) {
		// TODO Auto-generated method stub
		return false;
	}

}
