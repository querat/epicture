package com.example.querat_g.epicture;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.SystemClock;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class MainActivity extends Activity{


    protected String[]              drawerItemsList;
    protected ListView              Drawer;
    protected DrawerLayout          drawerLayout;
    protected ActionBarDrawerToggle DrawerToggle;
    protected static int                   Status;
    protected ArrayList<ApiImages>  imgs = new ArrayList<ApiImages>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadDrawer();
        Status = 0;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Status != 0){                                                                           // user is connected to api service
            try {
                imgs = new LoadImage().execute().get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            Toast.makeText(getApplicationContext(), "list = ", Toast.LENGTH_LONG).show();
            Toast.makeText(getApplicationContext(), imgs.get(0).getName(), Toast.LENGTH_LONG).show();
            Toast.makeText(getApplicationContext(), imgs.get(1).getName(), Toast.LENGTH_LONG).show();
            Toast.makeText(getApplicationContext(), imgs.get(2).getName(), Toast.LENGTH_LONG).show();
        }
        else
            Toast.makeText(getApplicationContext(), "plop", Toast.LENGTH_LONG).show();
    }

        /***
         * manages left-side drawer interactions
         */
    protected class MyDrawerItemClickListener implements
            ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> adapter, View v, int pos, long id) {
            String clickedItem = (String) adapter.getAdapter().getItem(pos);
            drawerLayout.closeDrawer(Drawer);
            Intent intent = new Intent(getApplicationContext(), Connection.class);
            intent.putExtra("api", clickedItem.toLowerCase());
            startActivity(intent);
        }
    }

    /***
     * load left-side drawer
     */
    protected void loadDrawer(){
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerItemsList = getResources().getStringArray(R.array.items);
        Drawer = (ListView) findViewById(R.id.drawer);
        Drawer.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_items, drawerItemsList));

        Drawer.setOnItemClickListener(new MyDrawerItemClickListener());
        Drawer.requestDisallowInterceptTouchEvent(true);
        drawerManagement();                                                                         // load drawer management
        drawerLayout.addDrawerListener(DrawerToggle);
    }


    /***
     * manages drawer behavior
     */
    private void drawerManagement(){
        DrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                drawerLayout,         /* DrawerLayout object */
                R.string.drawer_open,  /* "open drawer" description */
                R.string.drawer_close  /* "close drawer" description */
        ) {
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                Toast.makeText(getApplicationContext(), "closed", Toast.LENGTH_LONG).show();
            }
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                Toast.makeText(getApplicationContext(), "opened", Toast.LENGTH_LONG).show();
                Drawer.bringToFront();
                drawerLayout.requestLayout();
            }
        };
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        DrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        DrawerToggle.onConfigurationChanged(newConfig);
    }

}