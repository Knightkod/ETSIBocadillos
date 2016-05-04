package com.sanzfdu.cafeteriaetsib.pl;

import android.os.Bundle;

import android.app.Activity;
import android.support.v4.app.Fragment;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.ListView;

import com.sanzfdu.cafeteriaetsib.dl.Constants;
import com.sanzfdu.cafeteriaetsib.R;
import com.sanzfdu.cafeteriaetsib.bl.NetworkConnect;
import com.sanzfdu.cafeteriaetsib.bl.Pedido;
import com.sanzfdu.cafeteriaetsib.bl.TextAdapter;
import com.sanzfdu.cafeteriaetsib.bl.DataListAdapter;

import com.sanzfdu.cafeteriaetsib.bl.MySQL;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;


import com.sanzfdu.cafeteriaetsib.dl.Bocata;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;



public class Activity_fav extends Fragment{

    private  ListView lv;
    View rootView;

    private  List<String> data=new ArrayList<String>();
    private  List<Bocata> lbocata;

    private DataListAdapter dListAdapter;

    public Activity_fav(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        boolean bool = true;//Comprobara si hay algun elemento a mostrar o no

        rootView = inflater.inflate(R.layout.fragment_activity_fav, container, false);

        //Inicializamos los botones para que puedan cumplir su funcion al hacer click
        startButtonListening();

        lv = (ListView)rootView.findViewById(R.id.listado);
        //Cogemos donde ira el listado
        lbocata = new ArrayList<Bocata>();
        MySQL cn = new MySQL(getActivity().getApplicationContext(),"bocatasUni.db",null, Constants.vers);

        SQLiteDatabase db = cn.getReadableDatabase();

        Cursor cBoc = db.rawQuery("SELECT * FROM Bocatas", null) ;

        if(cBoc.moveToFirst()) {
            lbocata = cn.extractData(cBoc,db);

            //Que solo se muestren los elementos que se han valorado
            for(int i = 0; i < lbocata.size();i++){
                if(lbocata.get(i).getFav() < 3){
                    lbocata.remove(i);
                    i--;
                    if (lbocata.size() == 0) {
                        data.add("Solo los bocatas con una valoracion de 3 estrellas o mas pueden aparecer aqui\n");
                        bool = false;
                        lv.setAdapter(new TextAdapter(getActivity().getApplicationContext(), data));
                    }
                }
            }
            if(bool==true) {
                //Buscar posicion en la lista para que este ordenado por fav, FALTA METODO PARA CALCULAR ESO
                Collections.sort(lbocata, Bocata.Comparators.FAV);
                dListAdapter=new DataListAdapter(getActivity().getApplicationContext(), lbocata);
                lv.setAdapter(dListAdapter);
            }
        }else{

            data.add("Base de datos vacia\n");
            lv.setAdapter(new TextAdapter(getActivity().getApplicationContext(),data));
        }
        db.close();
        cBoc.close();

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
