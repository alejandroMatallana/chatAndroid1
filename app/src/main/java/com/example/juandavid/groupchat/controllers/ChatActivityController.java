package com.example.juandavid.groupchat.controllers;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.ProgressBar;

import com.example.juandavid.groupchat.util.enums.ServiceMethodEnum;
import com.example.juandavid.groupchat.util.interfaces.AsyncResponse;
import com.example.juandavid.groupchat.util.services.GenericService;

/**
 * Created by sebastiancardona on 24/09/17.
 */

public class ChatActivityController {

    public ChatActivityController(){

    }

    public void sendMessage(String ruta, String message, String nickname, Activity activity,
                            ProgressBar progressBar, AsyncResponse asyncResponse){

        GenericService asyncTask = new GenericService(ruta, toJson(message, nickname),activity,
                progressBar, ServiceMethodEnum.POST.toString(), false, asyncResponse);
        asyncTask.execute();
    }

    public void listMessage(String ruta, Activity activity,
                            ProgressBar carga, AsyncResponse asyncResponse) {

        GenericService asyncTask = new GenericService(ruta, "", activity,
                carga, ServiceMethodEnum.GET.toString(), true, asyncResponse);
        asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public void disconnect(String ruta, String nickname, Activity activity, ProgressBar carga,
                           AsyncResponse asyncResponse){

        GenericService asyncTask = new GenericService(ruta, toJson(nickname), activity,
                carga, ServiceMethodEnum.POST.toString(), false, asyncResponse);
        asyncTask.execute();
    }

    private String toJson(String str){
        String json = "{" +
                "'user': '" + str + "'" +
                "}";
        json = json.replace("'", "\"");
        return json;
    }

    private String toJson(String str, String str1){
        String json = "{" +
                "'message': '"+str+"',"+
                "'user': '"+str1+"'"+
                "}";
        json = json.replace("'", "\"");
        return json;
    }
}
