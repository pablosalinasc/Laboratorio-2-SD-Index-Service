/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package laboratorio_2_sd_indexservice;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import java.util.ArrayList;

/**
 *
 * @author ñuño
 */
public class Documento {
    int idDoc;
    String url;
    int cantPalabras;
    ArrayList<String> palabras;
    String titulo;
    String usuario;
    
    public Documento(int idDoc, ArrayList<String> palabras,String titulo,String usuario) {
        this.palabras=palabras;
        this.cantPalabras=palabras.size();
        this.idDoc=idDoc;
        this.titulo=titulo;
        this.usuario=usuario;
        this.url="https://es.wikipedia.org/wiki/"+titulo;
    }
    
    public Documento(int idDoc,String titulo,String usuario) {
        this.idDoc=idDoc;
        this.titulo=titulo;
        this.usuario=usuario;
        this.url="https://es.wikipedia.org/wiki/"+titulo;
    }
    
    public void print(){
        System.out.println("----------------------------");
        System.out.println("ID documento: "+idDoc);
        System.out.println("Titulo: "+titulo);
        System.out.println("URL: "+url);
        System.out.println("Usuario:"+usuario);
        System.out.println("Cantidad palabras: "+cantPalabras);
        System.out.println("Palabras: \n");
        int contador=0;
        for(int i=0;i<cantPalabras;i++){
            if(contador<9){
                contador++;
                System.out.print(palabras.get(i)+" ");
            }else if(contador==9){
                contador=0;
                System.out.print(palabras.get(i)+"\n");
            }
        }
        System.out.println("\n\n----------------------------\n");
    }
    
    public Documento(BasicDBObject dBObject) {
        this.idDoc = dBObject.getInt("idDoc");
        this.titulo = dBObject.getString("titulo");
        this.usuario = dBObject.getString("usuario");
        this.url = dBObject.getString("url");
        this.cantPalabras = dBObject.getInt("cantPalabras");
    }

    public BasicDBObject toDBObject() {

            // Creamos una instancia BasicDBObject
            BasicDBObject dBObject = new BasicDBObject();

            dBObject.append("idDoc", this.idDoc);
            dBObject.append("titulo", this.titulo);
            dBObject.append("url", this.url);
            dBObject.append("cantPalabras", this.cantPalabras);
            dBObject.append("usuario", this.usuario);

            return dBObject;
    }
}
