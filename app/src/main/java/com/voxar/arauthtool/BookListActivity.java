package com.voxar.arauthtool;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import java.util.ArrayList;

import eu.kudan.kudansamples.MyApplication;
import eu.kudan.kudansamples.R;

public class BookListActivity extends AppCompatActivity {
    BookDatabase database;
    ArrayList<Book> books;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list);
        // Find the toolbar view inside the activity layout
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        // Sets the Toolbar to act as the ActionBar for this Activity window.
        // Make sure the toolbar exists in the activity and is not null
        setSupportActionBar(toolbar);

        database = MyApplication.getDatabase();
        books = database.loadBooks();

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler);
        recyclerView.setAdapter(new BookAdapter());

        RecyclerView.LayoutManager layout = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);


        //RecyclerView.LayoutManager layout = new GridLayoutManager(this,2);

        recyclerView.setLayoutManager(layout);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_create_book:
                Intent intent = new Intent(this, EditBookActivity.class);
                startActivity(intent);

                break;
        }


        return super.onOptionsItemSelected(item);
    }








    class BookListHolder extends RecyclerView.ViewHolder{
        TextView tv_bookName;
        View view;

        public BookListHolder(View itemView) {
            super(itemView);
        this.view = itemView;
        this.    tv_bookName = (TextView) itemView.findViewById(R.id.tv_book_name);
        }
    }

    class BookAdapter extends RecyclerView.Adapter<BookListHolder>{


        @Override
        public BookListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view  = LayoutInflater.from(getApplicationContext()).inflate(R.layout.book_list_item_layout,parent, false);
            BookListHolder holder = new BookListHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(BookListHolder holder, final int position) {
            Book book = books.get(position);
            holder.tv_bookName.setText(book.getName());
            holder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), BookActivity.class);
                    intent.putExtra("book_id", position);
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return books.size();
        }
    }
}