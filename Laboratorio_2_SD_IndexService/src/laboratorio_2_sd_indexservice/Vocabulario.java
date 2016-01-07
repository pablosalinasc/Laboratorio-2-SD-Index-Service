/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package laboratorio_2_sd_indexservice;

import com.mongodb.BasicDBObject;

/**
 *
 * @author ñuño
 */
public class Vocabulario {
    String palabra;
    int idPalabra;
    int cantDocumentos;

    public Vocabulario(String palabra, int idPalabra, int cantDocumentos) {
        this.palabra = palabra;
        this.idPalabra = idPalabra;
        this.cantDocumentos = cantDocumentos;
    }
    
    public Vocabulario(BasicDBObject dBObject){
        this.idPalabra = dBObject.getInt("idPalabra");
        this.palabra = dBObject.getString("palabra");
        this.cantDocumentos = dBObject.getInt("cantDocumentos");
    }
    

    public BasicDBObject toDBObject() {

            // Creamos una instancia BasicDBObject
            BasicDBObject dBObject = new BasicDBObject();

            dBObject.append("idPalabra", this.idPalabra);
            dBObject.append("palabra", this.palabra);
            dBObject.append("cantDocumentos", this.cantDocumentos);

            return dBObject;
    }
    
    public void print(){
        System.out.println("=====Vocabulario======");
        System.out.println("idPalabra: "+idPalabra);
        System.out.println("Palabra: "+palabra);
        System.out.println("Cantidad de documentos: "+cantDocumentos);
    }
}
