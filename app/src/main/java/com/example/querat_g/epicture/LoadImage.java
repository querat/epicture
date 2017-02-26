package com.example.querat_g.epicture;

import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by bellia_n on 26/02/17.
 */

public class LoadImage extends AsyncTask<Void, Integer, ArrayList<ApiImages>> {
    ArrayList<ApiImages> img = new ArrayList<ApiImages>();

    protected void onPreExecute(){
        super.onPreExecute();

    }

    /***
     * Retrieve images uplaoded by user on website
     * @param params default parameters
     * @return list of images on website
     */
    protected ArrayList<ApiImages> doInBackground(Void... params) {

        img.add(new ApiImages(1,"IMG 1"));
        img.add(new ApiImages(2,"IMG 2"));
        img.add(new ApiImages(3,"IMG 3"));

        Log.d("Size: ", "" + img.size()); //It gives me the size equals to 3 -->Correct
        return img;

    }

    protected void onProgressUpdate(){

    }

    protected void onPostExecute(ArrayList<ApiImages> img){
    }

}
