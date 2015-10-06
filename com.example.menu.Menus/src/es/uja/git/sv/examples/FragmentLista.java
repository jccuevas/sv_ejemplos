package es.uja.git.sv.examples;

import android.app.Fragment;
import android.app.ListFragment;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class FragmentLista extends ListFragment {

	 boolean mDualPane=true;
	    int mCurCheckPosition = 0;
	
	
	
	 @Override
	    public void onActivityCreated(Bundle savedInstanceState) {
	        super.onActivityCreated(savedInstanceState);

	        // Populate list with our static array of titles.
	        String[] values = new String[] { "Opción 1", "Opción 2"};
	      

	        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
	                android.R.layout.simple_list_item_1, values);
	            setListAdapter(adapter);
	   
	    }

	
	 @Override
	    public void onSaveInstanceState(Bundle outState) {
	        super.onSaveInstanceState(outState);
	        outState.putInt("curChoice", mCurCheckPosition);
	    }

	    @Override
	    public void onListItemClick(ListView l, View v, int position, long id) {
	    	Fragment panel = getFragmentManager().findFragmentById(R.id.fragment_viewer);
            if (panel != null && panel.getClass().isInstance(Panel.class)) {
                // Make new fragment to show this selection.
                ((Panel)panel).publica("Pulsado "+position);
            }
	    }

	 
	
}
