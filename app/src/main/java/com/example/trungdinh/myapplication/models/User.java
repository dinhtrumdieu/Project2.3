package com.example.trungdinh.myapplication.models;

/**
 * Created by TrungDinh on 3/22/2017.
 */

public class User {

    private String id;
    private String name;
    private String images;

    public User() {

    }

    public User(String id, String name, String images) {
        this.id = id;
        this.name = name;
        this.images = images;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }
}
