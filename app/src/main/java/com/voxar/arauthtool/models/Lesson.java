package com.voxar.arauthtool.models;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Geovane on 30/10/2016.
 */

public class Lesson implements Serializable{
    String name;
    String filePath; //path to image tracked
    ArrayList<LessonItem> lessonItems;//contenc to be linked to image
    public void addLessonItem(LessonItem li) {
        lessonItems.add(li);
    }

    public Lesson(String name, String filePath) {
        this.name = name;
        this.filePath = filePath;
        this.lessonItems =new ArrayList<>();
    }

    public Lesson(String name, String filePath, ArrayList<LessonItem> lessonItems) {
        this.name = name;
        this.filePath = filePath;
        this.lessonItems = lessonItems;
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

    public ArrayList<LessonItem> getLessonItems() {
        return lessonItems;
    }

    public void setLessonItems(ArrayList<LessonItem> lessonItems) {
        this.lessonItems = lessonItems;
    }
}