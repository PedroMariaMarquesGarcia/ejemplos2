package main;

import java.util.Scanner;
import java.io.IOException;
import java.net.Socket;
import java.io.PrintWriter;
import java.io.FileOutputStream;

public class Cliente {

    public static void main(String[] args) {
        int puertoServidor = 1500;
       
    	//Cuando lanzamos el proceso cliente intentamos conectarnos al servidor
        try (
    		Socket socket = new Socket("localhost", puertoServidor);
            Scanner datosRecibidos = new Scanner(socket.getInputStream());
            Scanner	datosUsuario = new Scanner(System.in);
        	PrintWriter datosEnviados = new PrintWriter(socket.getOutputStream(), true)
        ) {
        	
        	//Recibimos instrucciones del servidor
            String datosImprimir = datosRecibidos.nextLine();
            System.out.println(datosImprimir);
           
            while (true) {
            	//Enviamos datos (solicitamos archivo)
            	String data = datosUsuario.nextLine();
            	datosEnviados.println(data);

	            datosImprimir = datosRecibidos.nextLine();
	           
            	//Si el archivo existe nos preparamos para recibirlo
	            if (datosImprimir.equals("El archivo Existe")) {
	            	
	            	System.out.println(datosImprimir);
	                
	            	try(FileOutputStream fos = new FileOutputStream("./src/ficherosRecibir/"+"descarga_" + data);){
		                byte[] buffer = new byte[4096];
		                int bytesRead;
		                while ((bytesRead = socket.getInputStream().read(buffer)) != -1) {
		                    fos.write(buffer, 0, bytesRead);
		                }	
	                }
	                System.out.println("Archivo recibido correctamente.");
	                try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {e.printStackTrace();}

	                socket.close();
	            } else {
	            	//Si el archivo No existe recibimos notificación y salimos
	            	
	                System.out.println(datosImprimir);
	                try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {e.printStackTrace();}

	                socket.close();
	            }
            }
		} catch (IOException e) {System.out.println("No se ha podido conectar con el servidor");}
    }
}