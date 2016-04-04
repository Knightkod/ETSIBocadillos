package com.sanzfdu.cafeteriaetsib.bl;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.sanzfdu.cafeteriaetsib.dl.Constants;
import com.sanzfdu.cafeteriaetsib.R;
import com.sanzfdu.cafeteriaetsib.dl.Bocata;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MirenPablo on 05/07/2015.
 */
public class DataListAdapter extends BaseAdapter {

    private LayoutInflater inflater;//Do Layouts from XML

    //Necesarias variables para cada uno de los objetos de la layout de fila (row_rating)
    TextView txt;
    RatingBar fav;
    //Para mostrar texto en los metodos que usan esta funcion
    Context context;
    //Necesaria(s) variable(s) para lo que se nos pasa de arriba
    List<Bocata> lbagg = new ArrayList<Bocata>();
    List<Bocata> pedido=new ArrayList<Bocata>();



   public DataListAdapter(Context context,List<Bocata> lbagg){
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

    public List<Bocata>getLbagg(){
        return lbagg;
    }


    public View getView(final int pos,View convertView, ViewGroup parent){
        View row =convertView;
        if(convertView==null) {
            row = inflater.inflate(R.layout.row_rating, parent, false);
        }
        txt = (TextView)row.findViewById(R.id.list_elem);
        fav = (RatingBar)row.findViewById(R.id.fav);
        txt.setText(createText(lbagg.get(pos)));

        //Esta parte es para capturar el click sobre el boton de annadir productos
        ImageButton button =(ImageButton)row.findViewById( R.id.imgButton);
        button.setOnClickListener(
                new View.OnClickListener() {public void onClick(View v) {
                        Toast.makeText( context, "Añadido al carrito 1 bocadillo "+ lbagg.get(pos).getNombre() +".",Toast.LENGTH_SHORT).show();
                        Constants.pedido.add(lbagg.get(pos));
                }
                });

    //Para poner el texto de otro color
        txt.setTextColor(context.getResources().getColor(R.color.textColor));
        txt.setTextSize(context.getResources().getInteger(R.integer.text_size));//Para cambiar el tamano de la letra
        //Con esto conseguimos printar los favoritos en funcion a la base de datos
        fav.setRating(lbagg.get(pos).getFav());
        LayerDrawable stars = (LayerDrawable) fav.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP);

        startRating(pos);

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

      private void startRating(final int pos){

        fav.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar rb, float rating, boolean userBool) {

                if(userBool) {
                    //QUITAR IPSO FACTO LA PARTE QUE MUESTRA LA POSICION, NO HACE FALTA
                    Toast.makeText(context, "Añadido a favoritos el elemento " + Integer.toString(pos) + " con una puntuacion de"
                            + Float.toString(rating) + ".\n", Toast.LENGTH_SHORT).show();
                    setBaggFav(pos, rating);
                }
            }

        });


    }
    public void setBaggFav(int pos,float fav){
        /*Annade en la propia lista el nuevo favorito (Esto lo annade a esta lista, pero a la
        de arriba??? NO se si seria necesario o no)*/
        //No hace falta porque SIEMPRE lee de la db cuando vuelvo a la layout al parecer
        //lbagg.get(pos).setFav(fav);
        //Lo guarda en la database
        MySQL cn = new MySQL(context,"bocatasUni.db",null, Constants.vers);
        SQLiteDatabase db = cn.getWritableDatabase();

        ContentValues val = new ContentValues();

        //Cogemos el objeto a meter
        val.put("Nombre",""+lbagg.get(pos).getNombre()+"");//aqui NO hay que meter ' porque es un DATO, no un CAMPO
        val.put("Precio",lbagg.get(pos).getPrecio());
        val.put("Rate", lbagg.get(pos).getRate());
        val.put("Fav", fav);
        System.out.println(fav + " es el rateo del elemento " + pos + " \n");
        val.put("Antiguedad", lbagg.get(pos).getAntiguedad());
        //Actualiza en la db el elemento con ese ID (ese nombre) y cierra
        //NOTA: En update tambien hay que meter todos los campos, igual que en el metodo de abajo
        //Este no funciona bien pero no pasa nah
        /*db.rawQuery("UPDATE Bocatas SET Nombre='" + lbagg.get(pos).getNombre() +"',Precio="+lbagg.get(pos).getPrecio()+",Rate="+lbagg.get(pos).getRate()+""+
                ",Fav=" + fav +",Antiguedad="+lbagg.get(pos).getAntiguedad()+" WHERE Nombre='" + lbagg.get(pos).getNombre()+"'",null);*/

        //Otra forma de hacerlo
        //JOOOOOODE, aqui el 3er campo es tooooda la condicion "WHERE Nombre=getNombre()", el Nombre= es importante
        db.update("Bocatas", val, "Nombre='" + lbagg.get(pos).getNombre() + "'", null);
        //Aqui si hemos metido los ' para hacer el update en base al String Nombre_del_bocata
        db.close();

        ListOfThings.lbocata.get(pos).setFav(fav);

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
