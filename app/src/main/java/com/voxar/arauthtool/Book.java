package com.voxar.arauthtool;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Geovane on 30/10/2016.
 */

public class Book implements Serializable {
    private ArrayList<Lesson> lessons;
    private String name;
    private Date createDate;
    public void addLesson(Lesson l) {
        lessons.add(l);
    }


}

