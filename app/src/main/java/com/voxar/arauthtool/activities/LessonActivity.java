package com.voxar.arauthtool.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.widget.EditText;

import com.voxar.arauthtool.models.Lesson;

import eu.kudan.kudansamples.MyApplication;
import eu.kudan.kudansamples.R;

public class LessonActivity extends AppCompatActivity {

    Lesson lesson;
    boolean editMode;
    EditText et_lessonName;
    //Realm realm;
    RecyclerView recyclerView;
    FloatingActionButton floatingActionButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson);
        long lessonId = getIntent().getLongExtra("lesson_id", -1);
        int request = getIntent().getIntExtra("request", -1);


        switch (request) {
            case BookActivity.REQUEST_CREATE_LESSON:
                lesson = new Lesson();
                Limbo.setCurrentLesson(lesson);
             break;
            case BookActivity.REQUEST_VIEW_LESSON:
            lesson = Limbo.getCurrentLesson();
                break;
        }


        Log.e("geeo", "" + lesson.getId());


        et_lessonName = (EditText) findViewById(R.id.et_lesson_name);
        floatingActionButton = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recyclerView = (RecyclerView) findViewById(R.id.recycler);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (editMode) {
            getMenuInflater().inflate(R.menu.menu_lesson_edit, menu);
        } else {
            getMenuInflater().inflate(R.menu.menu_lesson_view, menu);
        }
        return true;
    }

}
