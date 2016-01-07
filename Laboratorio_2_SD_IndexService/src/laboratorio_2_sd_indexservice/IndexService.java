package laboratorio_2_sd_indexservice;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 *
 * @author psalinasc
 */
public class IndexService {
    
        
    public static Result getResult(String query) {
        //Crea db y tablas
        MongoClient mongoClient = new MongoClient( "localhost" , 27017 );
        DB db = mongoClient.getDB( "indexDB" );

        DBCollection colDocumentos = db.getCollection("Documentos");
        DBCollection colIndiceInvertido = db.getCollection("IndiceInvertido");
        DBCollection colVocabulario = db.getCollection("Vocabulario");
        
        ArrayList<Frecuencia> frecDocs=new ArrayList<>();
        String[] palabras=query.split(" ");
        for(int i=0;i<palabras.length;i++){
            //se consigue el id de cada palabra
            DBCursor cursor=colVocabulario.find((DBObject) new BasicDBObject("palabra", palabras[i]));
            if(cursor.hasNext()){
                Vocabulario vocTemp= new Vocabulario((BasicDBObject) cursor.next());
                cursor=colIndiceInvertido.find((DBObject) new BasicDBObject("idPalabra",vocTemp.idPalabra));
                IndiceInvertido indiceTemp = new IndiceInvertido((BasicDBObject) cursor.next());
                Collections.sort(indiceTemp.frecuencias,Collections.reverseOrder());
                //Suma las frecuencias de cada palabra en cada documento
                for(int j=0;j<indiceTemp.frecuencias.size();j++){
                    //agregar a frecDocs en caso que tenga mayor frecuencia y actualiza las frecuencia que habian
                    if(frecDocs.size()>0){
                        int insertado=0;
                        for(int k=0;k<frecDocs.size();k++){
                            if(frecDocs.get(k).idDocumento==indiceTemp.frecuencias.get(j).idDocumento){
                                frecDocs.get(k).frecuencia+=indiceTemp.frecuencias.get(j).frecuencia;
                                insertado=1;
                                break;
                            }
                        }
                        if(insertado==0){
                            frecDocs.add(indiceTemp.frecuencias.get(j));
                            insertado=1;
                        }
                    }else{
                        //agrega el primer elemento
                        frecDocs.add(indiceTemp.frecuencias.get(j));
                    }
                }
            }
        }
        Result respuesta;
        if(frecDocs.size()>0){
            //calcula los top 10 (o menos)
            Collections.sort(frecDocs,Collections.reverseOrder());
            ArrayList<Documento> listaDocs= new ArrayList<>();
            if(frecDocs.size()>=10){
                for(int i=0;i<10;i++){
                    DBCursor cursor=colDocumentos.find((DBObject) new BasicDBObject("idDoc", frecDocs.get(i).idDocumento));
                    listaDocs.add(new Documento((BasicDBObject)cursor.next()));
                    System.out.println(" - Titulo: "+listaDocs.get(i).titulo);
                    System.out.println(" - URL: "+listaDocs.get(i).url);
                    System.out.println(" - Usuario: "+listaDocs.get(i).usuario);
                }
            }else if(frecDocs.size()>0){
                for(int i=0;i<frecDocs.size();i++){
                    DBCursor cursor=colDocumentos.find((DBObject) new BasicDBObject("idDoc", frecDocs.get(i).idDocumento));
                    listaDocs.add(new Documento((BasicDBObject)cursor.next()));
                    System.out.println(" - Titulo: "+listaDocs.get(i).titulo);
                    System.out.println(" - URL: "+listaDocs.get(i).url);
                    System.out.println(" - Usuario: "+listaDocs.get(i).usuario);
                }
            }
            respuesta = new Result(listaDocs);
        }else{
            respuesta= null;
        }
        return respuesta;
    }
    
    public static void main(String[] args) throws Exception{
        //Variables
        String fromFront="";
        
        try{
            //Socket (server)INDEX-FRONT en el puerto 3500
            ServerSocket acceptSocket1 = new ServerSocket(3500);

            System.out.println("Index service is running...\n");

            //Socket listo para recibir 
            Socket connectionSocket1 = acceptSocket1.accept();

            //Buffer para recibir desde el FrontService
            BufferedReader inFromFront = new BufferedReader(new InputStreamReader(connectionSocket1.getInputStream()));
            //Buffer para enviar al FrontService
            DataOutputStream outToFront = new DataOutputStream(connectionSocket1.getOutputStream());
            IndexService indice=new IndexService();
            
            while(true){
                fromFront =inFromFront.readLine();
                System.out.println("Received: " + fromFront);

                String[] tokens = fromFront.split(" ");
                String parametrosREST = tokens[1];

                String http_method = tokens[0];

                String[] tokens_parametros = parametrosREST.split("/");

                String terminos = tokens_parametros.length > 2 ? tokens_parametros[2] : "";
                
                if(http_method.equals("GET")){
                    String terminosNorm=terminos.replace("+", " ");
                    Query query= new Query(terminosNorm);
                    Result resultado=indice.getResult(query.terminos);
                    //envía el resultado al FrontService
                    if(resultado==null){
                        outToFront.writeBytes("0\n");
                        System.out.println("Envia a Front: '0'");
                    }else{
                        if(resultado.top.size()>=10){
                            outToFront.writeBytes("10\n");
                            for(int i=0;i<10;i++){
                                outToFront.writeBytes(resultado.top.get(i).idDoc+"\n");
                                outToFront.writeBytes(resultado.top.get(i).titulo+"\n");
                                outToFront.writeBytes(resultado.top.get(i).usuario+"\n");
                                System.out.println("Envia a Front: '1' '"+resultado.top.get(i).idDoc+"' '"+resultado.top.get(i).titulo+"' '"+resultado.top.get(i).usuario+"'");
                            }
                        }else{
                            outToFront.writeBytes(resultado.top.size()+"\n");
                            for(int i=0;i<resultado.top.size();i++){
                                outToFront.writeBytes(resultado.top.get(i).idDoc+"\n");
                                outToFront.writeBytes(resultado.top.get(i).titulo+"\n");
                                outToFront.writeBytes(resultado.top.get(i).usuario+"\n");
                                System.out.println("Envia a Front: '1' '"+resultado.top.get(i).idDoc+"' '"+resultado.top.get(i).titulo+"' '"+resultado.top.get(i).usuario+"'");
                            }
                        }
                    }
                }
            }
        }catch(IOException ex){
            System.out.println("\n---------------------------\nSe cerro la conexión con el Front\n-----------------------------\n");
        }
    }
    
}
