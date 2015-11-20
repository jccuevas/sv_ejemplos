package es.uja.git.sm.examples;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Date;

/**
 * Esta clase sirve para modelas los mensajes de un protcolo ficticio tipo al del ejercicio 1
 * @author Juan Carlos
 *
 */
public class Mensaje {

	protected static int secuencia;
	protected int secuenciaRecibida = 0;
	protected long fecha;
	private String mensaje = "";
	private Coordenadas data = null;

	public Mensaje(Coordenadas c) {
		Date d = new Date();
		fecha = d.getTime();
		data = c;

		mensaje = secuencia + " " + fecha + " " + c.toString();

		secuencia++;
		
		
	
		
	}
	/**
	 * Este método genera un array de bytes con la PDU del protocolo
	 * @return el array con los datos del mensaje
	 */
	public byte[] toByteArray()
	{
		ByteArrayOutputStream bos = new ByteArrayOutputStream(5);
		DataOutputStream dos = new DataOutputStream(bos);
		
		try {
			dos.writeInt(secuencia);
			dos.writeLong(fecha);
			data.toByteArray(dos);
			dos.close();
			return bos.toByteArray();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public Mensaje(String datos, byte[] bytedata) {

		ByteArrayInputStream bais = new ByteArrayInputStream(bytedata);

		DataInputStream dis = new DataInputStream(bais);
		try {
			this.secuenciaRecibida = dis.readInt();
			this.fecha = dis.readLong();
		String datso= dis.readUTF();

		} catch (IOException ex) {
		}
finally{
		String[] campos = datos.split(" ");
		if (campos.length == 4) {
			secuenciaRecibida = Integer.parseInt(campos[0]);
			fecha = Long.parseLong(campos[1]);
			data = new Coordenadas(campos[2], campos[3]);
		}}
	}

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

}
