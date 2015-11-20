package es.uja.git.sm.examples;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Coordenadas {
	
	protected double latitud=0.0;
	protected double longitud=0.0;
	
	public Coordenadas(double la, double lo)
	{
		this.latitud=la;
		this.longitud=lo;
		
	}
	
	public Coordenadas (String dato1,String dato2){
		this.latitud= Double.parseDouble(dato1);
		this.longitud=Double.parseDouble(dato2);

		
	}
	
	public Coordenadas (DataInputStream dis){
		try {
			this.latitud= dis.readDouble();
			this.longitud=dis.readDouble();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			this.latitud=0.0;
			this.longitud=0.0;
			e.printStackTrace();
		}
		

		
	}
	
	public byte[] toByteArray () throws IOException{
		
		ByteArrayOutputStream bos = new ByteArrayOutputStream(16);
		DataOutputStream dos = new DataOutputStream(bos);
	
			dos.writeDouble(this.latitud);
			dos.writeDouble(this.longitud);
			return bos.toByteArray();
		
	
	}

	public String toString()
	{
		return  latitud+" "+longitud;
		
	}

	public void toByteArray(DataOutputStream dos) {
		// TODO Auto-generated method stub
		
	}
}
