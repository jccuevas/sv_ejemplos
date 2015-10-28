package es.uja.git.sv.examples;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import es.uja.git.sv.examples.R;
import es.uja.git.sv.examples.FragmentLista.OnFragmentInteractionListener;

public class FragmentosDinamicos extends Activity implements OnFragmentInteractionListener{
	public static final String TAG_PANEL = "panel";
	public static final String TAG_BARRA = "barra";

	private FragmentManager mManager = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_fragment_dynamic);

		mManager = getFragmentManager();
		Fragment fl = mManager.findFragmentById(R.id.fragment_container);
		if (fl == null) {
			FragmentLista lista = new FragmentLista();
			FragmentTransaction ft = mManager.beginTransaction();
			ft.add(R.id.fragment_container, lista, TAG_BARRA);
			ft.commit();

		}
		if(findViewById(R.id.fragment_rigthframe)!=null)
		{
		Fragment fr = mManager.findFragmentById(R.id.fragment_rigthframe);
		if (fr == null) {
			FragmentPanel panel = new FragmentPanel();
			FragmentTransaction ft = mManager.beginTransaction();
			ft.add(R.id.fragment_rigthframe, panel, TAG_BARRA);
			ft.commit();
		}
		}
	}

	@Override
	public void onFragmentInteraction(int position) {
		// TODO Auto-generated method stub
		
	}

}
