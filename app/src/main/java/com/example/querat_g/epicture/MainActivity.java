package com.example.querat_g.epicture;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.SystemClock;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
@SuppressWarnings("deprecation")

public class MainActivity extends Activity{

    protected String[]              drawerItemsList;
    protected ListView              Drawer;
    protected DrawerLayout          drawerLayout;
    protected ActionBarDrawerToggle DrawerToggle;
    protected static int            Status;
    protected ArrayList<ApiImages>  imgs;
    protected Gallery               gallery;
    protected ProgressBar           pb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadDrawer();
        Status = 0;
        pb = (ProgressBar) findViewById(R.id.progressBar);
        pb.setVisibility(View.INVISIBLE);
//        spinner = new ProgressBar(this);
//        spinner.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Status != 0){                                                                           // user is connected to api service
            LoadImage ld = new LoadImage(this);
            ld.execute();
            imgs = new ArrayList<>(ld.img);
            gallery = (Gallery) findViewById(R.id.gallery1);
            gallery.setAdapter(new ImageAdapter(this));
            gallery.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                    ImageView imageView = (ImageView) findViewById(R.id.image1);
                    imageView.setImageBitmap(imgs.get(position).getImage());
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                    ImageView imageView = (ImageView) findViewById(R.id.image1);
                    imageView.setImageResource(R.drawable.appareil_photo);
                }
            });
        }
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
                this,
                drawerLayout,
                R.string.drawer_open,
                R.string.drawer_close
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

    /***
     * Adapter used by upper gallery
     */
    public class ImageAdapter extends BaseAdapter {
        private Context context;
        private int itemBackground;
        public ImageAdapter(Context c)
        {
            context = c;
            TypedArray a = obtainStyledAttributes(R.styleable.MyGallery);
            itemBackground = a.getResourceId(R.styleable.MyGallery_android_galleryItemBackground, 0);
            a.recycle();
        }
        // returns the number of images
        public int getCount() {
            return imgs.size();
        }
        // returns the ID of an item
        public Object getItem(int position) {
            return position;
        }
        // returns the ID of an item
        public long getItemId(int position) {
            return position;
        }
        // returns an ImageView view
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageView = new ImageView(context);
            imageView.setImageBitmap(imgs.get(position).getImage());
            imageView.setLayoutParams(new Gallery.LayoutParams(100, 100));
            imageView.setBackgroundResource(itemBackground);
            return imageView;
        }
    }
}