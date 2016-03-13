package com.sanzfdu.cafeteriaetsib.pl;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.widget.Toolbar;
import android.view.View;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.sanzfdu.cafeteriaetsib.bl.ListOfThings;
import com.sanzfdu.cafeteriaetsib.dl.Constants;
import com.sanzfdu.cafeteriaetsib.R;
import com.sanzfdu.cafeteriaetsib.bl.CallAPI;
import com.sanzfdu.cafeteriaetsib.bl.InterfaceCallAPI;
import com.sanzfdu.cafeteriaetsib.bl.JSONParser;
import com.sanzfdu.cafeteriaetsib.bl.MySQL;
import com.sanzfdu.cafeteriaetsib.bl.NetworkConnect;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends ActionBarActivity implements InterfaceCallAPI,FragmentDrawer.FragmentDrawerListener {

    private Toolbar mToolbar;
    private FragmentDrawer drawerFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Para el correcto funcionamiento de la toolbar
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("Inicio"); //Cambia el mensaje mostrado en la toolbar
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        drawerFragment = (FragmentDrawer)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
        drawerFragment.setDrawerListener(this);
        //para que se ejecute una sola vez al iniciar la app
        if(NetworkConnect.compruebaConexion(getApplicationContext())) {
            //conectarse para bajar la version y la db actual
            CallAPI callAPI = new CallAPI(this);
            callAPI.execute(getApplicationContext().getResources().getString(R.string.URL_Version));


        }
        else {
            ListOfThings lof = new ListOfThings();
            lof.fillLists(getApplicationContext());
        }
        // display the first navigation drawer view on app launch
        displayView(0);

    }


//entiendo que el onCreateOptions se ejecuta cada vez que cambio de vista y el onCreate no
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        //Si quisiera borrar totalmente la db para crearla de nuevo
        //this.deleteDatabase("bocatasUni.db");

        /*NOTA! Si en el parametro version (ultimo parametro del new) le pongo un numero mas alto,
        ejecuta el OnUpgrade de la clase MySQL, con lo que es facil actualizar todo remotamente, pero
        es importante tener en cuenta que tambien habria que actualizar el string de StringsDB*/


        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //Manejo de los action buttons
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDrawerItemSelected(View view, int position) {
        displayView(position);
    }

    private void displayView(int position) {
        Fragment fragment = null;
        String title = getString(R.string.app_name);
        switch (position) {
            case 0:
                fragment = new MenuPrincipal();
                title = getString(R.string.title_menu_principal);
                break;
            case 1:
                fragment = new List_complete_filters();
                title = getString(R.string.title_list_complete_filters);
                break;
            case 2:
                fragment = new Filter();
                title = getString(R.string.title_filter);
                break;
            case 3:
                fragment = new Activity_fav();
                title = getString(R.string.title_activity_fav);
                break;
            case 4:
                fragment = new Latest();
                title = getString(R.string.title_latest);
                break;
            case 5:
                fragment = new Rating();
                title = getString(R.string.title_rating);
                break;
            case 6:
                fragment = new About_us();
                title = getString(R.string.title_about_us);
                break;

            default:
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, fragment);
            fragmentTransaction.commit();

            // set the toolbar title
            getSupportActionBar().setTitle(title);
        }
    }

    @SuppressWarnings("unchecked")
    public void parseCallResponse(JSONObject json) {
        int vers;
        try {
            JSONArray versiones = json.getJSONArray("results");
            Constants.vers = JSONParser.parseVersion( versiones );
            vers = Constants.vers;
            if(0<vers) {
                MySQL cn = new MySQL(getApplicationContext(),"bocatasUni.db", null, vers);
                cn.getWritableDatabase();
                if(!MySQL.needsUpgrade) {
                    System.out.println("Tengo la version mas actual");
                    ListOfThings lof = new ListOfThings();
                    lof.fillLists(getApplicationContext());
                    //cn.close();
                }

            }
        } catch (JSONException e) {

        }
    }

}

