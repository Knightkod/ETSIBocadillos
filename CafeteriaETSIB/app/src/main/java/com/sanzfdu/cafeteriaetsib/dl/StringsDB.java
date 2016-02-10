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

    String str = "Bacon,2.0,0.0,0.0,0:Bacon;Bacon_Queso,2.10,0.0,0.0,0:Bacon,Queso;Calamares,2.45,0.0,0.0,0:Calamares,Lechuga,Mayonesa;" +
            "Caida,3.60,3.0,0.0,0:Bacon,Queso,Pechuga empanada,Huevo;Soto del real, 4.30,0.0,0.0,0:Lechuga,Tomate,Huevo,Queso,Carne rara;" +
            "Submarino,3.60,0.0,0.0,0:Bacon,Queso,Huevo,Lomo;Haiti,3.5,2.0,0.0,0:Bacon,Pechuga de pollo,Queso,Huevo;Varadero,3.10,3.0,0.0,0:Lomo,Queso,Pimiento,Cebolla;" +
            "Vikingo,3.60,1.0,0.0,0:Ternera,Jamon_York,Queso,Mayonesa;Cubanito Bacon Vegetal,4.25,2.0,0.0,0:Lomo,Queso,Pimiento,Huevo,Cebolla,Bacon,Lechuga,Tomate;" +
            "Cubanito Bacon,3.90,2.0,0.0,0:Lomo,Queso,Pimiento,Huevo,Cebolla,Bacon;Cubanito,3.60,2.0,0.0,0:Lomo,Queso,Pimiento,Huevo,Cebolla;" +
            "Martutene,4.3,2.0,0.0,0:Pimiento verde,Lomo,Jamon Serrano,Queso,Atún;Triste,1.80,1.0,0.0,0:Queso,Lechuga,Tomate,Mayonesa,Cebolla;" +
            "Sujetador,3.4,2.0,0.0,0:Huevos(2),Queso,Lomo;Aperribai,4.0,4.0,0.0,0:Salchicha,Bacon,Lomo,Queso;" +
            "Julius,3.25,2.0,0.0,0:Lechuga,Tomate,Pechuga empanada,Salsa cesar,Queso;Titanic,5.10,4.0,0.0,0:Lomo(extra),Queso,Huevo,Bacon extra,Pechuga de pollo,Mayonesa,Lechuga,Tomate;" +
            "Menu Feliz,4.5,5.0,0.0,0:Hamburguesa,Lechuga,Tomate,Cebolla,Mayonesa,Huevo,Bacon,Coca cola,Patatas fritas;" +
            "Hamburguesa,2.70,3.5,0,0:Hamburguesa,Lechuga,Tomate,Cebolla,Mayonesa;Hamburguesa Huevo,3.15,3.0,0,0:Hamburguesa,Lechuga,Tomate,Cebolla,Mayonesa,Huevo;" +
            "Hamburguesa Bacon,3.15,3.0,0,0:Hamburguesa,Lechuga,Tomate,Cebolla,Mayonesa,Bacon;Hamburguesa Completa,3.55,3.0,0,0:Hamburguesa,Lechuga,Tomate,Cebolla,Mayonesa,Huevo,Bacon;" +
            "Hamburguesa Doble de Carne,3.75,2.5,0,0:Hamburguesa(2),Lechuga,Tomate,Cebolla,Mayonesa;Hamburguesa Completa Doble de Carne,2.70,2.5,0,0:Hamburguesa(2),Lechuga,Tomate,Cebolla,Mayonesa,Bacon,Huevo;"+
            "Imperium,3.75,5.0,0.0,0:Lechuga,Tomate,Pechuga empanada,Salsa cesar,Pimiento,Bacon,Queso;" +
            "Ave cesar,2.75,4.0,0.0,0:Pechuga empanada,Salsa cesar,Lechuga,Tomate;Pumuky,2.85,4.0,0.0,0:Lechuga,Tomate,Huevo,Bacon,Queso;" +
            "VDP vegetal de pollo,2.45,4.0,0.0,0:Lechuga,Tomate,Pollo,Mayonesa;Jamaika Vegetal,3.60,0,0,0:Bacon,Queso,Huevo,Pollo,Lechuga,Tomate;" +
            "Jamaika,3.30,0,0,0:Bacon,Queso,Huevo,Pollo;Chorizo Frito con Pimientos Verdes,2.90,0,0,0:Chorizo,Pimientos verdes;" +
            "Angus,3.65,0,0,0:Pechuga empanada,Lechuga,Tomate,Queso,Bacon;";
    List<String> baggs =new ArrayList<String>();
    List<String> ingrs =new ArrayList<String>();
    List <String> baggHasIngr  =new ArrayList<String>();

    public StringsDB(){
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


