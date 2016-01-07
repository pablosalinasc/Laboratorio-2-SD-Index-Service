/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package laboratorio_2_sd_indexservice;

import java.io.*;
import java.net.*;
/**
 *
 * @author ñuño
 */
public class Cliente {
    
    public static void main(String args[]) throws Exception{
        //Variables
        String sentence="";
        
        try{
            //Buffer para recibir desde el usuario
            BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));

            //Socket para el cliente (host, puerto)
            Socket clientSocket = new Socket("localhost", 5000);

            //Buffer para enviar el dato al server
            DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());

            //Buffer para recibir dato del servidor
            BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            while(true){
                //Leemos del cliente y lo mandamos al servidor
                System.out.print("Ingrese su consulta:");
                sentence = inFromUser.readLine();
                if(!sentence.equals("exit")){
                    outToServer.writeBytes(sentence + '\n');
                }else {
                    break;
                }
                
                //Recibimos del servidor
                String tagIndex=inFromServer.readLine();
                if(tagIndex.equals("0")){
                    System.out.println("No hay respuesta");
                }else{
                    int largoTop = Integer.parseInt(tagIndex);
                    System.out.println("-----------------------");
                    System.out.println("Consulta: "+sentence);
                    System.out.println("Respuesta:");
                    for(int i=0;i<largoTop;i++){
                        String titulo=inFromServer.readLine();
                        System.out.println("Top "+(i+1));
                        System.out.println(" - Titulo: "+titulo);
                        String url=inFromServer.readLine();
                        System.out.println(" - URL: "+url);
                        String usuario=inFromServer.readLine();
                        System.out.println(" - Usuario: "+usuario);
                    }
                    System.out.println("------------------------\n");
                }
            }

            //Cerramos el socket
            clientSocket.close();
        }catch(IOException ex){
            System.out.println("\nALERTA: Servidor no disponible!!");
        }
    }
}
