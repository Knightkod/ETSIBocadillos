package com.sanzfdu.cafeteriaetsib.dl;

import java.util.List;

/**
 * Created by MirenPablo on 19/06/2015.
 */
public class Ingrediente {

    /**
     * The persistent class for the Ingredientes database table.
     *
     */
    private static final long serialVersionUID = 1L;

    private String nombre;

    //bi-directional many-to-many association to Bocata, borrado porque no tenemos anotaciones

    private List<Bocata> bocatas;

    public Ingrediente() {
    }

    public Ingrediente(String nombre, List <Bocata> bocatas) {
        this.nombre = nombre;
        this.bocatas = bocatas;
    }

    public String getNombre() {
        return this.nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<Bocata> getBocatas() {
        return this.bocatas;
    }

    public void setBocatas(List<Bocata> bocatas) {
        this.bocatas = bocatas;
    }

}
