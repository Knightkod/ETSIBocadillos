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

import android.widget.AdapterView;
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
import java.util.List;


public class List_complete_filters extends Fragment {

    private ListView lv;
    View rootView;

    private List<String> data =new ArrayList<String>();

    private List<Bocata> lbocata;

    private DataListAdapter dListAdapter;

    public List_complete_filters(){

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_complete_filters, container, false);

        //Inicializamos los botones para que puedan cumplir su funcion al hacer click
        startButtonListening();

        lv = (ListView)rootView.findViewById(R.id.listado);

        //un OnItemClickListener aquí capturaría el click del usuario sobre el elemento de la lista,
        // y NO sobre el button/imagebutton

        lbocata = ListOfThings.lbocata;
        if(lbocata.size()!=0) {//Para comprobar si ha encontrado algo o no, si lo hay es true, si no hay nada es false

        /*Este setAdapter es con el listado de objetos, lo ponemos separado del otro porque necesitamos que adapte
            de distinta manera en cada caso*/

            /*this: contexto, parametro 2 referencia a recurso a la vista que se va a
            utilizar para crear la lista
            3er parametro ese lque indica donde va a ir el elemento que viene despues
            4o elemento es lo que se va a visualizar
            */
            dListAdapter= new DataListAdapter(getActivity().getApplicationContext(),lbocata);

            lv.setAdapter(dListAdapter);

           }
        else{
            data.add("No se ha podido encontrar nada en la base de datos");

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
                    Pedido.realizaPedido(dListAdapter,getActivity().getApplicationContext());
                else
                    Toast.makeText(getActivity().getApplicationContext(),"Por favor, conectese a internet para realizar el pedido."
                            , Toast.LENGTH_SHORT).show();
            }
        });

    }

}
