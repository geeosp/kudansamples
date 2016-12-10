package com.voxar.arauthtool.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Geovane on 30/10/2016.
 */

public class LessonItem extends RealmObject {
    public static final int TYPE_URL = 0;
    public static final int TYPE_FILE = 1;
    @PrimaryKey
    long id;
    int type;
    String name;


    //    String content;
    String fileName;
    String fileMIMEType;
    String path;


    public LessonItem() {
        this.id = System.currentTimeMillis();
    }


    public LessonItem(int type, String name, String content) {
        this.type = type;
        this.path = content;
        if (name == null) name = "";

        this.name = name;
        this.id = System.currentTimeMillis();
    }

    public long getId() {
        return id;
    }

    public String getName() {
        if (this.name == null)
            this.name = "";
        return name;
    }

    public void setName(String name) {
        if (this.name == null) this.name = "";
        this.name = name;
    }

    public int getType() {

        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getPath() {
        if (this.path == null) {
            this.path = "";
        }
        return path;
    }

    public void setPath(String content) {
        if (content == null) {
            content = "";
        }
        this.path = content;
    }
}
