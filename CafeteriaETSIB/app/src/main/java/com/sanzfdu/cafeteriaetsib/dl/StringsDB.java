package com.sanzfdu.cafeteriaetsib.dl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Created by MirenPablo on 06/07/2015.
 */


public class StringsDB {
    //Structure = "Bagg_name,price,rate,fav,old:ingr1,ingr2;
    // Bagg_name,price,rate,fav,old:ingr1,ingr2,ingr3;...}

    private String str =null;
    List<String> baggs =new ArrayList<String>();
    List<String> ingrs =new ArrayList<String>();
    List <String> baggHasIngr  =new ArrayList<String>();

    public StringsDB(String s){
        str = s;
        fillLists();
    }

    public List<String> getBaggList(){
        return baggs;
    }

    public List<String> getIngrList(){
        return ingrs;
    }


    public List<String> getBaggHasIngr(){
        return baggHasIngr;
    }
    private void fillLists(){
        String [ ] baggComp= str.split(";");
        String [ ] baggNoIngr;
        String []  baggFrag;
        String [ ] ingrFrag;
        int count = 0;
        for(int i =0; i<baggComp.length;i++){
            baggNoIngr= baggComp[i].split(":");
            baggFrag = baggNoIngr[0].split(",");//Siempre cojo la 1a parte de lo cortado arriba
            for(int j =0; j<baggFrag.length;j++){//Guarda el bocata separando en cada posicion un elemento
                    baggs.add(baggFrag [j]);    //Guarda primero nombre, luego precio... y asi, el de arriba que los coja en ese orden
            }
            //System.out.println("Lo que estoy metiendo en bocatas es"+baggs+"\n");

            ingrFrag=baggNoIngr[1].split(",");
            for(int j =0; j<ingrFrag.length;j++){
                if(!ingrs.contains(ingrFrag[j])){//solo si no estan ya annadidos
                    ingrs.add(ingrFrag[j]);//Guarda ingrediente a ingrediente
                }
            }
            //System.out.println("Lo que estoy metiendo en ingredientes es"+ingrs+"\n");
            for(int j =0; j<ingrFrag.length;j++){
                baggHasIngr.add(baggFrag[0]);
                baggHasIngr.add(ingrFrag[j]);
            }
           // System.out.println("Lo que estoy metiendo en hash es"+baggHasIngr+"\n");

        }

    }
}


