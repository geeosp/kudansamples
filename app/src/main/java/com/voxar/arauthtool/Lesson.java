package com.voxar.arauthtool;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Geovane on 30/10/2016.
 */

public class Lesson implements Serializable{
    String name;
    String filePath; //path to image tracked
    ArrayList<LessonItem> lessons;//contenc to be linked to image
    public void addLesson(LessonItem li) {
        lessons.add(li);
    }

    public Lesson(String name, String filePath) {
        this.name = name;
        this.filePath = filePath;
    }

    public Lesson(String name, String filePath, ArrayList<LessonItem> lessons) {
        this.name = name;
        this.filePath = filePath;
        this.lessons = lessons;
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

    public ArrayList<LessonItem> getLessons() {
        return lessons;
    }

    public void setLessons(ArrayList<LessonItem> lessons) {
        this.lessons = lessons;
    }
}