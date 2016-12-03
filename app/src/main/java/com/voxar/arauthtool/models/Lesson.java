package com.voxar.arauthtool.models;

import java.io.Serializable;
import java.util.ArrayList;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by Geovane on 30/10/2016.
 */

public class Lesson extends RealmObject  implements Serializable {
    String name;
    String filePath; //path to image tracked
    RealmList<LessonItem> lessonItems;//contenc to be linked to image

    public void addLessonItem(LessonItem li) {
        lessonItems.add(li);
    }

    public Lesson() {
    }

    public Lesson(String name, String filePath) {
        this.name = name;
        this.filePath = filePath;
        this.lessonItems = new RealmList<LessonItem>();
    }

    public Lesson(String name, String filePath, ArrayList<LessonItem> lessonItems) {
        this.name = name;
        this.filePath = filePath;
        this.lessonItems = new RealmList<LessonItem>();
        for (int i = 0; i < lessonItems.size(); i++)
            this.lessonItems.add(lessonItems.get(i));
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

    public void setLessonItems(ArrayList<LessonItem> lessonItems) {
        for (int i = 0; i < lessonItems.size(); i++)
            this.lessonItems.add(lessonItems.get(i));
    }
}