package com.voxar.arauthtool.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
//import com.bumptech.glide.signature.StringSignature;
import com.nguyenhoanglam.imagepicker.activity.ImagePicker;
import com.nguyenhoanglam.imagepicker.activity.ImagePickerActivity;
import com.nguyenhoanglam.imagepicker.model.Image;
import com.voxar.arauthtool.R;
import com.voxar.arauthtool.models.Lesson;
import com.voxar.arauthtool.models.LessonItem;

import java.util.ArrayList;

import cn.refactor.lib.colordialog.ColorDialog;


public class LessonActivity extends AppCompatActivity {

    public final static int REQUEST_CODE_PICKER = 1;
    public final static int REQUEST_CREATE_LESSON_ITEM = 2;
    public final static int REQUEST_EDIT_LESSON_ITEM = 3;
    public final static int RESULT_LESSON_ITEM_EDITED = 4;
    public final static int RESULT_LESSON_ITEM_DELETED = 5;
    Lesson lesson;
    EditText et_lessonName;
    ImageView iv_lesson_image;
    //Realm realm;
    RecyclerView recyclerView;
    FloatingActionButton floatingActionButton;
    int request;
    View placeHolder;
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

        iv_lesson_image = (ImageView) findViewById(R.id.iv_lesson_image);
        et_lessonName = (EditText) findViewById(R.id.et_lesson_name);
        floatingActionButton = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createLessonItem(floatingActionButton);
            }
        });
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        setTitle(R.string.lesson_edit_title);
        et_lessonName.setText(lesson.getName());
        RecyclerView.LayoutManager layout = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layout);
        recyclerView.setAdapter(new LessonItemAdapter());
        placeHolder = findViewById(R.id.placeholder);
        updateViews();

    }

    void updateViews() {

        recyclerView.setAdapter(new LessonItemAdapter());
        if (lesson.getLessonItems().size() > 0) {
            placeHolder.setVisibility(View.GONE);
        } else {
            placeHolder.setVisibility(View.VISIBLE);
        }
        Glide
                .with(getApplicationContext())
                .load(lesson.getPath())
          //      .centerCrop()
            //    .crossFade()
              //  .signature(new StringSignature(String.valueOf(System.currentTimeMillis())))
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
            case R.id.menu_discard:
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
        ColorDialog dialog = new ColorDialog(this);
        dialog.setAnimationEnable(true);
        dialog.setTitle(getString(R.string.sure_want_to_delete));
        dialog.setContentText(getString(R.string.lesson_lost_forever));
        dialog.setPositiveListener(getString(R.string.ok), new ColorDialog.OnPositiveListener() {
            @Override
            public void onClick(ColorDialog dialog) {
                dialog.dismiss();
                setResult(BookActivity.RESULT_DELETED);
                Limbo.setCurrentLesson(lesson);
                finish();
            }
        });
        dialog.setNegativeListener(getString(R.string.cancel), new ColorDialog.OnNegativeListener() {
            @Override
            public void onClick(ColorDialog dialog) {
                dialog.dismiss();
            }
        });
        //    dialog.setCancelable(true);
        dialog.show();

    }

    void cancel() {
        ColorDialog dialog = new ColorDialog(this);
        dialog.setAnimationEnable(true);
        dialog.setTitle(getString(R.string.sure_want_to_cancel));
        dialog.setContentText(getString(R.string.changes_will_not_be_saved));
        dialog.setPositiveListener(getString(R.string.ok), new ColorDialog.OnPositiveListener() {
            @Override
            public void onClick(ColorDialog dialog) {
                dialog.show();
                Limbo.setCurrentLesson(null);
                finish();
            }
        });
        dialog.setNegativeListener(getString(R.string.cancel), new ColorDialog.OnNegativeListener() {
            @Override
            public void onClick(ColorDialog dialog) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void createLessonItem(View v) {

        openLessonItem(REQUEST_CREATE_LESSON_ITEM, -1);

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
        LessonItem item = Limbo.getCurrentLessonItem();
        switch (resultCode) {
            case RESULT_OK:
                switch (requestCode) {
                    case REQUEST_CODE_PICKER:
                        if (data != null) {
                            ArrayList<Image> images = data.getParcelableArrayListExtra(ImagePickerActivity.INTENT_EXTRA_SELECTED_IMAGES);
                            Log.e("Image picked", images.get(0).getPath());
                            lesson.setPath(images.get(0).getPath());
                            updateViews();
                        }
                        break;
                }
                break;
            case RESULT_LESSON_ITEM_DELETED:
                Log.e("DELETED", "DELETED");
                lesson.deleteLessonItem(item);
                break;
            case RESULT_LESSON_ITEM_EDITED:
                switch (requestCode) {
                    case REQUEST_CREATE_LESSON_ITEM:
                        lesson.addLessonItem(item);
                        break;
                    case REQUEST_EDIT_LESSON_ITEM:
                        lesson.updateLessonItem(item);
                        break;
                }

        }
        updateViews();
    }

    void openLessonItem(int request, long lessonItemId) {
        Intent intent = new Intent(this, LessonItemActivity.class);
        intent.putExtra("request", request);
        Limbo.setCurrentLessonItem(lesson.getLessonItem(lessonItemId));
        switch (request) {
            case REQUEST_EDIT_LESSON_ITEM:
                Limbo.setCurrentLessonItem(lesson.getLessonItem(lessonItemId));
                break;
            case REQUEST_CREATE_LESSON_ITEM:

                break;
        }

        startActivityForResult(intent, request);

    }


    class LessonItemListHolder extends RecyclerView.ViewHolder {
        TextView tv_lessonItemName;
        View view;


        public LessonItemListHolder(View itemView) {
            super(itemView);
            this.view = itemView;
            this.tv_lessonItemName = (TextView) itemView.findViewById(R.id.tv_lesson_item_name);
        }
    }

    class LessonItemAdapter extends RecyclerView.Adapter<LessonItemListHolder> {
        @Override
        public LessonItemListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.lesson_item_list_item_layout, parent, false);
            LessonItemListHolder holder = new LessonItemListHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(LessonItemListHolder holder, final int position) {
            final LessonItem lessonItem = lesson.getLessonItems().get(position);
            holder.tv_lessonItemName.setText(lessonItem.getName());
            holder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    openLessonItem(REQUEST_EDIT_LESSON_ITEM, lessonItem.getId());

                }
            });

        }

        @Override
        public int getItemCount() {
            lesson.getLessonItems();
            return lesson.getLessonItems().size();
        }


    }


}




