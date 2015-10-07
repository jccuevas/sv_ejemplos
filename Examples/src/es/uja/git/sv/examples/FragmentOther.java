package es.uja.git.sv.examples;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class FragmentOther extends Fragment implements OnShowMessage{
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
	
		View fragment = inflater.inflate(R.layout.layout_other, null);
		setHasOptionsMenu(true);//Para comunicar que el panel quiere recibir los eventos de la barra de acción
		
		return fragment;
		
	}
	
	public void publica(CharSequence text)
	{

		TextView t= (TextView) getActivity().findViewById(R.id.other_textView_centro);
		if(t!=null)
		{
			t.setText(text);
		}
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater menuinflater) {
		menuinflater.inflate(R.menu.menu_fragment_panel, menu);
		
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch(item.getItemId())
		{
		case R.id.menu_pulsaFragmentPanel:
			Toast.makeText(getActivity(), getResources().getString(R.string.fragmentPanelMenuPulsa),Toast.LENGTH_SHORT).show();
			return true;
		
		}
		return super.onOptionsItemSelected(item);
	}
	
	
	

}
