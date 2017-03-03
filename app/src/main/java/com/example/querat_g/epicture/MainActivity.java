package com.example.querat_g.epicture;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
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
import android.widget.Button;
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

    protected String[]              drawerItemsList;                                                // Drawer connection list
    protected ListView              Drawer;                                                         // left-side drawer
    protected DrawerLayout          drawerLayout;
    protected ActionBarDrawerToggle DrawerToggle;
    protected static int            Status;                                                         // API connection status
    protected ArrayList<ApiImages>  imgs = new ArrayList<ApiImages>();                                      // list of api imgs
    protected Gallery               gallery;                                                        // upper imgs gallery
    protected FloatingActionButton  upButton;                                                       // upload button
    static final int API_CONNECTION = 1;
    static final int PHONE_BROWSING = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadDrawer();
        Status = 0;
        upButton = (FloatingActionButton) findViewById(R.id.uploadButton);
    }

    /***
     * Called when API-related activities are used
     * @param requestCode activity expected
     * @param resultCode  activity return code
     * @param data        contain data returned by the called activity
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {                 // used at end of each activityResult
        if (requestCode == API_CONNECTION) {                                                        // connection intent
            if(resultCode == Activity.RESULT_OK){
                Bundle dBundle = data.getExtras();
                Status = dBundle.getInt("service", 0);
                new LoadImage(this, Status).execute();                                              // launch asyncTask to load API gallery
                upButton.setVisibility(View.VISIBLE);
                upButton.setOnClickListener(new uploadListener());
            }
        }
        if (requestCode == PHONE_BROWSING && resultCode == RESULT_OK && null != data) {             // upload intent
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex);
                cursor.close();
                new UploadImage(this, picturePath).execute();                                       // launch asyncTask to upload pic to API gallery
                new LoadImage(this, Status).execute();                                              // launch asyncTask to load API gallery
                upButton.setVisibility(View.VISIBLE);
                upButton.setOnClickListener(new uploadListener());
            }
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
            startActivityForResult(intent, API_CONNECTION);
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
            }
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
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

    /***
     * Called by {@link LoadImage} in onPostExecute
     * Deep copy {@param list} to class variable imgs
     */
    public void setList(ArrayList<ApiImages> list) {
        imgs = new ArrayList<>(list);
        for (int i = 0; i + 1 < list.size(); i++){
            imgs.set(i, new ApiImages(list.get(i)));
        }
    }

    /***
     * Called by {@link LoadImage} in onPostExecute
     * Set and show image gallery from API
     */
    public void manageGallery(){
        gallery = (Gallery) findViewById(R.id.gallery1);
        gallery.setAdapter(new ImageAdapter(this));                                                 // use adapter to populate gallery
        gallery.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                ImageView imageView = (ImageView) findViewById(R.id.image1);
                imageView.setImageBitmap(imgs.get(position).getImage());
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        if (imgs.size() == 0){                                                                      // gallery is empty
            ImageView imageView = (ImageView) findViewById(R.id.image1);
            imageView.setImageResource(R.drawable.appareil_photo);
        }
    }

    /***
     * Called when upButton is pressed
     */
    private class uploadListener implements
    FloatingActionButton.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent i = new Intent(
                    Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

            startActivityForResult(i, PHONE_BROWSING);
        }
    }
}