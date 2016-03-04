package com.sanzfdu.cafeteriaetsib.pl;

import android.app.Activity;
import android.support.v4.app.Fragment;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


import com.sanzfdu.cafeteriaetsib.R;


public class About_us extends Fragment{


    View rootView;

    public About_us() {
        // Constructor vacio obligatorio
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_about_us, container, false);

        //Inicializamos los botones para que puedan cumplir su funcion al hacer click
        startButtonListening();


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
}
