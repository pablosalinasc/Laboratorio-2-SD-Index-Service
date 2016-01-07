/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package laboratorio_2_sd_indexservice;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ñuño
 */

public class FrontService {
    static int nHebras;
    static int HebraActual;
    static Thread[] t;
    Result[] resultadoTemp;
    static BufferedReader inFromIndex;
    static DataOutputStream outToIndex;
    static String fromClient;
    static String processedData; 
        
    public FrontService(int sizeIndex,int nHebras) {
        this.nHebras=nHebras;
        this.HebraActual=0;
        this.t=new Thread[nHebras];
        this.resultadoTemp=new Result[nHebras];
    }
    
    
    
    Result AnswerQuery(Query consulta) throws InterruptedException{


                System.out.println("Consulta:");
                System.out.println("   - Terminos: "+consulta.terminos);
                System.out.println("   - Fecha: "+consulta.fecha);
                                
                try {
                    //enviar consulta a indexService                    
                    String terminosNorm= consulta.terminos.replace(" ", "+");;
                    outToIndex.writeBytes("GET /result/"+terminosNorm+"\n");
                    System.out.println("Envia a Index: GET /result/"+terminosNorm);
                    //recibir resultado del index
                    String tagIndex=inFromIndex.readLine();
                    System.out.println("Recibe de Index: '"+tagIndex+"'");
                    ArrayList<Documento> topTemp= new ArrayList<>();
                    if(tagIndex.equals("0")){
                        resultadoTemp[HebraActual]=null;
                    }else{
                        int largoTop= Integer.parseInt(tagIndex);
                        for(int i=0;i<largoTop;i++){
                            int idDoc = Integer.parseInt(inFromIndex.readLine());
                            String titulo=inFromIndex.readLine();
                            String usuario=inFromIndex.readLine();
                            Documento docTemp = new Documento(idDoc, titulo, usuario);
                            System.out.println("Recibe de Index: '"+docTemp.titulo+"' '"+docTemp.url+"' '"+docTemp.usuario+"'");
                            topTemp.add(docTemp);
                        }
                        resultadoTemp[HebraActual]=new Result(topTemp);
                    }
                    
                } catch (IOException ex) {
                    Logger.getLogger(FrontService.class.getName()).log(Level.SEVERE, null, ex);
                }
                

        return resultadoTemp[HebraActual];
    }

    public static void main(String[] args) throws Exception{
        //Variables
   
        
        try{
            //Socket (server)FRONT-CLIENTE en el puerto 5000
            ServerSocket acceptSocket = new ServerSocket(5000);
            //Socket (server)INDEX-FRONT en el puerto 3500
            Socket clientSocket2 = new Socket("localhost",3500);
            
            System.out.println("Front service is running...\n");

            //Socket listo para recibir 
            Socket connectionSocket1 = acceptSocket.accept();

            //Buffer para recibir desde el cliente
            BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket1.getInputStream()));
            //Buffer para recibir del cache
            inFromIndex = new BufferedReader(new InputStreamReader(clientSocket2.getInputStream()));
            //Buffer para enviar al cliente
            DataOutputStream outToClient = new DataOutputStream(connectionSocket1.getOutputStream());
            //Buffer para consultar al index
            outToIndex = new DataOutputStream(clientSocket2.getOutputStream());
            
            //Abrir archivo de configuracion
            File archivo = new File ("config.ini");
            FileReader fr = new FileReader (archivo);
            BufferedReader br = new BufferedReader(fr);
            String linea = br.readLine();
            String[] parametros=linea.split(" ");
            FrontService buscador=new FrontService(
                    Integer.parseInt(parametros[1]),
                    Integer.parseInt(parametros[2]));
            
            while(true){

                //Recibimos el dato del cliente y lo mostramos en el server
                fromClient =inFromClient.readLine();
                Lock l=new ReentrantLock();
                int pid;
                l.lock();
                try {
                    pid=HebraActual;
                    HebraActual=(HebraActual+1)%nHebras;
                } finally {
                    l.unlock();
                }

                t[pid]=new Thread(){
                    @Override
                    public void run(){
                        System.out.println("Recibe de Cliente: " + fromClient);
                        Query consulta=new Query(fromClient);
                        Result respuesta = null;
                        try {
                            respuesta = buscador.AnswerQuery(consulta);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(FrontService.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        if(respuesta!=null){
                            try {
                                //Se le envia al cliente el largo del top
                                outToClient.writeBytes(respuesta.top.size()+"\n");
                                for(int i=0;i<respuesta.top.size();i++){
                                    System.out.println("Resultado: ");
                                    System.out.println("   - Titulo: "+respuesta.top.get(i).titulo);
                                    outToClient.writeBytes(respuesta.top.get(i).titulo+"\n");
                                    System.out.println("   - Url: "+respuesta.top.get(i).url);
                                    outToClient.writeBytes(respuesta.top.get(i).url+"\n");
                                    System.out.println("   - Usuario: "+respuesta.top.get(i).usuario);
                                    outToClient.writeBytes(respuesta.top.get(i).usuario+"\n");
                                }
                            } catch (IOException ex) {
                                Logger.getLogger(FrontService.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }else{
                            try {
                                System.out.println("ALERTA: No existe resultado para la consulta");
                                //Se le envia al cliente un 0
                                outToClient.writeBytes("0\n");
                            } catch (IOException ex) {
                                Logger.getLogger(FrontService.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    }
                };
                t[pid].start();

            }
        }catch(IOException ex){
            System.out.println("\n---------------------------\nSe cerro la conexión con el cliente\n-----------------------------\n");
        }
    }
    
}
