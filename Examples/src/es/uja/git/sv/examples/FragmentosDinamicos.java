package es.uja.git.sv.examples;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;

public class FragmentosDinamicos extends Activity {
	public static final String TAG_PANEL = "panel";
	public static final String TAG_BARRA = "barra";

	private FragmentManager mManager = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_fragment_dynamic);

		mManager = getFragmentManager();
		Fragment fl = mManager.findFragmentById(R.id.fragment_container);
		if (fl != null) {
			FragmentLista lista = new FragmentLista();
			FragmentTransaction ft = mManager.beginTransaction();
			ft.add(R.id.fragment_container, lista, TAG_BARRA);
			ft.commit();

		}
		Fragment fr = mManager.findFragmentById(R.id.fragment_rigthframe);
		if (fr != null) {
			FragmentPanel panel = new FragmentPanel();
			FragmentTransaction ft = mManager.beginTransaction();
			ft.add(R.id.fragment_container, panel, TAG_BARRA);
			ft.commit();
		}

	}

}
