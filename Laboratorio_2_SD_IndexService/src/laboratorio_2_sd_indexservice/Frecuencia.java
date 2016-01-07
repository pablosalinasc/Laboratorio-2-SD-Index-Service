package laboratorio_2_sd_indexservice;

import com.mongodb.BasicDBObject;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author ñuño
 */
public class Frecuencia implements Comparable<Frecuencia> {
    int frecuencia;
    int idDocumento;

    public Frecuencia(int frecuencia, int idDocumento) {
        this.frecuencia = frecuencia;
        this.idDocumento = idDocumento;
    }

    public Frecuencia(BasicDBObject f) {
        this.idDocumento = f.getInt("idDocumento");
        this.frecuencia= f.getInt("frecuencia");
    }
    
    
    public BasicDBObject toDBObject(){
        BasicDBObject dBObject = new BasicDBObject();
        dBObject.append("idDocumento", this.idDocumento);
        dBObject.append("frecuencia", this.frecuencia);
        
        return dBObject;
    }
    
    public void print(){
        System.out.println("- frecuencia: "+frecuencia+" idDocumento: "+idDocumento);
    }

    @Override
    public int compareTo(Frecuencia o) {
        return Integer.compare(this.frecuencia,o.frecuencia);
    }
}

    