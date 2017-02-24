package com.example.querat_g.epicture;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
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
    private Button    btnConnect;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.connection);
        intent = getIntent();
        imgApi = (ImageView) findViewById(R.id.imageViewApi);
        loadDrawer();
        idImg = getResources().getIdentifier(intent.getStringExtra("api") + "_logo", "drawable", getPackageName());
        imgApi.setImageResource(idImg);
    }
}
