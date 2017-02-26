package com.example.querat_g.epicture;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.InputStream;
import java.net.URL;

/**
 * Created by bellia_n on 26/02/17.
 */

public class ApiImages {

    private int     id;
    private String  name;
    private Bitmap  image;

    // TODO add image loading
    public ApiImages(int _id, String _name) {
        this.id = _id;
        this.name = _name;
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

    public void setName(String name) {
        this.name = name;
    }
}
