package com.example.juandavid.groupchat.util.services;

import android.app.Activity;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.juandavid.groupchat.ChatActivity;
import com.example.juandavid.groupchat.util.interfaces.AsyncResponse;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

/**
 * Created by sebastiancardona on 24/09/17.
 */

public class GenericService extends AsyncTask<String, String, String> {

    private boolean ifWhile;
    private StringBuffer buffer = null;
    private String method;
    //private final String ruta = "http://192.168.1.55/WebServicesAndroid/ConexionOracle.php";
    private String ruta;
    Activity activity;
    ProgressBar carga;
    private String json;
    public AsyncResponse delegate = null;

    /**
     *
     * @param ruta
     * @param json
     * @param activity
     * @param carga
     * @param method
     * @param ifWhile, identify if it is necessary to consume the method in a while
     * @param asyncResponse
     */
    public GenericService(String ruta, String json, Activity activity,
                             ProgressBar carga, String method, boolean ifWhile,
                                AsyncResponse asyncResponse) {
        this.activity = activity;
        this.carga = carga;
        this.ruta = ruta;
        this.json = json;
        this.delegate = asyncResponse;
        this.method = method;
        this.ifWhile = ifWhile;
    }


    @Override
    protected void onPreExecute() {
        if(ifWhile == false) {
            carga.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onProgressUpdate(String... values) {
        if (ifWhile){
            ((ChatActivity)activity).fillInListView(values[0].toString());
        } else {
            Toast.makeText(activity, values[0],
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected String doInBackground(String... params) {
        if(ifWhile){
            return consumeServiceWithWhile();
        } else {
            return consumeServiceWithoutWhile();
        }
    }


    @Override
    protected void onPostExecute(String result) {
        try {
            if (result != "") {
                delegate.processFinish(buffer.toString());
                carga.setVisibility(View.GONE);

            } else {
                Toast.makeText(activity, "Error en la operacion",
                        Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(activity, "Error en los Datos Ingresados ",
                    Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private String consumeServiceWithWhile(){
        while(true) {
            Log.i("Holasldasld", "1");
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            HttpURLConnection conn = null;
            BufferedReader reader = null;

            try {

                URL url = new URL(ruta);
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod(method);
                Uri.Builder builder = new Uri.Builder();
                if (method.equalsIgnoreCase("post")) {
                    conn.setRequestProperty("Content-Type", "application/json");
                    conn.setRequestProperty("Accept", "application/json");
                    OutputStream os = conn.getOutputStream();
                    os.write(json.toString().getBytes("UTF-8"));
                    os.close();
                }
                conn.connect();
                Thread.sleep(3000);
                InputStream stream = conn.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));
                buffer = new StringBuffer();
                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }
                publishProgress(buffer.toString());
            } catch (MalformedURLException e) {
                publishProgress("Error mal estructura URL " + e.getMessage());
                e.printStackTrace();
                return "";
            } catch (IOException e) {
                publishProgress("Error IO " + e.getMessage());
                e.printStackTrace();
                return "";
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                if (conn != null) {
                    conn.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    publishProgress("Error al final " + e.getMessage());
                    e.printStackTrace();
                    return "";
                }
            }
        }
    }

    private String consumeServiceWithoutWhile(){
        HttpURLConnection conn = null;
        BufferedReader reader = null;

        try {

            URL url = new URL(ruta);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(method);
            if(method.equalsIgnoreCase("post")){
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("Accept", "application/json");
                OutputStream os = conn.getOutputStream();
                os.write(json.toString().getBytes("UTF-8"));
                os.close();
            }

            conn.connect();
            Thread.sleep(3000);
            InputStream stream = conn.getInputStream();
            reader = new BufferedReader(new InputStreamReader(stream));
            buffer = new StringBuffer();
            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
        } catch (MalformedURLException e) {
            publishProgress("Error mal estructura URL " + e.getMessage());
            e.printStackTrace();
            return "";
        } catch (IOException e) {
            publishProgress("Error IO " + e.getMessage());
            e.printStackTrace();
            return "";
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                publishProgress("Error al final " + e.getMessage());
                e.printStackTrace();
                return "";
            }
        }
        return "callback!!!";
    }

}
