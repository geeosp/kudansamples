package com.voxar.arauthtool;

import java.io.Serializable;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Geovane on 30/10/2016.
 */

public class Book implements Serializable {
    private ArrayList<Lesson> lessons;
    private String name;
    private Date createdDate;
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
}

