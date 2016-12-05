package com.voxar.arauthtool.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Geovane on 30/10/2016.
 */

public class Lesson extends RealmObject  {
    @Ignore
    static long lastId;
    @PrimaryKey
     long id;
    String name;
    String filePath; //path to image tracked
    RealmList<LessonItem> lessonItems;//conteudo to be linked to image

    public Lesson() {
        this.id = Math.max(lastId +1, System.currentTimeMillis());
        lastId = this.id+1;
    }

    public Lesson(String name, String filePath) {
        this.id = Math.max(lastId +1, System.currentTimeMillis());
        lastId = this.id+1;
        this.name = name;
        this.filePath = filePath;
        this.lessonItems = new RealmList<LessonItem>();

    }

  /*
    public Lesson(String name, String filePath, List<LessonItem> lessonItems) {
        this.name = name;
        this.filePath = filePath;
        this.lessonItems = new RealmList<LessonItem>();
        this.id = System.currentTimeMillis();
        for (int i = 0; i < lessonItems.size(); i++)
            this.lessonItems.add(lessonItems.get(i));
    }
*/
/*
    public Lesson set(Lesson lesson) {
        this.name = lesson.getName();
        this.filePath = lesson.getFilePath();
        this.lessonItems = new RealmList<LessonItem>();
        for (int i = 0; i < lesson.getLessonItems().size(); i++) {
            lessonItems.add(lesson.getLessonItems().get(i));
        }
        this.id = lesson.getId();
        return this;
    }
*/
    public long getId() {
        return id;
    }

    public void addLessonItem(LessonItem li) {
        lessonItems.add(li);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public RealmList<LessonItem> getLessonItems() {
        return lessonItems;
    }

    public void setLessonItems(List<LessonItem> lessonItems) {
        this.lessonItems= new RealmList<>();
        for (int i = 0; i < lessonItems.size(); i++)
            this.lessonItems.add(lessonItems.get(i));
    }
}