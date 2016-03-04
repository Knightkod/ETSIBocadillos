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
import android.widget.Toast;

import com.sanzfdu.cafeteriaetsib.R;
import com.sanzfdu.cafeteriaetsib.bl.DataListAdapter;
import com.sanzfdu.cafeteriaetsib.bl.ListOfThings;
import com.sanzfdu.cafeteriaetsib.bl.NetworkConnect;
import com.sanzfdu.cafeteriaetsib.bl.Pedido;
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

    private DataListAdapter dListAdapter;


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
        List <Bocata> auxLBoc = new ArrayList<Bocata>();

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
                auxLBoc.add(lbocata.get(pos));
                dListAdapter= new DataListAdapter(getActivity().getApplicationContext(),auxLBoc);
                lv.setAdapter(dListAdapter);
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
        Button btnPay = (Button) rootView.findViewById(R.id.buttonPay);

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
        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(NetworkConnect.compruebaConexion(getActivity().getApplicationContext()))
                    Pedido.realizaPedido(dListAdapter, getActivity().getApplicationContext());
                else
                    Toast.makeText(getActivity().getApplicationContext(),"Por favor, conectese a internet para realizar el pedido."
                            , Toast.LENGTH_SHORT).show();
            }
        });
    }


}
