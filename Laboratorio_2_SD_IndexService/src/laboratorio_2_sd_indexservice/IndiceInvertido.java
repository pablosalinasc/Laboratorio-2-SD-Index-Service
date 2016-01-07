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
public class IndiceInvertido {
    int idPalabra;
    ArrayList<Frecuencia> frecuencias; //(idDoc,frecuencia)

    public IndiceInvertido(int idPalabra, int frecuencia, int idDocumento) {
        this.frecuencias= new ArrayList<>();
        Frecuencia frec= new Frecuencia(frecuencia,idDocumento);
        this.frecuencias.add(frec);
        this.idPalabra=idPalabra;
        
    }
    
    public void ActualizarFrecuencias( int frecuencia, int idDocumento){
        Frecuencia fTemp=new Frecuencia(frecuencia, idDocumento);
        int contador=0;
        int indice=0;
        for(int i=0;i<frecuencias.size();i++){
            if(frecuencias.get(i).idDocumento==idDocumento){
                contador=1;
                indice=i;
                break;
            }
        }
        if(contador==1){
            frecuencias.get(indice).frecuencia++;
        }else{
            frecuencias.add(fTemp);
        }
    }

    public IndiceInvertido(BasicDBObject DBObject) {
        this.idPalabra = DBObject.getInt("idPalabra");
        BasicDBList frecuenciasDB = (BasicDBList) DBObject.get("frecuencias");
        this.frecuencias = new ArrayList<>();
        for (Object f : frecuenciasDB) {
            Frecuencia fTemp=new Frecuencia((BasicDBObject) f);
            this.frecuencias.add(fTemp);
        }
    }
    
    public void print(){
        System.out.println("=====IndiceInvertido======");
        System.out.println("idPalabra: "+idPalabra);
        for(int i=0;i<frecuencias.size();i++){
            frecuencias.get(i).print();
        }
    }
    
    public BasicDBObject toDBObject(){
        BasicDBObject dBObject = new BasicDBObject();
        dBObject.append("idPalabra", this.idPalabra);
        BasicDBList frecuenciasDB = new BasicDBList();
        for(int i=0;i<frecuencias.size();i++){
            frecuenciasDB.add(frecuencias.get(i).toDBObject());
        }
        dBObject.append("frecuencias", frecuenciasDB);
        
        return dBObject;
    }
    
}
