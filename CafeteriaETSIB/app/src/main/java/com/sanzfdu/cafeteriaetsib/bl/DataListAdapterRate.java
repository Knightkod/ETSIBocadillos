package com.sanzfdu.cafeteriaetsib.bl;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.sanzfdu.cafeteriaetsib.R;
import com.sanzfdu.cafeteriaetsib.dl.Bocata;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MirenPablo on 23/09/2015.
 */
public class DataListAdapterRate extends BaseAdapter{

        private LayoutInflater inflater;//Do Layouts from XML

        //Necesarias variables para cada uno de los objetos de la layout de fila (row_rating)
        TextView txt;
        RatingBar rate;
        //Para mostrar texto en los metodos que usan esta funcion
        Context context;
        //Necesaria(s) variable(s) para lo que se nos pasa de arriba
        List<Bocata> lbagg = new ArrayList<Bocata>();

        List<Bocata> pedido=new ArrayList<Bocata>();


        public DataListAdapterRate(Context context,List<Bocata> lbagg){
        this.context = context;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.lbagg = lbagg;
    }
        //TRAMPA, he puesto STRING y no hace falta OJO DE CAMBIARLO LUEGO!!!
    /*public DataListAdapter(Context context,List<String> lStr){
        this.context = context;

        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.lStr = lStr;
    }*/




    public View getView(final int pos,View convertView, ViewGroup parent){
        View row =convertView;
        if(convertView==null) {
            row = inflater.inflate(R.layout.row_rating, parent, false);
        }
        txt = (TextView)row.findViewById(R.id.list_elem);
        rate = (RatingBar)row.findViewById(R.id.fav);
        txt.setText(createText(lbagg.get(pos)));


        //Esta parte es para capturar el click sobre el boton de annadir productos
        ImageButton button =(ImageButton)row.findViewById( R.id.imgButton);
        button.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        Toast.makeText(context, "Añadido al carrito 1 bocadillo " + lbagg.get(pos).getNombre() + ".", Toast.LENGTH_SHORT).show();
                        pedido.add(lbagg.get(pos));
                    }
                });


        //Para poner el texto de otro color
        txt.setTextColor(context.getResources().getColor(R.color.textColor));
        txt.setTextSize(context.getResources().getInteger(R.integer.text_size));//Para cambiar el tamano de la letra
        //Con esto conseguimos printar el rateo en estrellas en funcion a la base de datos
        rate.setRating(lbagg.get(pos).getRate());
        //y con esto lo cambiamos de color
        LayerDrawable stars = (LayerDrawable) rate.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(Color.BLUE, PorterDuff.Mode.SRC_ATOP);

        return row;
    }

    /*Devuelve el tamanno de la lista que se va a mostrar, es el que te dice
    cuanto sale en pantalla, cuantas filas debe printar*/
    public int getCount(){

        return lbagg.size();//Numero de elementos en la lista
    }

    public Object getItem(int pos){
        return null;
    }
    public long getItemId(int pos){
        return pos;
    }


    public String createText(Bocata bagg){
        String s;
        s = "Bocata: " + bagg.getNombre()+"\n";
        s = s.concat("Precio:"+bagg.getPrecio()+"€\n");

        if(bagg.getIngredientes()!=null) {

            s = s.concat("\nIngredientes:");

            for (int i = 0; i < bagg.getIngredientes().size(); i++) {
                s = s.concat(bagg.getIngredientes().get(i).getNombre() + "");
                if (i < bagg.getIngredientes().size() - 1) {
                    s = s.concat(", ");
                }
            }
            s = s.concat(".\n");
        }
        return s;
    }

    public List<Bocata> getPedido(){
        return pedido;
    }

}
