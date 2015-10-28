package es.uja.git.sv.examples;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import es.uja.git.sv.utils.Record;

public class Ficheros extends Activity {

	protected SQLiteTableRecords database=null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_ficheros);

		database = new SQLiteTableRecords(this);
		database.open();

	}

	public void onSave(View v) {
		int value;
		
		EditText edit_t = (EditText) findViewById(R.id.ficheros_editText_texto);
		EditText edit_n = (EditText) findViewById(R.id.ficheros_editText_value);
		EditText edit_f = (EditText) findViewById(R.id.ficheros_editText_filename);

		String tag = edit_t.getEditableText().toString();
		String value_s = edit_n.getEditableText().toString();
		String filename = edit_f.getEditableText().toString();

		if (value_s != null)
			try {
				value = Integer.valueOf(value_s);
			} catch (NumberFormatException es) {
				value = 0;
			}
		else
			value = 0;

		 Intent i = getIntent();
         // Le metemos el resultado que queremos mandar a la
         // actividad principal.
         i.putExtra("name", filename);
         
		try {
			FileOutputStream os = openFileOutput(filename, MODE_PRIVATE | MODE_APPEND);
			/* Escritura sin serializaci�n*/
			DataOutputStream dos = new DataOutputStream(os);
			dos.writeUTF(tag);
			dos.writeInt(value);
			dos.flush();
			dos.close();

			/*Escritura con serializaci�n*/
//			Record data = new Record(tag,value);
//			ObjectOutputStream oos = new ObjectOutputStream(os);
//			oos.writeObject(data);
//			oos.flush();
//			oos.close();
			/*Fin escritura co serializaci�n*/
			os.close();
			Toast.makeText(this,
					getResources().getString(R.string.toast_saved),
					Toast.LENGTH_SHORT).show();
		    setResult(RESULT_OK, i);

	         // Finalizamos la Activity para volver a la anterior
	         finish();

		} catch (IOException ex) {
			Toast.makeText(this,
					getResources().getString(R.string.toast_error),
					Toast.LENGTH_SHORT).show();
		    setResult(RESULT_CANCELED, i);

	         // Finalizamos la Activity para volver a la anterior
	         finish();

		}
		
		
	
         // Establecemos el resultado, y volvemos a la actividad
         // principal. La variable que introducimos en primer lugar
         // "RESULT_OK" es de la propia actividad, no tenemos que
         // declararla nosotros.
     
	}

	public void onSaveExternal(View v) {

		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) {

			EditText edit_t = (EditText) findViewById(R.id.ficheros_editText_texto);
			EditText edit_n = (EditText) findViewById(R.id.ficheros_editText_value);
			EditText edit_f = (EditText) findViewById(R.id.ficheros_editText_filename);

			String texto = edit_t.getEditableText().toString();
			String numero = edit_n.getEditableText().toString();
			String filename = edit_f.getEditableText().toString();

			int n;

			if (numero != null)
				try {

					n = Integer.valueOf(numero);
				} catch (NumberFormatException es) {
					n = 0;
				}
			else
				n = 0;

			try {
				File path = Environment.getExternalStoragePublicDirectory("");

				if (path != null) {
					File file = new File(path, filename);

					FileOutputStream os = new FileOutputStream(file, true);// el
																			// par�metro
																			// true
																			// indica
																			// que
																			// es
																			// para
																			// a�adir
					DataOutputStream dos = new DataOutputStream(os);
					dos.writeUTF(texto);
					dos.writeInt(n);
					dos.close();
					os.close();

					Toast.makeText(this,
							getResources().getString(R.string.toast_saved),
							Toast.LENGTH_SHORT).show();
				}

			} catch (IOException ex) {
				Toast.makeText(this,
						getResources().getString(R.string.toast_error),
						Toast.LENGTH_SHORT).show();

			}

		} else
			Toast.makeText(this,
					getResources().getString(R.string.toast_error_nomedia),
					Toast.LENGTH_SHORT).show();

	}

	public void onLoad(View v) {

		String texto = "";

		try {

			EditText edit_f = (EditText) findViewById(R.id.ficheros_editText_filename);
			TextView resultado = (TextView) findViewById(R.id.ficheros_textView_result);

			String filename = edit_f.getEditableText().toString();

			FileInputStream is = openFileInput(filename);
			
			/* Lectura sin serializaci�n */
			DataInputStream dos = new DataInputStream(is);
			
			int n = dos.available();
			texto = "Leidos (" + n + " bytes)\r\n";
			while (dos.available() > 0) {
				texto = texto + " clave: " + dos.readUTF() + " valor:"
						+ dos.readInt() + "\r\n";
			}
			dos.close();

	
			/*Lectura con serializaci�n */
//			ObjectInputStream ois = new ObjectInputStream(is);
//			Record data = null;
//			
//			
//				try {
//					boolean end=false;
//					while(!end)
//					{
//						try{
//					
//						data = (Record)ois.readObject();
//						texto=texto+data.toString()+"\r\n";
//						}catch(EOFException ex)
//						{
//							end=true;
//						}
//					}
//				} catch (ClassNotFoundException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			

					
			is.close();
			resultado.setText(texto);

			

			Toast.makeText(this,
					getResources().getString(R.string.toast_loaded),
					Toast.LENGTH_SHORT).show();

		} catch (IOException ex) {
			Toast.makeText(this,
					getResources().getString(R.string.toast_error),
					Toast.LENGTH_SHORT).show();

		}

	}

	public void onLoadExternal(View v) {

		String texto = "";

		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)
				|| Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {

			try {

				EditText edit_f = (EditText) findViewById(R.id.ficheros_editText_filename);
				TextView resultado = (TextView) findViewById(R.id.ficheros_textView_result);

				String filename = edit_f.getEditableText().toString();

				FileInputStream os = openFileInput(filename);
				DataInputStream dos = new DataInputStream(os);
				int n = dos.available();
				texto = "Leidos (" + n + " bytes)\r\n";
				while (dos.available() > 0) {
					texto = texto + " clave: " + dos.readUTF() + " valor:"
							+ dos.readInt() + "\r\n";
				}

				resultado.setText(texto);

				dos.close();
				os.close();

				Toast.makeText(this,
						getResources().getString(R.string.toast_loaded),
						Toast.LENGTH_SHORT).show();

			} catch (IOException ex) {
				Toast.makeText(this,
						getResources().getString(R.string.toast_error),
						Toast.LENGTH_SHORT).show();

			}

		} else
			Toast.makeText(this,
					getResources().getString(R.string.toast_error_nomedia),
					Toast.LENGTH_SHORT).show();

	}

	public void onSaveDatabase(View view) {

		if(database!=null)
		{	
			database.addRecord(getData());
		}
	}

	public void onLoadDatabase(View view) {

		List<Record> comentarios =database.getAllRecords();
		TextView resultado = (TextView) findViewById(R.id.ficheros_textView_result);
		String texto="";
		for(Record c : comentarios)
		{
			texto=texto+"\r\n"+c.getTag()+" valor="+c.getValue();

			

		}
		
		resultado.setText(texto);

	}

	public Record getData() {

		int n = 0;
		EditText edit_t = (EditText) findViewById(R.id.ficheros_editText_texto);
		EditText edit_n = (EditText) findViewById(R.id.ficheros_editText_value);

		String texto = edit_t.getEditableText().toString();
		String numero = edit_n.getEditableText().toString();

		if (numero != null)
			try {

				n = Integer.valueOf(numero);
			} catch (NumberFormatException es) {
				n = 0;
			}
		else
			n = 0;

		return new Record(texto, n);
	}

	@Override
	protected void onDestroy() {
		if(this.database!=null)
			this.database.close();
		super.onDestroy();
	}



}
