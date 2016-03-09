package com.sanzfdu.cafeteriaetsib.bl;

import android.content.Context;
import android.widget.Toast;

import com.sanzfdu.cafeteriaetsib.dl.Bocata;

import java.util.List;

/**
 * Created by MirenPablo on 04/03/2016.
 */
public class Pedido {

    private static float costeTot=0;
    private static String names="";//string con los nombres de los pedidos

    public Pedido(){

    }

    public static boolean realizaPedido(DataListAdapter dListAdapter,Context context){
        names="";
        costeTot=0;
        boolean bool = false;

        List<Bocata> pedido;
        pedido=dListAdapter.getPedido();

        for(int i = 0; i < pedido.size();i++){
            if(i==0)
                names=pedido.get(i).getNombre();
            else
                names=names+","+pedido.get(i).getNombre();
            costeTot=costeTot+pedido.get(i).getPrecio();
        }
        if(pedido.size()==0)
            Toast.makeText(context, "Debe seleccionar al menos un bocadillo para poder comprar", Toast.LENGTH_SHORT).show();
        else {
            //Falta poner la funcion de comprobacion de internet y de compra, que va a ir en otra clase
            /*Toast.makeText(context, "Pedido enviado. El pedido consta de los bocadillos " + names +
                    " y el precio es de " + costeTot + " euros.", Toast.LENGTH_SHORT).show();*/
        bool=true;
        }
        return bool;
    }

    public static float getCosteTot() {
        return costeTot;
    }

    public static void setCosteTot(float costeTot) {
        Pedido.costeTot = costeTot;
    }

    public static String getNames() {
        return names;
    }

    public static void setNames(String names) {
        Pedido.names = names;
    }
}
