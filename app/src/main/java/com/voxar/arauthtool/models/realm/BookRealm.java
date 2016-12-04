package com.voxar.arauthtool.models.realm;

import com.voxar.arauthtool.models.Book;
import com.voxar.arauthtool.models.Lesson;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Geovane on 30/10/2016.
 */

public class BookRealm extends RealmObject implements Serializable{
    @PrimaryKey
    long id;

   private RealmList<LessonRealm> lessons;
    private String name;
    private Date createdDate;
    public void addLesson(LessonRealm l) {
        lessons.add(l);
    }
    public void removeLesson(int id){
        lessons.remove(id);
    }


    public BookRealm(){
        this.id=System.currentTimeMillis();
    }

    public Book toBook(){
        Book b =  new Book();
        b.setId(this.id);
        b.setName(this.name);


        return b;
    }


    public BookRealm(String name){
        this.name = name;
        this.createdDate = new Date();
        this.lessons = new RealmList<LessonRealm>();
        this.id=System.currentTimeMillis();
    }

    public ArrayList<Lesson> getLessons() {
        return new ArrayList<Lesson>();
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

