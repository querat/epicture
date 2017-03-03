package com.example.querat_g.epicture;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.Toast;

import java.util.ArrayList;

/***
 * Asynchronously upload a picture to user gallery of connected API
 */
public class UploadImage extends AsyncTask<Void, String, Boolean> {

    private final MainActivity       activity;
    private final ProgressDialog     dialog;
    private final String             path;
    private final String             title;
    private final String             extension;

    public UploadImage(MainActivity activity, String path){
        this.activity = activity;
        dialog = new ProgressDialog(activity);
        this.path = path;
        this.title = path.substring(path.lastIndexOf('/') + 1);
        this.extension = path.substring(path.lastIndexOf('.' + 1));
    }

    protected void onPreExecute(){
        super.onPreExecute();
        dialog.setMessage("Loading gallery");
        dialog.show();
    }
    @Override
    protected Boolean doInBackground(Void... voids) {

        //TODO upload here
        /*        try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }*/

        return true;
    }

    protected void onPostExecute(Boolean task){
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
        if (task){
            Toast.makeText(this.activity.getApplicationContext(), "Image uploaded successfully", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(this.activity.getApplicationContext(), "An error occurred during upload", Toast.LENGTH_SHORT).show();
        }
    }
}
