package com.sanzfdu.cafeteriaetsib.pl;

import android.app.Activity;
import android.graphics.drawable.AnimationDrawable;
import android.support.v4.app.Fragment;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;

import android.os.Bundle;

import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.sanzfdu.cafeteriaetsib.R;

import java.util.concurrent.TimeUnit;


public class MenuPrincipal extends Fragment{


    public MenuPrincipal() {
        // Constructor vacio obligatorio
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_menu_principal, container, false);

        //Es el equivalente al antiguo goRand, te lleva a random al tocar el boton
        final ImageButton btn = (ImageButton)rootView.findViewById(R.id.imagebutton);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).setActionBarTitle("RANDOM");
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                Randomize rand = new Randomize();
                fragmentTransaction.replace(R.id.container_body, rand);
                fragmentTransaction.commit();
            }
        });


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

   /********************NOTA: Acordarse que el RATING de global no funciona todavia!!****/

   //Hay que cargar un par de librerias adicionales, las dos que estan separadas

    /*OJO! Intent es para poder referenciar a la nueva actividad
    * esto quiere decir qeu estoy cogiendo la referencia del objeto de esta clase y tambien
    * la referencia del activity que hace referencia al menu principal*/

/*
    public void goRate(View view){
        Toast.makeText(getApplicationContext(),"Coming soon...",Toast.LENGTH_SHORT).show();
        /*Intent i = new Intent(this,Rating.class);
        startActivity(i);*/
/*
    }
*/

}
