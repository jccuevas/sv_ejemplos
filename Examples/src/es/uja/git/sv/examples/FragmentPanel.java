package es.uja.git.sv.examples;

import java.util.ArrayList;
import java.util.List;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import es.uja.git.sv.utils.Car;

public class FragmentPanel extends Fragment implements OnShowMessage{

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		
		View fragment = inflater.inflate(R.layout.layout_panel, null);
		
		setHasOptionsMenu(true);//Para comunicar que el panel quiere recibir los eventos de la barra de acci�n
		
		ListView lista = (ListView) fragment.findViewById(R.id.listView1);
		
		
		//Inicializaci�n del adaptador
		List<Car> cars = new ArrayList<Car>();
				
		Car c1= new Car("SL500","Mercedes",500,1);
		Car c2= new Car("Z4","BMW",450,2);
		Car c3= new Car("R8","Audi",525,3);
		Car c4= new Car("500","FIAT",60,4);
				
		cars.add(c1);
		cars.add(c2);
		cars.add(c3);
		cars.add(c4);
		
		
		NewListAdapter nla= new NewListAdapter(this.getActivity(),cars);
		
		lista.setAdapter(nla);
		
		return fragment;
		
	}
	
	public void publica(CharSequence text)
	{

		TextView t= (TextView) getActivity().findViewById(R.id.panel_textView_centro);
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
