package com.sanzfdu.cafeteriaetsib.bl;


import com.sanzfdu.cafeteriaetsib.dl.Bocata;
import com.sanzfdu.cafeteriaetsib.dl.Ingrediente;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pablo on 10/03/16.
 * Basado en el código de aritzbi en el proyecto "SeriesAPP" colgado en GITHUB
 */
//CLASE PARA RECOJER EL CODIGO EN BRUTO DEL JSON
public class JSONParser {

    public JSONParser(){

    }


    public static int parseVersion(JSONArray versiones) {
            int vers = 0;
            try {
                JSONObject jsonVers = versiones.getJSONObject(0);
                vers = jsonVers.getInt("version");//Accedemos al campo version del objeto json que nos pasa
            }
            catch ( JSONException e )
            {
                System.out.println("Error en el parseo de la version\n");
            }
        return vers;
    }

    public static List<Bocata> parseDatabase(JSONArray bocatas ){
        List<Bocata> lBagg = new ArrayList<Bocata>();
        List<Ingrediente>lIngr= new ArrayList<Ingrediente>();
        Bocata boc = new Bocata();
        String[]arrayIngr;
        for(int i=0;i<bocatas.length();i++) {
            try {
                JSONObject jsonBoc = bocatas.getJSONObject(i); //Bagg_name,price,rate,fav,old:ingr1,ingr2,ingr3;
                boc.setNombre(jsonBoc.getString("nombre"));
                boc.setPrecio(Float.parseFloat(Double.toString(jsonBoc.getDouble("precio"))));
                boc.setRate(Float.parseFloat(Double.toString(jsonBoc.getDouble("rate"))));
                boc.setFav(Float.parseFloat(Double.toString(jsonBoc.getDouble("fav"))));
                boc.setAntiguedad(jsonBoc.getInt("antiguedad"));
                arrayIngr=jsonBoc.getString("ingredientes").split(",");
                for(int j= 0;j<arrayIngr.length;j++) {
                    lIngr.add(new Ingrediente(arrayIngr[j], null));
                }
                boc.setIngredientes(lIngr);
                lBagg.add(boc);
                boc=new Bocata();
                lIngr = new ArrayList<Ingrediente>();
                //Con el lIngr.clear() lo que hace es borrar la lista pero nada mas. Como no cambia
                // la referencia de la lista, al reescribirla con cada bocadillo nuevo se acaba con los bocadillos
                //con los mismos ingredientes. Hay que hacer un new ArrayList para que sean listas
                //independientes

            } catch (JSONException e) {
                System.out.println("Error en el parseo de los bocatas\n");
            }
        }

        return lBagg;
    }
}