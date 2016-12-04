package com.voxar.arauthtool.models;

import java.io.Serializable;
import java.io.SerializablePermission;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Geovane on 30/10/2016.
 */

public class Book extends RealmObject implements Serializable{
    @PrimaryKey
    long id;

   private RealmList<Lesson> lessons;
    private String name;
    private Date createdDate;
    public void addLesson(Lesson l) {
        lessons.add(l);
    }
    public void removeLesson(int id){
        lessons.remove(id);
    }


    public Book (){

        this.id=System.currentTimeMillis();
    }

    public Book (Book book){
        setName(book.getName());
        setId(book.getId());
        createdDate = book.getCreatedDate();
        lessons = (RealmList<Lesson>) book.getLessons();
    }


    public Book(String name){
        this.name = name;
        this.createdDate = new Date();
        this.lessons = new RealmList<Lesson>();
        this.id=System.currentTimeMillis();
    }

    public List<Lesson> getLessons() {
        return new RealmList<>();
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

