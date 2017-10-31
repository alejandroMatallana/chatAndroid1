package com.example.juandavid.groupchat.controllers;

import android.app.Activity;
import android.widget.ProgressBar;

import com.example.juandavid.groupchat.util.interfaces.AsyncResponse;
import com.example.juandavid.groupchat.util.services.GenericService;

/**
 * Created by sebastiancardona on 25/09/17.
 */

public class MainActivityController {

    public MainActivityController(){ }
    public GenericService asyncTask;

    public GenericService getAsyncTask() {
        return asyncTask;
    }

    public void setAsyncTask(GenericService asyncTask) {
        this.asyncTask = asyncTask;
    }

    public void connect(String ruta, String nickname, Activity activity,
                        ProgressBar carga, String method, AsyncResponse asyncResponse){

        asyncTask = new GenericService(ruta, toJson(nickname), activity,
                carga, method, false,asyncResponse);
       // asyncTask.execute();
    }

    private String toJson(String str){
        String json = "{" +
                "'user': '" + str + "'" +
                "}";
        json = json.replace("'", "\"");
        return json;
    }

}
