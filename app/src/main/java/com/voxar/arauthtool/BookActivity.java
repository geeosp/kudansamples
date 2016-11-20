package com.voxar.arauthtool;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import eu.kudan.kudansamples.MyApplication;
import eu.kudan.kudansamples.R;

import static android.support.v7.widget.RecyclerView.*;

public class BookActivity extends AppCompatActivity {
    Book book;
    boolean editMode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);
        int bookId = getIntent().getIntExtra("book_id", -1);
        if(bookId==-1){
            book = new Book(getResources().getString(R.string.book_name_default));
            editMode = true;
        }else {
            book= MyApplication.getDatabase().getBook(bookId);
            editMode=false;
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        // Sets the Toolbar to act as the ActionBar for this Activity window.
        // Make sure the toolbar exists in the activity and is not null
        setSupportActionBar(toolbar);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler);
        recyclerView.setAdapter(new LessonAdapter());

       // RecyclerView.LayoutManager layout = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);


        LayoutManager layout = new GridLayoutManager(this,2);

        recyclerView.setLayoutManager(layout);

    }

    class LessonListHolder extends ViewHolder{
        TextView tv_lessonName;
        View view;

        public LessonListHolder(View itemView) {
            super(itemView);
            this.view = itemView;
            this.tv_lessonName = (TextView) itemView.findViewById(R.id.tv_lesson_name);
        }
    }

    class LessonAdapter extends RecyclerView.Adapter<LessonListHolder>{


        @Override
        public LessonListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view  = LayoutInflater.from(getApplicationContext()).inflate(R.layout.lesson_list_item_layout,parent, false);
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

