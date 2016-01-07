/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package laboratorio_2_sd_indexservice;

import java.util.ArrayList;

/**
 *
 * @author ñuño
 */
public class Result {
    ArrayList<Documento> top;

    public Result(ArrayList<Documento> top) {
        this.top=new ArrayList();
        this.top=top;
    }

    public Result() {
    }
    
    
    public void insertarResultado(Documento doc){
        top.add(doc);
    }
    
}
