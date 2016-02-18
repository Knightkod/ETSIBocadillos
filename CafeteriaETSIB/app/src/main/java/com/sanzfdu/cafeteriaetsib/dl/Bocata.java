package com.sanzfdu.cafeteriaetsib.dl;

import java.util.Comparator;
import java.util.List;

/**
 * Created by MirenPablo on 19/06/2015.
 */
public class Bocata implements Comparable<Bocata> {

    /**
     * The persistent class for the Bocatas database table.
     *
     */
        private static final long serialVersionUID = 1L;


        private String nombre;

        private float precio;

        private float rate;

        private float fav;

        private int antiguedad;



        //bi-directional many-to-many association to Ingrediente

        private List<Ingrediente> ingredientes;


        public Bocata() {
        }

        public Bocata(String nombre,float precio,float rate, float fav,int antiguedad, List<Ingrediente> ingredientes) {
            this.nombre = nombre;
            this.precio = precio;
            this.rate = rate;
            this.fav = fav;
            this.antiguedad = antiguedad;
            this.ingredientes = ingredientes;
        }

    public String getNombre() {
        return this.nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public float getPrecio() {
        return this.precio;
    }

    public void setPrecio(float precio) {
        this.precio = precio;
    }

    public float getRate() {
        return this.rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }

    public float getFav() {
        return this.fav;
    }

    public void setFav(float fav) {
        this.fav = fav;
    }

    public int getAntiguedad() {
        return this.antiguedad;
    }

    public void setAntiguedad(int antiguedad) {
        this.antiguedad = antiguedad;
    }



    public List<Ingrediente> getIngredientes() {
        return this.ingredientes;
    }

    public void setIngredientes(List<Ingrediente> ingredientes) {
        this.ingredientes = ingredientes;
    }
    /*Overridamos compareTo del tipo que queramos, luego annadiremos las busquedas en base
a cada parametro. Tenemos que hacerlo asi para que luego al usar el sort en las capas superiores
sepa filtrar en base a eso*/
    @Override
    public int compareTo(Bocata boc){

        return Comparators.RATE.compare(this,boc);
    }

    //CLASE EMBEBIDA
    public static class Comparators{


        public static Comparator<Bocata> RATE= new Comparator<Bocata>() {
            @Override
            public int compare(Bocata lhs, Bocata rhs) {
                if(lhs.getRate() < rhs.getRate()){
                    return 1;
                }
                if(lhs.getRate() > rhs.getRate())
                {
                    return -1;
                }
                return 0;
            }
        };
        public static Comparator<Bocata> OLD = new Comparator<Bocata>() {
            @Override
            public int compare(Bocata lhs, Bocata rhs) {
                if(lhs.getAntiguedad() < rhs.getAntiguedad()){
                    return 1;
                }
                if(lhs.getAntiguedad() > rhs.getAntiguedad())
                {
                    return -1;
                }
                return 0;
            }
        };
        public static Comparator<Bocata> FAV = new Comparator<Bocata>() {
            @Override
            public int compare(Bocata lhs, Bocata rhs) {
                if(lhs.getFav() < rhs.getFav()){
                    return 1;
                }
                if(lhs.getFav() > rhs.getFav()) {
                    return -1;
                }
                return 0;
            }
        };


    }


    }
