package com.example.juandavid.groupchat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.example.juandavid.groupchat.controllers.ConnectedUsersActivityController;
import com.example.juandavid.groupchat.util.interfaces.AsyncResponse;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ConnectedUsersActivity extends AppCompatActivity {

    private ListView lstUsers;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connected_users);
        lstUsers = (ListView) findViewById(R.id.lstConnectedUsers);
        progressBar = (ProgressBar) findViewById(R.id.progressBar3);
        consumeServiceListMessages();
    }

    public void closedConnectedUsers(View view){
        finish();
    }

    public void consumeServiceListMessages(){
        ConnectedUsersActivityController controller = new ConnectedUsersActivityController();
        String ruta = "http://"+ChatActivity.IP+":5002/api/values/connectedusers";
        controller.listConnectedUsers(ruta,this,progressBar, new AsyncResponse() {
            @Override
            public void processFinish(Object o) {
                Gson gson = new Gson();
                Type listType = new TypeToken<ArrayList<String>>() {}.getType();
                List<String> list = gson.fromJson(o.toString(), listType);
                fillInListUsersView(list);
            }
        });
    }

    public void fillInListUsersView(List<String> stringList){
        for (int i = 0; i < stringList.size(); i++) {
            if(stringList.get(i).equals(ChatActivity.NICKNAME)){
                String aux = stringList.get(i).concat(" : Tú");
                stringList.set(i, aux);
            }
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,stringList);
        lstUsers.setAdapter(adapter);
    }
}
