package com.voxar.arauthtool.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.voxar.arauthtool.models.Lesson;

import eu.kudan.kudansamples.R;

public class LessonActivity extends AppCompatActivity {

    Lesson lesson;

    EditText et_lessonName;
    //Realm realm;
    RecyclerView recyclerView;
    FloatingActionButton floatingActionButton;
    int request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson);
        long lessonId = getIntent().getLongExtra("lesson_id", -1);
        request = getIntent().getIntExtra("request", -1);


        switch (request) {
            case BookActivity.REQUEST_CREATE_LESSON:
                lesson = new Lesson();
                Limbo.setCurrentLesson(lesson);
                break;
            case BookActivity.REQUEST_EDIT_LESSON:
                lesson = Limbo.getCurrentLesson();
                break;
        }


        et_lessonName = (EditText) findViewById(R.id.et_lesson_name);
        floatingActionButton = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        setTitle(R.string.lesson_edit_title);
        et_lessonName.setText(lesson.getName());
        RecyclerView.LayoutManager layout = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layout);
        updateViews();
        updateViews();
    }

    void updateViews() {
        recyclerView.invalidate();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        switch (request) {
            case BookActivity.REQUEST_CREATE_LESSON:
                getMenuInflater().inflate(R.menu.menu_lesson_create, menu);
                break;
            case BookActivity.REQUEST_EDIT_LESSON:
                getMenuInflater().inflate(R.menu.menu_lesson_edit, menu);
                break;
        }


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_save:
                save();
                break;
            case R.id.menu_cancel:
                cancel();
                break;
            case R.id.menu_delete:
                delete();
                break;
            case android.R.id.home:
                cancel();
                break;

        }
        return true;

    }

    void save() {
        lesson.setName(et_lessonName.getText().toString());

        setResult(BookActivity.RESULT_EDITED);
        Limbo.setCurrentLesson(lesson);
        finish();
    }

    void delete() {
        setResult(BookActivity.RESULT_DELETED);
        Limbo.setCurrentLesson(lesson);
        finish();
    }

    void cancel() {

        finish();
    }

}




