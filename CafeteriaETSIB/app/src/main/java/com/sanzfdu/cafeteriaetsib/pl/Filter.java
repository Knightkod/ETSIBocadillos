package com.sanzfdu.cafeteriaetsib.pl;

import android.app.Activity;
import android.support.v4.app.Fragment;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.sanzfdu.cafeteriaetsib.dl.Constants;
import com.sanzfdu.cafeteriaetsib.R;
import com.sanzfdu.cafeteriaetsib.bl.DataListAdapter;
import com.sanzfdu.cafeteriaetsib.bl.ListOfThings;
import com.sanzfdu.cafeteriaetsib.bl.NetworkConnect;
import com.sanzfdu.cafeteriaetsib.bl.Pedido;
import com.sanzfdu.cafeteriaetsib.bl.TextAdapter;
import com.sanzfdu.cafeteriaetsib.bl.MySQL;
import com.sanzfdu.cafeteriaetsib.dl.Bocata;
import com.sanzfdu.cafeteriaetsib.dl.Ingrediente;

import java.util.ArrayList;
import java.util.List;


public class Filter extends Fragment {

    private ListView lv;
    private List<String> data = new ArrayList<String>();
    private List<Bocata> lbagg;
    private  List <Ingrediente> lingr;
    private boolean compr = false;
    private DataListAdapter dListAdapter;

    private View rootView;


    public Filter(){

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       rootView = inflater.inflate(R.layout.fragment_filter, container, false);

       //Para ocultar el boton de pago hasta que salga la lista de ingredientes

        Button btnPay = (Button)rootView.findViewById(R.id.buttonPay);
        btnPay.setVisibility(View.GONE);

        lv = (ListView)rootView.findViewById(R.id.listado);

        //Inicializamos los botones para que puedan cumplir su funcion al hacer click
        startButtonListening();

        List<String> lingrString = new ArrayList<String>();

        lv = (ListView)rootView.findViewById(R.id.listado);
        lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        lingr = new ArrayList<Ingrediente>();
        lingr = ListOfThings.lingr;

        if(lingr.size() != 0){
            /*Como tal arrayadapter trabajar con elementos no String como que no, asi que lo mejor
            es transformar a string toda la lista y trabajar en base a eso*/
            for(int i= 0;i < lingr.size();i++){
                lingrString.add(lingr.get(i).getNombre());
            }

            lv.setAdapter(new ArrayAdapter<String>(getActivity().getApplicationContext(),R.layout.checkbox_view, R.id.list_elem, lingrString));
        }else{
            data.add("No hay ingredientes en la base de datos");
            //Esto hay que dejarlo como estaba!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
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

    public void startButtonListening(){

        Button btn = (Button)rootView.findViewById(R.id.button);
        Button btnRet = (Button)rootView.findViewById(R.id.buttonReturn);
        Button btnPay = (Button)rootView.findViewById(R.id.buttonPay);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search();
            }
        });

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


    //METODOS PARA SACAR LISTA DE BOCATAS
    //Importante el View view para que funcione
    public void search(){
        List<String> namBaggs = new ArrayList<String>();
        List<String> namIngr = new ArrayList<String>();


        //Cogemos las posiciones de los elementos habilitados (checkbox marcados)
        SparseBooleanArray selected = lv.getCheckedItemPositions();

        if(selected == null||selected.size()==0){
            Toast.makeText(getActivity().getApplicationContext(),"No hay ningun ingrediente marcado.\n", Toast.LENGTH_SHORT).show();
        }
        else {
            //Para que el boton desaparezca
            Button bt = (Button)rootView.findViewById(R.id.button);
            Button btnPay = (Button)rootView.findViewById(R.id.buttonPay);
            bt.setVisibility(View.GONE);


            for(int i = 0; i < selected.size();i++){
                if(selected.valueAt(i)){
                    //Anadimos a la lista de nombres de ingredientes
                    /*NOTA: Con las pruebas en las que no habia lingr rellena en la db fallaba. esto tiene sentido
                    porque obviamente le estoy diciendo cosas como "coge el elemento 1 de una lista
                    de 0 elementos"*/
                   namIngr.add(lingr.get(selected.keyAt(i)).getNombre());

                }
            }
            // se consiguen los bocatas asociados a esos ingredientes
            namBaggs = searchBaggHasIngr(namIngr);
            //COGEMOS LOS BOCATAS
            searchBaggete(namBaggs);

            if (compr) {
                btnPay.setVisibility(View.VISIBLE);
                dListAdapter=new DataListAdapter(getActivity().getApplicationContext(),lbagg);
                lv.setAdapter(dListAdapter);

            } else {
                data.add("No hay bocadillos con esos ingredientes en la base de datos");
                lv.setAdapter(new TextAdapter(getActivity().getApplicationContext(), data));
            }
        }

    }

    //Para sacar los nombres de bocatas con esos ingredientes
    private List<String> searchBaggHasIngr(List<String> s){
        List<String> lnameBaggs  = new ArrayList<String>();
        String nameBaggs ="";
        String nameBaggs2 ="";
        List<String> aux = new ArrayList<String>();
        String query = null;

        if(0 < s.size()) {
            MySQL cn = new MySQL(getActivity().getApplicationContext(), "bocatasUni.db", null, Constants.vers);
            SQLiteDatabase db = cn.getReadableDatabase();
            for (int i = 0; i < s.size(); i++) {
                aux.add(s.get(i));//OJOOOO aux hay que reiniciarlo que si no estoy pidiendo cada vez en base a mas ingredientes
                query = MySQL.createQuery(aux, "Bocata_has_ingrediente", "Ingredientes_Nombre", "OR");
                Cursor cBaggHasIngr = db.rawQuery(query, null);
                if (cBaggHasIngr.moveToFirst()) {
                    do {
                        //El if lo usariamos para simplificar si pudieramos guardar los nombres solo una vez
                        //if(!nameBaggs.contains(cBaggHasIngr.getString(0))){
                        //Consigo los bocatas de un ingrediente cada vez, los junto y los separo mas abajo
                        //CUIDADO!! Como Bocata has ingrediente tiene varias veces el mismo nombre, puede que el mismo
                        //Ingrediente de resultados parecidos cada vez
                        nameBaggs = nameBaggs + cBaggHasIngr.getString(0)+",";
                        //}

                    } while (cBaggHasIngr.moveToNext());

                    if(nameBaggs.endsWith(","))
                    {
                        //Substring divide desde donde indica el 1er parametro hasta el ultimo
                        /*1er parametro index de inicio (donde empezar a capturar la string original)
                        , 2o parametro index de cuantos caracteres coger desde lo que se ha marcado como inicio*/
                        nameBaggs = nameBaggs.substring(0,nameBaggs.length() - 1);
                        nameBaggs = nameBaggs + ";";
                    }
                    cBaggHasIngr.close();
                    nameBaggs2 = nameBaggs2 + nameBaggs;
                    //Hay que reinicializarlo todo para que no me vuelva a hacer la consulta de los bocatas anteriores
                    nameBaggs = new String();
                    aux = new ArrayList<String>();

                }
            }
            //Rehacemos namBaggs con la comprobacion de que cada bocata tiene los ingredientes pedidos
            //pero solo en caso de que tengamos algun bocata para poder buscarlo
            if(nameBaggs2.length() != 0) {
                lnameBaggs = comprBaggs(nameBaggs2);
            }
        }
        return lnameBaggs;
    }

    public List<String> comprBaggs(String lNameBagg){
       /* Collection collection = (Collection)lNameIngr;
        System.out.println(collection.toString() + "\n");*/

        String[] arrayStringIngr;//Division en base a ingredientes
        String[] arrayStringBagg;//Division en base a bocatas
        List<String> lAux = new ArrayList<String>();
        int count = 0;          //Finalidad: Contar si el bocata existe en todas las otras busquedas

        if(lNameBagg.split(";").length == 1){
            lNameBagg = lNameBagg.substring(0,lNameBagg.length()-1);
            arrayStringIngr = lNameBagg.split(",");//arrayStringIngr lo uso aqui por no crear otro array aux, pero deberia
            for(int i = 0;i < arrayStringIngr.length;i++){
                lAux.add(arrayStringIngr[i]);
            }
        }else {
            //La cantidad de "busquedas" es igual al numero de ingredientes que habia
            arrayStringIngr = lNameBagg.split(";");
            arrayStringBagg = arrayStringIngr[0].split(",");//Obtengo todos los bocadillos asociados al primer ingrediente. Si ninguno
                                                            //de estos bocatas esta en el resto de splits de la lista anterior, ya da
                                                            //igual, porque ningun bocata estara en todas las busquedas hechas con los ingredientes
            for (int i = 0; i < arrayStringBagg.length; i++) { //Por cada elemento de la lista de bocatas asociados al primer ingrediente
                for (int j = 1; j < arrayStringIngr.length; j++) { //eligeme las otras listas de bocatas asociadas a otros ingredientes 1 a 1
                    for(int k = 0;k <arrayStringIngr[j].split(",").length;k++) { //Y comprueba que cada elemento es o no el bocata
                        System.out.println("Comparando "+ arrayStringBagg[i]+" con "+arrayStringIngr[j].split(",")[k]);
                        if (arrayStringIngr[j].split(",")[k].equals(arrayStringBagg[i])) {
                        //TIENE QUE SER ".equals", no ".contains", porque si no por ejemplo si un bocata es "Bacon" y otro "Cubanito Bacon"
                        //los consideraría iguales y no lo son
                            count++;//Como mucho esta una vez por fraccion de la lista de bocatas original
                            k = arrayStringIngr[j].split(",").length;
                        }
                            if (count == arrayStringIngr.length - 1) {//CUIDADO, tenia puesto count++ y no filtraba correctamente
                                System.out.println(arrayStringBagg[i] + " Gracias por Dios, el elemento " + i + " \n");
                                count = 0;
                                if (!lAux.contains(arrayStringBagg[i])) {//Comprobamos si ya existe el bocata en la lista o no
                                    lAux.add(arrayStringBagg[i]);
                                }
                            }
                    }
                }
                count = 0;//Cada bocata tiene que tener su contador para ver si esta o no
            }
            //codigo que creo que era simplificable
            /*for (int i = 0; i < arrayStringIngr.length; i++) {
                arrayStringBagg = arrayStringIngr[i].split(",");
                for (int j = 0; j < arrayStringBagg.length; j++) {
                    for (int k = 0; k < arrayStringIngr.length; k++) {
                        if (k != i) {
                            if (arrayStringIngr[k].contains(arrayStringBagg[j])) {
                                count++;
                            }
                            if (count++ == arrayStringIngr.length - 1) {
                                System.out.println("Gracias por Dios \n");
                                if (!lAux.contains(arrayStringBagg[j])) {
                                    lAux.add(arrayStringBagg[j]);
                                }
                            }
                        }
                    }
                }
            }*/
        }

        return lAux;
    }

    private void searchBaggete(List<String> s){

        lbagg = new ArrayList<Bocata>();
        List <Ingrediente> lingr2 = null;
        Cursor cBocHasIngr = null;
        Bocata bagg;
        String query =null;

        if(0<s.size()){

            query = MySQL.createQuery(s,"Bocatas","nombre","OR");
        MySQL cn = new MySQL(getActivity().getApplicationContext(),"bocatasUni.db",null,Constants.vers);
        SQLiteDatabase db = cn.getReadableDatabase();
        Cursor cBagg = null;
        cBagg = db.rawQuery(query, null);
            if(cBagg.moveToFirst()) {
                do {
                    cBocHasIngr = db.rawQuery("SELECT Ingredientes_Nombre FROM Bocata_has_ingrediente WHERE Bocatas_Nombre='"+cBagg.getString(0)+"'",null);
                    lingr2=buscaIngredientes(cBocHasIngr,db);
                    bagg = new Bocata(cBagg.getString(0), cBagg.getFloat(1), cBagg.getFloat(2),cBagg.getFloat(3), cBagg.getInt(4),lingr2);
                    lbagg.add(bagg);
                } while (cBagg.moveToNext());
                compr = true;
            }
            else{
            compr = false;
            }

            db.close();
            cBagg.close();
        }
    }

    private List<Ingrediente> buscaIngredientes(Cursor c, SQLiteDatabase db){
        Cursor cIngr =null;
        Ingrediente ingred;
        List <Ingrediente> lingr2 = new ArrayList<Ingrediente>();
        String query = null;
        if(c.moveToFirst()) {

            do {
                cIngr = db.rawQuery("SELECT * FROM Ingredientes WHERE Nombre='" + c.getString(0) + "'", null);

                if (cIngr.moveToFirst()) {

                    ingred = new Ingrediente(cIngr.getString(0), null);
                    lingr2.add(ingred);
                }
            } while (c.moveToNext());
            cIngr.close();
        }
        else{
            lingr2 = null;
        }
    return  lingr2;
    }


}
