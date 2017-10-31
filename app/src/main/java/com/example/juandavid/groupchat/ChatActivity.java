package com.example.juandavid.groupchat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.juandavid.groupchat.controllers.ChatActivityController;
import com.example.juandavid.groupchat.util.interfaces.AsyncResponse;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    public static String IP = "";
    public static String NICKNAME = "";
    private EditText txtMessage;
    private ListView lstMessageList;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        txtMessage = (EditText) findViewById(R.id.txtMessage);
        lstMessageList = (ListView) findViewById(R.id.lstMessageList);
        progressBar = (ProgressBar) findViewById(R.id.progressBar2);
        progressBar.setVisibility(View.GONE);
        consumeServiceListMessages();
    }

    public void changeIpAddress(View view){
        IP = "";
        //Intent i = new Intent(this, MainActivity.class);
        //startActivity(i);
        finish();
    }

    public void sendMessage(View view){
        ChatActivityController controller = new ChatActivityController();
        String msg = txtMessage.getText().toString();
        String ruta = "http://"+IP+":5002/api/values/send";
        controller.sendMessage(ruta,msg,ChatActivity.NICKNAME,this,progressBar, new AsyncResponse() {
            @Override
            public void processFinish(Object o) {
                if((o.toString()).equals("")) {
                    Toast.makeText(getApplicationContext(), "Ocurrio algun error",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
        txtMessage.setText("");
    }

    public void disconnect(View view){
        ChatActivityController controller = new ChatActivityController();
        String ruta = "http://"+IP+":5002/api/values/disconnect";
        controller.disconnect(ruta,ChatActivity.NICKNAME,this,progressBar, new AsyncResponse() {
            @Override
            public void processFinish(Object o) {
                if((o.toString()).equals("")) {
                    Toast.makeText(getApplicationContext(), "Has sido desconectado",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
        Toast.makeText(getApplicationContext(), "Has sido desconectado",
                Toast.LENGTH_SHORT).show();
        finish();
    }

    public void seeConnectedUsers(View view){
        Intent i = new Intent(getApplicationContext(), ConnectedUsersActivity.class);
        startActivity(i);
    }

    public void consumeServiceListMessages(){
        ChatActivityController controller = new ChatActivityController();
        String ruta = "http://"+IP+":5002/api/values/messages";
        controller.listMessage(ruta,this,progressBar, new AsyncResponse() {
            @Override
            public void processFinish(Object o) {

            }
        });

    }

    public void fillInListView(String stringList){
        Gson gson = new Gson();
        Type listType = new TypeToken<ArrayList<String>>() {}.getType();
        List<String> list = gson.fromJson(stringList, listType);
        if(list.size() > lstMessageList.getCount()){
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1,list);
            lstMessageList.setAdapter(adapter);
            lstMessageList.setSelection(lstMessageList.getCount() -1);
        }
    }


}
