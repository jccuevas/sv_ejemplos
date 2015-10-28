package es.uja.git.sv.examples;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import es.uja.git.sv.examples.R;

public class FragmentoPrueba extends Fragment {

	
	public static FragmentoPrueba newInstance(int value)
	{
		FragmentoPrueba fragment= new FragmentoPrueba();
		
		Bundle args = new Bundle();
		args.putInt("param1", value);
		
		fragment.setArguments(args);
		return fragment;
		
	}
	
	
	
	/* (non-Javadoc)
	 * @see android.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
				
		

		View fragment = inflater.inflate(R.layout.fragment_prueba, container,false);
		
		Button lanzar = (Button)fragment.findViewById(R.id.fragmentPrueba_new);
		
		lanzar.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				Intent newactivity_help = new Intent(
						getActivity().getApplicationContext(), Help.class);
				startActivity(newactivity_help);
				
			}});
		
		return fragment;
		
		
		
		
	}
	
	

}
