package com.sanzfdu.cafeteriaetsib.pl;

import android.app.Activity;
import android.support.v4.app.Fragment;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import android.os.Bundle;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.ListView;

import com.sanzfdu.cafeteriaetsib.R;
import com.sanzfdu.cafeteriaetsib.bl.ListOfThings;
import com.sanzfdu.cafeteriaetsib.bl.TextAdapter;
import com.sanzfdu.cafeteriaetsib.dl.Bocata;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class Randomize extends Fragment {

    private  ListView lv;
    private List<String> data = new ArrayList<String>();
    View rootView;
    private List<Bocata> lbocata;




    //Es el string que nos va a permitir cambiar el titulo de la activity a la que pertenece este fragmento

    public Randomize() {
        // Constructor vacio obligatorio
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_randomize, container, false);
        Random rand = new Random();
        int pos;
        List <String> auxLBoc = new ArrayList<String>();

        //Inicializamos los botones para que puedan cumplir su funcion al hacer click
        startButtonListening();

        lv = (ListView)rootView.findViewById(R.id.listado);

            lbocata = new ArrayList<Bocata>();

        ListOfThings lof;
        lbocata = ListOfThings.lbocata;

            if (lbocata.size()!=0) {

                //Buscar posicion al azar en la lista y obtener el objeto, FALTA METODO PARA CALCULAR ESO
                pos = rand.nextInt(lbocata.size());
            /*Este setAdapter es con el listado de objetos, lo ponemos separado del otro porque necesitamos que adapte
            de distinta manera en cada caso
            Ademas, usamos una lista auxiliar para mandarlo al listview solo con ese bocata*/
                auxLBoc.add(createText(lbocata.get(pos)));
                lv.setAdapter(new TextAdapter(getActivity().getApplicationContext(), auxLBoc));
            } else {

                data.add("Base de datos vacia");
                lv.setAdapter(new TextAdapter(getActivity().getApplicationContext(), data));
            }

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void startButtonListening() {

        Button btnRet = (Button) rootView.findViewById(R.id.buttonReturn);

        btnRet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                MenuPrincipal princMen = new MenuPrincipal();
                fragmentTransaction.replace(R.id.container_body, princMen);
                fragmentTransaction.commit();
            }
        });
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
                    s = s.concat(",");
                }
            }
            s = s.concat("\n");
        }


        return s;
    }

}
