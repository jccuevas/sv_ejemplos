package es.uja.git.sv.examples;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import es.uja.git.sv.examples.R;

public class Prueba extends Activity {

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.layout_prueba);
		
		FragmentManager fm = this.getFragmentManager();
		
		FragmentTransaction ft = fm.beginTransaction();
		
		FragmentoPrueba fragment = new FragmentoPrueba();
		
		ft.add(R.id.fragment_prueba_container,fragment);
		
		ft.commit();
		
		
	}
	
	

}
