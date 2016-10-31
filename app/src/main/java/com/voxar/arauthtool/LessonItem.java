package com.voxar.arauthtool;

import java.io.Serializable;

/**
 * Created by Geovane on 30/10/2016.
 */

public class LessonItem implements Serializable {
    public enum LessonItemType{
        URL,
        VIDEO,
        PHOTO,
        FILE
    }
    public LessonItemType type;
    public String content;
    public String name;

    public LessonItem(LessonItemType type,String name,  String content) {
        this.type = type;
        this.content = content;
    this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LessonItemType getType() {

        return type;
    }

    public void setType(LessonItemType type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
