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
import com.sanzfdu.cafeteriaetsib.bl.DataListAdapterRate;
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


public class Rating extends Fragment {

    private  ListView lv;
    View rootView;

    private   List<String> data= new ArrayList<String>();
    private  List <Bocata> lbocata;

    private DataListAdapterRate dListAdapter;

    public Rating(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        boolean bool = true;//Comprobara si hay algun elemento a mostrar o no

        rootView = inflater.inflate(R.layout.fragment_rating, container, false);

        //Inicializamos los botones para que puedan cumplir su funcion al hacer click
        startButtonListening();

        lv = (ListView)rootView.findViewById(R.id.listado);
        lbocata = new ArrayList<Bocata>();

         MySQL cn = new MySQL(getActivity().getApplicationContext(),"bocatasUni.db",null,1);

        SQLiteDatabase db = cn.getReadableDatabase();
        //OJOOOOOO el rateo lo mete el usuario??? SI ES ASI AQUI TENEMOS QUE HACER OTRO METODO PARA QUE EL USUARIO PUEDA METER EN LA BASE DE DATOS el valor
        Cursor  cBoc = db.rawQuery("SELECT * FROM Bocatas", null) ;

        if(cBoc.moveToFirst()) {
            //Obtenemos la lista de ingredientes de cada bocata
            //Crear nuevo objeto con los valores del objeto de la db apuntados por la variable c
            //add el objeto en la lista de objetos
            lbocata = cn.extractData(cBoc,db);

            for(int i = 0; i < lbocata.size();i++){
                if(lbocata.get(i).getRate() == 0) {

                    lbocata.remove(i);
                    i--;
                    if (lbocata.size() == 0) {
                        bool = false;
                        data.add("Parece que no has votado ningun bocadillo! Aqui se mostraran todos los que votes\n");
                        lv.setAdapter(new TextAdapter(getActivity().getApplicationContext(), data));
                    }
                }
            }
            if(bool==true) {
                //Buscar posicion en la lista para que este ordenado por prorateo, FALTA METODO PARA CALCULAR ESO
                Collections.sort(lbocata, Bocata.Comparators.RATE);
            /*Este setAdapter es con el listado de objetos, lo ponemos separado del otro porque necesitamos que adapte
            de distinta manera en cada caso*/
                dListAdapter=new DataListAdapterRate(getActivity().getApplicationContext(), lbocata);
                lv.setAdapter(dListAdapter);
            }
        }else{

            data.add("Base de datos vacia");
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
                    float costeTot=0;
                    String names="";
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
                        Toast.makeText(getActivity().getApplicationContext(), "Debe seleccionar al menos un bocadillo para poder comprar", Toast.LENGTH_SHORT).show();
                    else {
                        FragmentManager fragmentManager = getFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        Fragment_web fragWeb = new Fragment_web();
                        fragmentTransaction.replace(R.id.container_body,fragWeb);
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
