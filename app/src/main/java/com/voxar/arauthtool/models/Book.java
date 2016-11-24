package com.voxar.arauthtool.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Geovane on 30/10/2016.
 */

public class Book implements Serializable {
    private ArrayList<Lesson> lessons;
    private String name;
    private Date createdDate;
    long id;
    public void addLesson(Lesson l) {
        lessons.add(l);
    }
    public void removeLesson(int id){
        lessons.remove(id);
    }
    public Book(String name){
        this.name = name;
        this.createdDate = new Date();
        this.lessons = new ArrayList<Lesson>();
        this.id=System.currentTimeMillis();
    }

    public ArrayList<Lesson> getLessons() {
        return lessons;
    }

    public String getName() {
        return name;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}

