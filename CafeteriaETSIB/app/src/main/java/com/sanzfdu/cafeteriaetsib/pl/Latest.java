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
import com.sanzfdu.cafeteriaetsib.bl.ListOfThings;
import com.sanzfdu.cafeteriaetsib.bl.NetworkConnect;
import com.sanzfdu.cafeteriaetsib.bl.Pedido;
import com.sanzfdu.cafeteriaetsib.bl.TextAdapter;
import com.sanzfdu.cafeteriaetsib.bl.DataListAdapter;
import com.sanzfdu.cafeteriaetsib.bl.MySQL;
import com.sanzfdu.cafeteriaetsib.dl.Bocata;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class Latest extends Fragment {

    private  ListView lv;
    View rootView;
    private  List<String>data=new ArrayList<String>();
    private  List<Bocata> lbocata;
    private DataListAdapter dListAdapter;

    public Latest(){

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_latest, container, false);

        //Inicializamos los botones para que puedan cumplir su funcion al hacer click
        startButtonListening();

        //Metemos aqui la lista para que simplemente sea segun se carga la actividad salga la lista
        lv = (ListView)rootView.findViewById(R.id.listado);
        lbocata = new ArrayList<Bocata>();
        for (int i = 0; i < ListOfThings.lbocata.size(); i++) {
            lbocata.add(ListOfThings.lbocata.get(i));
        }
        if(lbocata.size()!=0){


            //Buscar posicion en la lista para que este ordenado por tiempo, FALTA METODO PARA CALCULAR ESO

            Collections.sort(lbocata,Bocata.Comparators.OLD);
            /*Este setAdapter es con el listado de objetos, lo ponemos separado del otro porque necesitamos que adapte
            de distinta manera en cada caso*/

            //El ArrayAdapter puede ser un objeto del tipo que nos de la gana, la prueba la he hecho con strings
       /*OJO con el parametro 2 de ArrayAdapter,si usaramos ese ArrayAdapter en el setadapter
       habria que poner el android antes del .R (quedando android.R.layout.simple_list_item_1, si ponemos solo R como
        antes casca!!*/
            dListAdapter= new DataListAdapter(getActivity().getApplicationContext(),lbocata);
            lv.setAdapter(dListAdapter);
        }else {
            data.add("Base de datos vacia!");
            lv.setAdapter(new TextAdapter(getActivity().getApplicationContext(),data));
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
                ((MainActivity)getActivity()).setActionBarTitle("Inicio");
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
                if(NetworkConnect.compruebaConexion(getActivity().getApplicationContext())) {
                    if(Pedido.realizaPedido(dListAdapter, getActivity().getApplicationContext())) {

                        FragmentManager fragmentManager = getFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        Fragment_web fragWeb = new Fragment_web();
                        fragmentTransaction.replace(R.id.container_body, fragWeb);
                        fragmentTransaction.commit();
                    }
                }
                else
                    Toast.makeText(getActivity().getApplicationContext(),"Por favor, conectese a internet para realizar el pedido."
                            , Toast.LENGTH_SHORT).show();
            }
        });

    }
}
