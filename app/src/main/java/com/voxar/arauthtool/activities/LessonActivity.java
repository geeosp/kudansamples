package com.voxar.arauthtool.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.nguyenhoanglam.imagepicker.activity.ImagePicker;
import com.nguyenhoanglam.imagepicker.activity.ImagePickerActivity;
import com.nguyenhoanglam.imagepicker.model.Image;
import com.voxar.arauthtool.models.Lesson;

import java.util.ArrayList;

import eu.kudan.kudansamples.R;

public class LessonActivity extends AppCompatActivity {

    public final static int REQUEST_CODE_PICKER = 1;
    Lesson lesson;
    EditText et_lessonName;
    ImageView iv_lesson_image;
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

iv_lesson_image=(ImageView) findViewById(R.id.iv_lesson_image);
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

    }

    void updateViews() {
        recyclerView.invalidate();Glide
                .with(getApplicationContext())
                .load(lesson.getFilePath())
                .centerCrop()
                .crossFade()
                .into(iv_lesson_image);
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

    public void pickImage(View v) {
        ImagePicker.create(this)
                .imageTitle("Tap to select") // image selection title
                .single() // single mode
                .showCamera(true) // show camera or not (true by default)
                .imageDirectory("ARTool") // directory name for captured image  ("Camera" folder by default)
                .start(REQUEST_CODE_PICKER); // start image picker activity with request code
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_PICKER:
                    if (data != null) {
                        ArrayList<Image> images = data.getParcelableArrayListExtra(ImagePickerActivity.INTENT_EXTRA_SELECTED_IMAGES);
                        Log.e("Image picked", images.get(0).getPath());
                        lesson.setFilePath(images.get(0).getPath());
                        updateViews();
                    }


            }
        }
    }
}




