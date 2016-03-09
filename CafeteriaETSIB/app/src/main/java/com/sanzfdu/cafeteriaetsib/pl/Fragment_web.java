package com.sanzfdu.cafeteriaetsib.pl;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ValueCallback;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.sanzfdu.cafeteriaetsib.R;
import com.sanzfdu.cafeteriaetsib.bl.Pedido;

public class Fragment_web extends Fragment {


    View rootView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_web, container, false);
        WebView myWebView = (WebView) rootView.findViewById(R.id.webView);
        myWebView.loadUrl("http://etsibocadillos.no-ip.biz/usuarioAuth.xhtml");
        //Esta segunda parte es para autorrellenar los campos del formulario que se requieren
        WebSettings settings = myWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        //codigo javascript para rellenar parte del formulario de pedido. solo funciona con tagName y no con byId, no se porque
        final String js="document.getElementsByTagName('input')[4].value = '" + Pedido.getNames() + "';document.getElementsByTagName('input')[5].value = '"+ Pedido.getCosteTot() + "';";

            myWebView.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view,url);
                    //para saber la version de android que se está usando,
                    // porque este codigo solo vale para versiones por encima de la 19
                    if(19 <= Build.VERSION.SDK_INT){

                        view.evaluateJavascript(js, new ValueCallback<String>() {
                            @Override
                            public void onReceiveValue(String value) {

                            }
                        });
                    }//Y este de aqui abajo solo para las versiones por debajo de la 19
                    else{
                        System.out.println("caca2");
                    view.loadUrl(js);
                    }
                }
            });


        return rootView;
    }
}
