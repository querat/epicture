package com.example.querat_g.epicture;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

/**
 * Created by bellia_n on 21/02/17.
 */

public class Connection extends MainActivity {
    private ImageView imgApi;
    private int       idImg;
    private Intent    intent;
    private EditText  txtUsername, txtPassword;
    private Button    btnConnect, btnCancel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.connection);
        loadClassVar();                                                                             // load class variables
        loadDrawer();                                                                               // load left drawer
        buttonManager();
    }

    /***
     * init and set class variables
     */
    private void loadClassVar(){
        intent = getIntent();
        imgApi = (ImageView) findViewById(R.id.imageViewApi);
        idImg = getResources().getIdentifier(intent.getStringExtra("api") + "_logo",
                "drawable", getPackageName());                                                      // get img res
        txtPassword = (EditText) findViewById(R.id.editViewConnectPassword);                        // get password EditText
        txtUsername = (EditText) findViewById(R.id.editViewConnectUsername);                        // get username EditText
        btnConnect = (Button) findViewById(R.id.buttonConnectOk);                                   // get ok button
        btnCancel = (Button) findViewById(R.id.buttonConnectCancel);                                // get cancel button
        imgApi.setImageResource(idImg);                                                             // set img res
    }

    private void buttonManager(){
        btnConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "ok button", Toast.LENGTH_LONG).show();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "cancel button", Toast.LENGTH_LONG).show();
            }
        });
    }
}