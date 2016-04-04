package com.sanzfdu.cafeteriaetsib.bl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;

import org.apache.http.client.HttpClient;

import org.apache.http.client.methods.HttpGet;

import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.widget.Toast;

import com.sanzfdu.cafeteriaetsib.dl.Constants;

/**
 * Created by pablo on 10/03/16.
 * código obtenido de aritzbi en el proyecto "SeriesAPP" colgado en GITHUB
 */
public class CallAPI extends AsyncTask<String, Void, JSONObject>{

    private boolean isJSONResponse;
    private InterfaceCallAPI iCallAPI;

    public CallAPI (InterfaceCallAPI iCallAPI)
    {
        this.iCallAPI =iCallAPI;
        isJSONResponse = true;
    }

    @Override
    protected JSONObject doInBackground(String... params) {
        String response = null;

            response = makeGetCall ( params[0] );

        System.out.println("Respuesta de la pagina");
        JSONObject jsonObject = null;

        if ( isJSONResponse )
        {
            try {
                jsonObject = new JSONObject(response);
            } catch (JSONException e) {
                System.out.println("Error en el modulo CallAPI");
            }
        }
        else
        {
            jsonObject = new JSONObject();
        }
        return jsonObject;
    }

    @Override
    protected void onPostExecute(JSONObject result) {

        if ( result == null )
        {
           System.out.println("No se han obtenido resultados");
        }
        else
        {

            if ( iCallAPI != null )
            {
                iCallAPI.parseCallResponse(result);
            }
        }
    }

    private static String makeGetCall(String url){
        InputStream inputStream = null;
        String result = "";
        try {
            // make GET request to the given URL
            HttpGet httpGet = new HttpGet(url);
            //Enabling http params for enabling timeouts
            HttpParams httpParams = new BasicHttpParams();
            //setting timeouts values
            int timeoutConnection = 3000;
            HttpConnectionParams.setConnectionTimeout(httpParams, timeoutConnection);
            int timeoutSocket=5000;
            HttpConnectionParams.setSoTimeout(httpParams,timeoutSocket);
            // create HttpClient
            DefaultHttpClient httpclient = new DefaultHttpClient(httpParams);
            // Obtaining the response of the server or error if timeout is done
            HttpResponse httpResponse = httpclient.execute(httpGet);

            // receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();

            // convert inputstream to string
            if(inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";
        } catch (Exception e) {
            //Para que no quede atascado en el dialog si está apagado el servidor
            Constants.mProgDiag.dismiss();
           System.out.println("Error al intentar obtener los objetos JSON");
        }

        return result;
    }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }

}
