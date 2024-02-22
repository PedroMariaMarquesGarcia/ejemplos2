package main;

import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.PrintWriter;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.io.BufferedReader;


public class HCliente extends Thread {
	private Socket clienteSocket;
	private String token = null;
	
	//Constructor para la conexion de clientes	
	public HCliente(Socket clienteSocket) {
		this.clienteSocket = clienteSocket;
	}
	
	@Override
	public void run() {
		
		try {
			while( !checkToken() )
			{
				autenticar();
			}
		} catch (IOException | InterruptedException e) {};

	}


	private void autenticar() throws IOException, InterruptedException {
		
		// Solicitar usuario
		enviarDatos("Ingrese usuario:");
		String user = recibirDatos();
		System.out.println(user);
		
		//Solicitar contraseña
		enviarDatos("Ingrese contraseña:");
		String pw = recibirDatos();

	}

	private boolean checkToken() throws IOException {
		String[] datosRecibidos = recibirDatos().split(";");
		String t = datosRecibidos[0];
		String status = "0;";
		System.out.println(datosRecibidos[1]);
		boolean check = false;
		
		if(t.equals(null)) {
			// No se ha proporcionado token
			// Acceso Denegado;
			enviarDatos(status);
		}
		else if(this.token != null && !this.token.equals(t)) {
			// Existe token y el proporcionado no es valido
			// Acceso Denegado;
			status = "1;";
			enviarDatos(status);
		}
		else if(this.token != null && this.token.equals(t)) {
			// Existe token y el proporcionado es valido
			// Acceso garantizado;
			status = "2;";
			enviarDatos(status);
			check = true;
		}
		return check;
	}
	
	private String recibirDatos() throws IOException {
		InputStream inputStream = this.clienteSocket.getInputStream();
		String datosCliente = null;
		
		byte[] buffer = new byte[1024];
        int bytesRead = inputStream.read(buffer);
        String respuesta = new String(buffer, 0, bytesRead);
        
		datosCliente = respuesta;
		
		return datosCliente;
	}
	
	private void enviarDatos(String mensaje) throws IOException {
		OutputStream outputStream = this.clienteSocket.getOutputStream();
		String datos = mensaje;
		
		outputStream.write(datos.getBytes());
	}
}
