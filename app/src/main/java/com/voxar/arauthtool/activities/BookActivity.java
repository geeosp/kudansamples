package com.voxar.arauthtool.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import io.realm.Realm;

import static android.support.v7.widget.RecyclerView.*;

public class BookActivity extends AppCompatActivity {
    Book book;
    boolean editMode;
    EditText et_bookName;
    //Realm realm;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);
        long bookId = getIntent().getLongExtra("book_id", -1);
        Log.e("geeo", "" + bookId);
        et_bookName = (EditText) findViewById(R.id.et_book_name);
      //  realm = Realm.getDefaultInstance();


        if (bookId == -1) {
            book = new Book(getResources().getString(R.string.book_name_default));
            setEditMode(true)
            ;
        } else {
            book = MyApplication.getDatabase().getBook(bookId);
            setEditMode(false);
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        // Sets the Toolbar to act as the ActionBar for this Activity window.
        // Make sure the toolbar exists in the activity and is not null
        setSupportActionBar(toolbar);
        recyclerView = (RecyclerView) findViewById(R.id.recycler);
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
        //    realm.beginTransaction();
        } else {
            et_bookName.setVisibility(INVISIBLE);
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
        }
        return super.onOptionsItemSelected(item);
    }
    void save() {
        book.setName(et_bookName.getText().toString());


        MyApplication.getDatabase().saveBook(book.getId(), book);

        setEditMode(false);
        updateViews();
    }
    void cancelEdit() {
        //realm.cancelTransaction();
        setEditMode(false);
    }
    @Override
    protected void onStop() {
        super.onStop();

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
            Lesson lesson = book.getLessons().get(position);
            holder.tv_lessonName.setText(lesson.getName());
            holder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), LessonActivity.class);
                    intent.putExtra("book_id", book.getId());
                    intent.putExtra("lesson_id", position);
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return book.getLessons().size();
        }
    }


}

