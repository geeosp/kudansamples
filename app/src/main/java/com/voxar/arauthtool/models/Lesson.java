package com.voxar.arauthtool.models;

import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Geovane on 30/10/2016.
 */

public class Lesson extends RealmObject {
    @Ignore
    static long lastId;
    @PrimaryKey
    long id;
    String name;
    String filePath; //path to image tracked
    RealmList<LessonItem> lessonItems;//conteudo to be linked to image

    public Lesson() {
        this.id = Math.max(lastId + 1, System.currentTimeMillis());
        lastId = this.id + 1;
        this.lessonItems = new RealmList<LessonItem>();
        this.filePath = "";
        this.name = "";
    }

    public Lesson(String name, String filePath) {
        this.id = Math.max(lastId + 1, System.currentTimeMillis());
        lastId = this.id + 1;
        this.name = name;
        this.filePath = filePath;
        this.lessonItems = new RealmList<LessonItem>();

    }


    public long getId() {
        return id;
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
        this.lessonItems = new RealmList<>();
        for (int i = 0; i < lessonItems.size(); i++)
            this.lessonItems.add(lessonItems.get(i));
    }

    public LessonItem getLessonItem(long id) {
        for (int i = 0; i < lessonItems.size(); i++) {
            if (lessonItems.get(i).getId() == id) {
                return lessonItems.get(i);
            }
        }
        return null;
    }

    public void updateLessonItem(LessonItem item) {
        for (int i = 0; i < lessonItems.size(); i++) {
            if (lessonItems.get(i).getId() == item.getId()) {
                lessonItems.set(i, item);
            }
        }

    }

    public void deleteLessonItem(LessonItem item) {
        for (int i = 0; i < lessonItems.size(); i++) {
            if (lessonItems.get(i).getId() == item.getId()) {
                lessonItems.remove(i);
            }
        }
    }

    public void addLessonItem(LessonItem item) {
        lessonItems.add(item);
    }


}