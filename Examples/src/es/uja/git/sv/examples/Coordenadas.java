package es.uja.git.sv.examples;

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
	
	public void toByteArray (DataOutputStream dos){
		try {
			dos.writeDouble(this.latitud);
			dos.writeDouble(this.longitud);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	
	}

	public String toString()
	{
		return  latitud+" "+longitud;
		
	}
}
