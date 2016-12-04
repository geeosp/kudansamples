package com.voxar.arauthtool.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.voxar.arauthtool.models.Book;
import com.voxar.arauthtool.models.Lesson;

import eu.kudan.kudansamples.MyApplication;
import eu.kudan.kudansamples.R;

import static android.support.v7.widget.RecyclerView.INVISIBLE;
import static android.support.v7.widget.RecyclerView.LayoutManager;
import static android.support.v7.widget.RecyclerView.OnClickListener;
import static android.support.v7.widget.RecyclerView.VISIBLE;
import static android.support.v7.widget.RecyclerView.ViewHolder;

public class BookActivity extends AppCompatActivity {
    public static final int RESULT_EDITED = 1;
    public static final int REQUEST_VIEW_LESSON = 0;
    public static final int REQUEST_CREATE_LESSON = 1;

    Book book;
    boolean editMode;
    EditText et_bookName;
    //Realm realm;
    RecyclerView recyclerView;
    FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);
        long bookId = getIntent().getLongExtra("book_id", -1);

        floatingActionButton = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        et_bookName = (EditText) findViewById(R.id.et_book_name);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        recyclerView = (RecyclerView) findViewById(R.id.recycler);


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
        LayoutManager layout = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layout);
        updateViews();
    }

    void updateViews() {
        setTitle(book.getName());
        et_bookName.setText(book.getName());


        recyclerView.invalidate();
    }


    void setEditMode(boolean editMode) {
        this.editMode = editMode;
        if (editMode) {
            et_bookName.setVisibility(VISIBLE);
            et_bookName.setText(book.getName());
            floatingActionButton.setImageResource(R.drawable.ic_action_add);
            floatingActionButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    openLesson(REQUEST_CREATE_LESSON, null);
                }
            });
        } else {
            et_bookName.setVisibility(View.GONE);
            floatingActionButton.setImageResource(R.drawable.ic_action_play);
        }
        invalidateOptionsMenu();
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
            case R.id.menu_cancel:
                setEditMode(false);
                break;
            case R.id.menu_delete:
                delete();

                break;
            case android.R.id.home:
                this.finish();
        }
        return true;
    }

    void save() {
        book.setName(et_bookName.getText().toString());
        MyApplication.getDatabase().saveBook( book);
        setEditMode(false);
        updateViews();
    }

    void delete() {
        MyApplication.getDatabase().deleteBook(book.getId());
        finish();
    }


    void cancelEdit() {
        //realm.cancelTransaction();
        setEditMode(false);
    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    void playBook() {

    }

    void openLesson(int request, Lesson lesson) {
        Intent intent = new Intent(this, LessonActivity.class);
        intent.putExtra("request", request);
        if (request == REQUEST_VIEW_LESSON) {
            Limbo.setCurrentLesson(lesson);
        }
        startActivityForResult(intent, request);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (resultCode == RESULT_EDITED) {
            Lesson lesson = (Lesson) data.getExtras().get("lesson");
            switch (requestCode) {
                case REQUEST_CREATE_LESSON:
                    book.getLessons().add(lesson);

                    break;
                case REQUEST_VIEW_LESSON:
                    for (int i = 0; i < book.getLessons().size(); i++) {
                        if (book.getLessons().get(i).getId() == lesson.getId()) {
                            book.getLessons().set(i, lesson);
                        }
                    }

                    break;
            }
        }
    }


    class LessonListHolder extends ViewHolder {
        TextView tv_lessonName;
        View view;

        public LessonListHolder(View itemView) {
            super(itemView);
            this.view = itemView;
            this.tv_lessonName = (TextView) itemView.findViewById(R.id.tv_lesson_name);
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
                    Lesson n = new Lesson();
                    n.set(lesson);
                    openLesson(REQUEST_VIEW_LESSON, n);
                }
            });
        }

        @Override
        public int getItemCount() {
            return book.getLessons().size();
        }
    }
}

