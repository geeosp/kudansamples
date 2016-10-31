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

    public LessonItem(LessonItemType type, String content) {
        this.type = type;
        this.content = content;
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
