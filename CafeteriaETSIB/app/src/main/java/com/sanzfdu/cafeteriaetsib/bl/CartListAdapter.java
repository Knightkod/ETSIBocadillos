package com.sanzfdu.cafeteriaetsib.bl;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.sanzfdu.cafeteriaetsib.R;
import com.sanzfdu.cafeteriaetsib.dl.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MirenPablo on 05/07/2015.
 */
public class CartListAdapter extends BaseAdapter {

    private LayoutInflater inflater;//Do Layouts from XML

    //Necesarias variables para cada uno de los objetos de la layout de fila
    TextView txt;
    Context context;
    //Necesaria(s) variable(s) para lo que se nos pasa de arriba



   public CartListAdapter(Context context){
       this.context = context;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }


    public View getView(final int pos,View convertView, ViewGroup parent){
        View row =convertView;
        if(convertView==null) {
            row = inflater.inflate(R.layout.row_cart, parent, false);
        }
        txt = (TextView)row.findViewById(R.id.list_elem);
        txt.setText(Constants.pedido.get(pos).getNombre());

        //Esta parte es para capturar el click sobre el boton de eliminar productos
        ImageButton button =(ImageButton)row.findViewById( R.id.imgButton);
        button.setOnClickListener(
                new View.OnClickListener() {public void onClick(View v) {
                    Constants.pedido.remove(pos);
                    //Con el notifyDataSetChanged hacemos que se actualice la lista
                    // automaticamente al pulsar el boton de eliminar!
                    //Asi evitamos incongruencias y demas
                    notifyDataSetChanged();
                }
                });
    //Para poner el texto de otro color
        txt.setTextColor(context.getResources().getColor(R.color.textColor));
        txt.setTextSize(context.getResources().getInteger(R.integer.text_size_big));//Para cambiar el tamano de la letra

        return row;
    }

    /*Devuelve el tamanno de la lista que se va a mostrar, es el que te dice
    cuanto sale en pantalla, cuantas filas debe printar*/
    public int getCount(){

        return Constants.pedido.size();//Numero de elementos en la lista
    }

    public Object getItem(int pos){
        return null;
    }
    public long getItemId(int pos){
        return pos;
    }

}
