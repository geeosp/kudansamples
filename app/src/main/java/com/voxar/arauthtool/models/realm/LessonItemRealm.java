package com.voxar.arauthtool.models.realm;

import java.io.Serializable;

import io.realm.RealmObject;

/**
 * Created by Geovane on 30/10/2016.
 */

public class LessonItemRealm extends RealmObject  implements Serializable {


    public static final  int TYPE_URL = 0;
    public static final  int TYPE_FILE = 1;


    public LessonItemRealm(){}
    public int  type;
    public String content;
    public String name;

    public LessonItemRealm(int type, String name, String content) {
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

    public int getType() {

        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
