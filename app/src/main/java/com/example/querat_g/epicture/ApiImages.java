package com.example.querat_g.epicture;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by bellia_n on 26/02/17.
 */

public class ApiImages {

    private int      id = 0;
    private String   name = null;
    private String   title = null;
    private Bitmap   image = null;
    private Boolean  favorite = false;

    public ApiImages(int _id, String _name, URL url) {
        this.id = _id;
        this.name = _name;
        try {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            this.image = BitmapFactory.decodeStream(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ApiImages(ApiImages img){
        this.id = img.getId();
        this.name = img.getName();
        this.image = img.getImage();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Bitmap getImage() {
        return image;
    }

    public String getTitle() {return title;}

    public Boolean getFavorite() {return favorite;}

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTitle(String title) {this.title = title;}

    public void setFavorite(Boolean favorite) {this.favorite = favorite;}




}
