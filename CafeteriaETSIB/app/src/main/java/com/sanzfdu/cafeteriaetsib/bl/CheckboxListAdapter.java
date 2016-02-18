package com.sanzfdu.cafeteriaetsib.bl;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import com.sanzfdu.cafeteriaetsib.R;
import com.sanzfdu.cafeteriaetsib.dl.Bocata;
import com.sanzfdu.cafeteriaetsib.dl.Ingrediente;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MirenPablo on 25/09/2015.
 */
public class CheckboxListAdapter extends BaseAdapter {


    private LayoutInflater inflater;//Do Layouts from XML

    //Necesarias variables para cada uno de los objetos de la layout de fila (row_rating)
    TextView txt;
    //Para mostrar texto en los metodos que usan esta funcion
    Context context;
    //Necesaria(s) variable(s) para lo que se nos pasa de arriba
    List<String> lingr = new ArrayList<String>();




    public CheckboxListAdapter(Context context,List<String> lingr){
        this.context = context;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.lingr = lingr;
    }


    public View getView(int pos,View convertView, ViewGroup parent){
        View row =convertView;
        if(convertView==null) {
            row = inflater.inflate(R.layout.fragment_filter, parent, false);
        }
        txt = (TextView)row.findViewById(R.id.listado);

        //Para poner el texto de otro color
        txt.setTextColor(context.getResources().getColor(R.color.textColor));
        txt.setTextSize(context.getResources().getInteger(R.integer.text_size));//Para cambiar el tamano de la letra
        txt.setText(lingr.get(pos));
        return row;
    }

    /*Devuelve el tamanno de la lista que se va a mostrar, es el que te dice
    cuanto sale en pantalla, cuantas filas debe printar*/
    public int getCount(){
        return lingr.size();//Numero de elementos en la lista
    }

    public Object getItem(int pos){
        return null;
    }
    public long getItemId(int pos){
        return pos;
    }

}
