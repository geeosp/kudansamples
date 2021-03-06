package com.voxar.arauthtool.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.FileProvider;
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
import com.bumptech.glide.signature.StringSignature;
import com.voxar.arauthtool.models.Book;
import com.voxar.arauthtool.models.Lesson;

import java.io.File;

import cn.refactor.lib.colordialog.ColorDialog;
import eu.kudan.kudansamples.MyApplication;
import eu.kudan.kudansamples.R;

import static android.support.v7.widget.RecyclerView.OnClickListener;
import static android.support.v7.widget.RecyclerView.VISIBLE;
import static android.support.v7.widget.RecyclerView.ViewHolder;

public class BookActivity extends AppCompatActivity {
    public static final int RESULT_EDITED = 1;
    public static final int RESULT_DELETED = 2;
    public static final int REQUEST_EDIT_LESSON = 0;
    public static final int REQUEST_CREATE_LESSON = 1;

    Book book;
    boolean editMode;
    EditText et_bookName;
    //Realm realm;
    RecyclerView recyclerView;
    FloatingActionButton floatingActionButton;
    View placeHolder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);
        long bookId = getIntent().getLongExtra("book_id", -1);

        floatingActionButton = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        et_bookName = (EditText) findViewById(R.id.et_book_name);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        placeHolder = findViewById(R.id.placeholder);

        if (bookId == -1) {
            book = new Book(getResources().getString(R.string.book_name_default));
            setEditMode(true)
            ;
        } else {
            book = MyApplication.getDatabase().getBook(bookId);
            setEditMode(false);
        }


        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recyclerView.setAdapter(new LessonAdapter());
        // RecyclerView.LayoutManager layout = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        RecyclerView.LayoutManager layout = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layout);
        updateViews();

        for (int i = 0; i < book.getLessons().size(); i++) {
            Log.e("book", book.getLessons().get(i).getName());
        }
    }

    void updateViews() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                et_bookName.setText(book.getName());
                if (editMode) {

                    setTitle(getResources().getString(R.string.edit_book));
                    et_bookName.setVisibility(VISIBLE);
                    et_bookName.setText(book.getName());
                    floatingActionButton.setImageResource(R.drawable.ic_action_add);
                    floatingActionButton.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            openLesson(REQUEST_CREATE_LESSON, -1);
                        }
                    });

                } else {
                    setTitle(book.getName());
                    et_bookName.setVisibility(View.GONE);
                    floatingActionButton.setImageResource(R.drawable.ic_action_play);
                    floatingActionButton.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            playBook();
                        }
                    });
                }
                invalidateOptionsMenu();
                recyclerView.setAdapter(new LessonAdapter());
                if (book.getLessons().size() > 0) {
                    placeHolder.setVisibility(View.GONE);
                } else {
                    placeHolder.setVisibility(View.VISIBLE);

                }
            }
        });

    }


    void setEditMode(boolean editMode) {
        this.editMode = editMode;
        updateViews();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (editMode) {
            getMenuInflater().inflate(R.menu.menu_book_edit, menu);
        } else {
            getMenuInflater().inflate(R.menu.menu_book_view, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_edit:
                setEditMode(true);
                break;
            case R.id.menu_save:
                save();
                break;
            case R.id.menu_discard:
                cancelEdit();
                break;
            case R.id.menu_delete:
                delete();

                break;
            case android.R.id.home:
                if (editMode) {
                    cancelEdit();
                } else {

                    this.finish();
                }
                break;
            case R.id.menu_share:

                exportBook();
                break;


        }
        return true;
    }

    void exportBook() {
        Log.d("SHARE", "Starting");
        final ProgressDialog progressDialog = ProgressDialog.show(this, getResources().getString(R.string.exporting_title), getResources().getString(R.string.exporting_message), true, false);

        Thread thread = new Thread() {
            @Override
            public void run() {
                File file = new File(MyApplication.getDatabase().getFilePathToExportBook(book.getId()));
                Uri uri = FileProvider.getUriForFile(getApplicationContext(), "eu.kudan.kudansamples.fileprovider", file);
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
                shareIntent.setType("*/" + getResources().getString(R.string.export_file_extension));
                startActivity(Intent.createChooser(shareIntent, getResources().getText(R.string.share_using)));
                progressDialog.dismiss();
            }
        };
        thread.start();

    }


    void save() {
        final ProgressDialog progressDialog = ProgressDialog.show(this, getResources().getString(R.string.saving_title), getResources().getString(R.string.saving_message), true, false);

        Thread mThread = new Thread() {
            @Override
            public void run() {
                book.setName(et_bookName.getText().toString());
                MyApplication.getDatabase().saveBook(book);
                setEditMode(false);
                updateViews();
                progressDialog.dismiss();
            }
        };
        mThread.start();


    }

    void delete() {
        ColorDialog dialog = new ColorDialog(this);
        dialog.setAnimationEnable(true);
        dialog.setTitle(getString(R.string.sure_want_to_delete));
        dialog.setContentText(getString(R.string.book_lost_forever));
        dialog.setPositiveListener(getString(R.string.ok), new ColorDialog.OnPositiveListener() {
            @Override
            public void onClick(ColorDialog dialog) {
                dialog.dismiss();
                MyApplication.getDatabase().deleteBook(book.getId());
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


    void cancelEdit() {
        ColorDialog dialog = new ColorDialog(this);
        dialog.setAnimationEnable(true);
        dialog.setTitle(getString(R.string.sure_want_to_cancel));
        dialog.setContentText(getString(R.string.changes_will_not_be_saved));
        dialog.setPositiveListener(getString(R.string.ok), new ColorDialog.OnPositiveListener() {
            @Override
            public void onClick(ColorDialog dialog) {
                dialog.dismiss();
                setEditMode(false);
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

    @Override
    protected void onStop() {
        super.onStop();

    }

    void playBook() {
        Intent intent = new Intent(this, TrackingActivityFinal.class);
        intent.putExtra("bookId", book.getId());
        startActivity(intent);
    }

    void openLesson(int request, long lessonId) {
        Intent intent = new Intent(this, LessonActivity.class);
        intent.putExtra("request", request);
        Limbo.setCurrentBook(book);


        switch (request) {
            case REQUEST_EDIT_LESSON:
                Limbo.setCurrentLesson(book.getLesson(lessonId));
                break;
            case REQUEST_CREATE_LESSON:

                break;
        }

        startActivityForResult(intent, request);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        Lesson lesson = Limbo.getCurrentLesson();
        Limbo.setCurrentLesson(null);
        switch (resultCode) {
            case RESULT_EDITED:
                switch (requestCode) {
                    case REQUEST_CREATE_LESSON:
                        Log.d("BOOK_ACTV_RES: ", "RESULT_CREATE");
                        book.getLessons().add(lesson);
                        break;
                    case REQUEST_EDIT_LESSON:
                        Log.d("BOOK_ACTV_RES: ", "RESULT_EDITED");
                        book.updateLesson(lesson);
                        break;
                }
                break;
            case RESULT_DELETED:
                Log.d("BOOK_ACTV_RES: ", "RESULT_DELETED");
                book.removeLesson(lesson.getId());
                break;
        }


        updateViews();

    }


    class LessonListHolder extends ViewHolder {
        TextView tv_lessonName;
        View view;
        ImageView iv_lesson_thumbnail;

        public LessonListHolder(View itemView) {
            super(itemView);
            this.view = itemView;
            this.tv_lessonName = (TextView) itemView.findViewById(R.id.tv_lesson_name);
            this.iv_lesson_thumbnail = (ImageView) itemView.findViewById(R.id.iv_lesson_thumbnail);
        }
    }

    class LessonAdapter extends RecyclerView.Adapter<LessonListHolder> {
        @Override
        public LessonListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.lesson_list_item_layout, parent, false);
            LessonListHolder holder = new LessonListHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(LessonListHolder holder, final int position) {
            final Lesson lesson = book.getLessons().get(position);
            holder.tv_lessonName.setText(lesson.getName());
            holder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (editMode) {

                        openLesson(REQUEST_EDIT_LESSON, lesson.getId());
                    }
                }
            });
            Glide
                    .with(getApplicationContext())
                    .load(lesson.getPath())
                    .centerCrop()
                    .crossFade()
                    .signature(new StringSignature(String.valueOf(System.currentTimeMillis())))
                    .into(holder.iv_lesson_thumbnail);
        }

        @Override
        public int getItemCount() {
            return book.getLessons().size();
        }


    }
}

