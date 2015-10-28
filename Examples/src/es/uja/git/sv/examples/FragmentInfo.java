package es.uja.git.sv.examples;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import es.uja.git.sv.examples.R;

public class FragmentInfo extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
			
		
		View fragment = inflater.inflate(R.layout.layout_fragment_info, null);
		
		
		
		return fragment;
		
	}
	
	public void publica(CharSequence text)
	{

		TextView t= (TextView) getActivity().findViewById(R.id.fragmentinfo_helptext);
		if(t!=null)
		{
			t.setText(text);
		}
	}

	
	
	
	
	
}
