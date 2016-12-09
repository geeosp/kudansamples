package com.voxar.arauthtool.models;

import com.google.gson.Gson;

import java.util.Date;
import java.util.Iterator;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Geovane on 30/10/2016.
 */

public class Book extends RealmObject {

    @Ignore
    static long lastId;


    @PrimaryKey
    long id;

    private RealmList<Lesson> lessons;
    private String name;
    private Date createdDate;

    public Book() {
        this.id = Math.max(lastId + 1, System.currentTimeMillis());
        lastId = this.id + 1;
        this.name = "";
    }


    public Book(String name) {
        this.id = Math.max(lastId + 1, System.currentTimeMillis());
        lastId = this.id + 1;
        this.name = name;
        this.createdDate = new Date();
        this.lessons = new RealmList<Lesson>();
        this.id = System.currentTimeMillis();
    }

    public void addLesson(Lesson l) {
        lessons.add(l);
    }

    public void removeLesson(long lessonId) {
        for (int i = 0; i < lessons.size(); i++) {
            if (lessons.get(i).getId() == lessonId) {
                lessons.remove(i);
            }
        }
    }

    public void updateLesson(Lesson lesson) {

        for (int i = 0; i < lessons.size(); i++) {
            if (lessons.get(i).getId() == lesson.getId()) {
                lessons.set(i, lesson);
            }
        }
    }

    public RealmList<Lesson> getLessons() {
        return lessons;
    }

    public void setLessons(RealmList<Lesson> lessons) {
        this.lessons = lessons;
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

    public String toJSON() {
        return new Gson().toJson(this);


    }
}

