package es.uja.git.sv.examples;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import es.uja.git.sv.examples.R;


public class Help extends Activity {
	 @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.layout_help);
	        
   
	        
	    }

	    @Override
	    public boolean onCreateOptionsMenu(Menu menu) {
	        //getMenuInflater().inflate(R.menu.activity_menus, menu);
	        return true;
	    }

		@Override
		public boolean onOptionsItemSelected(MenuItem item) {
			
			return false;
		}
}
