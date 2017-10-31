package com.example.juandavid.groupchat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.juandavid.groupchat.controllers.MainActivityController;
import com.example.juandavid.groupchat.util.enums.ServiceMethodEnum;
import com.example.juandavid.groupchat.util.interfaces.AsyncResponse;
import com.example.juandavid.groupchat.util.services.GenericService;

public class MainActivity extends AppCompatActivity {

    private EditText txtIp, txtNickName;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtIp = (EditText) findViewById(R.id.txtIp);
        txtNickName = (EditText) findViewById(R.id.txtNickname);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        txtNickName.setText(ChatActivity.NICKNAME);
    }

    public void connectTheIp(View view){
        if(txtIp.getText().toString().equals("")
                || txtNickName.getText().toString().equals("")){
            Toast.makeText(this, "Please, enter a IP address and your nickname",
                    Toast.LENGTH_SHORT).show();
        } else {
            String nickname = txtNickName.getText().toString();
            ChatActivity.IP = txtIp.getText().toString();
            String ruta = "http://"+ChatActivity.IP+":5002/api/values/connect";
            ChatActivity.NICKNAME = nickname;

            GenericService asyncTask = new GenericService(ruta, toJson(nickname), this,
                    progressBar, ServiceMethodEnum.POST.toString(), false,new AsyncResponse() {
                @Override
                public void processFinish(Object o) {
                    if(!(o.toString()).equals("")) {
                        Intent i = new Intent(getApplicationContext(), ChatActivity.class);
                        startActivity(i);
                    }
                }
            });
            Toast.makeText(this, "va a iniciar", Toast.LENGTH_LONG).show();
            asyncTask.execute();
        }
    }

    private String toJson(String str){
        String json = "{" +
                "'user': '" + str + "'" +
                "}";
        json = json.replace("'", "\"");
        return json;
    }
}
