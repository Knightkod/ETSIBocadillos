package com.sanzfdu.cafeteriaetsib.bl;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sanzfdu.cafeteriaetsib.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MirenPablo on 10/07/2015.
 */

//Clase para adaptar el texto a color negro
public class TextAdapter extends BaseAdapter{

    private LayoutInflater inflater;//Do Layouts from XML

    //Necesarias variables para cada uno de los objetos de la layout de fila (row_rating)
    TextView txt;
    //Para mostrar texto en los metodos que usan esta funcion
    Context context;
    //Necesaria(s) variable(s) para lo que se nos pasa de arriba
    List<String> text = new ArrayList<String>();

    public TextAdapter(Context context, List<String> text){
        this.context = context;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.text = text;
    }

    public View getView(int pos,View convertView, ViewGroup parent){
        View row =convertView;
        if(convertView==null) {
            row = inflater.inflate(R.layout.row_text_black, parent, false);
        }

        txt = (TextView)row.findViewById(R.id.text);

        //Podriamos hacer todos los cambios personalizados al texto que quisieramos, annadiendo llamadas y tal aqui abajo
        txt.setText(text.get(pos));

        txt.setTextColor(context.getResources().getColor(R.color.textColor));//Para poner el texto de otro color
        txt.setTextSize(context.getResources().getInteger(R.integer.text_size));//Para cambiar el tamano de la letra
        return row;
    }

    public int getCount(){
        return text.size();
    }

    public Object getItem(int pos){
        return null;
    }
    public long getItemId(int pos){
        return pos;
    }
}
