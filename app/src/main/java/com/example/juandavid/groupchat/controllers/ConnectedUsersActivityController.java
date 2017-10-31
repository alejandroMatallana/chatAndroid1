package com.example.juandavid.groupchat.controllers;

import android.app.Activity;
import android.widget.ProgressBar;

import com.example.juandavid.groupchat.util.enums.ServiceMethodEnum;
import com.example.juandavid.groupchat.util.interfaces.AsyncResponse;
import com.example.juandavid.groupchat.util.services.GenericService;

/**
 * Created by Juan David on 26/09/2017.
 */

public class ConnectedUsersActivityController {

    public ConnectedUsersActivityController(){}

    public void listConnectedUsers(String ruta, Activity activity,
                            ProgressBar carga, AsyncResponse asyncResponse) {

        GenericService asyncTask = new GenericService(ruta, "", activity,
                carga, ServiceMethodEnum.GET.toString(), false, asyncResponse);
        asyncTask.execute();
    }

}
