package com.voxar.arauthtool.models;

import java.io.Serializable;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Geovane on 30/10/2016.
 */

public class Book extends RealmObject  {
    @PrimaryKey
    long id;

    private RealmList<Lesson> lessons;
    private String name;
    private Date createdDate;

    public Book() {

        this.id = System.currentTimeMillis();
    }

    public Book(Book book) {
        setName(book.getName());
        setId(book.getId());
        createdDate = book.getCreatedDate();
        lessons = (RealmList<Lesson>) book.getLessons();
    }


    public Book(String name) {
        this.name = name;
        this.createdDate = new Date();
        this.lessons = new RealmList<Lesson>();
        this.id = System.currentTimeMillis();
    }


    public void setLessons(RealmList<Lesson> lessons) {
        this.lessons = lessons;
    }



    public void addLesson(Lesson l) {
        lessons.add(l);
    }



    public List<Lesson> getLessons() {
        return lessons;
    }

    public Lesson getLesson(long lessonId) {
        Iterator<Lesson> it = getLessons().iterator();
        Lesson l = null;
        while (it.hasNext()) {
            Lesson t = it.next();
            if (t.getId() == lessonId) {
                l = t;
            }
        }
        return l;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}

