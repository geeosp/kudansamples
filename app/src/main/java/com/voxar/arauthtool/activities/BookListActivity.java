package com.voxar.arauthtool.activities;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.voxar.arauthtool.database.BookDatabase;
import com.voxar.arauthtool.models.Book;

import java.util.List;

import eu.kudan.kudansamples.MyApplication;
import eu.kudan.kudansamples.R;

public class BookListActivity extends AppCompatActivity {
    final int MY_PERMISSION_CAMERA = 1;
    final int MY_PERMISSION_READ_EXTERNAL_STORAGE = 2;
    final int MY_PERMISSION_WRITE_EXTERNAL_STORAGE = 3;
    BookDatabase database;
    List<Book> books;
    RecyclerView recyclerView;
    BookAdapter bookAdapter;

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

        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        bookAdapter = new BookAdapter();
        recyclerView.setAdapter(bookAdapter);

        RecyclerView.LayoutManager layout = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);


        //RecyclerView.LayoutManager layout = new GridLayoutManager(this,2);

        recyclerView.setLayoutManager(layout);
        permissionsRequest();

    }


    // Requests app permissions
    public void permissionsRequest() {

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.INTERNET)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.INTERNET, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 111);

        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 111: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    permissionsNotSelected();
                }
            }
        }
    }

    private void permissionsNotSelected() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Permissions Requred");
        builder.setMessage("Please enable the requested permissions in the app settings in order to use this demo app");
        builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
                System.exit(1);
            }
        });
        AlertDialog noInternet = builder.create();
        noInternet.show();
    }


    @Override
    protected void onPostResume() {
        super.onPostResume();
        recyclerView.invalidate();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
           /*
            case R.id.menu_create_book:
                Intent intent = new Intent(this, BookActivity.class);
                intent.putExtra("book_id", -1);
                startActivity(intent);

                break;
        */
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        books = database.loadBooks();

        bookAdapter.notifyDataSetChanged();
    }

    public void onPlusFloatingPressed(View v) {

        openCreateNewBook();
    }

    void openCreateNewBook() {
        Intent intent = new Intent(this, BookActivity.class);
        intent.putExtra("book_id", -1);
        startActivity(intent);

    }

    class BookListHolder extends RecyclerView.ViewHolder {
        TextView tv_bookName;
        View view;

        public BookListHolder(View itemView) {
            super(itemView);
            this.view = itemView;
            this.tv_bookName = (TextView) itemView.findViewById(R.id.tv_book_name);
        }
    }

    class BookAdapter extends RecyclerView.Adapter<BookListHolder> {


        @Override
        public BookListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.book_list_item_layout, parent, false);
            BookListHolder holder = new BookListHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(BookListHolder holder, final int position) {
            final Book book = books.get(position);
            holder.tv_bookName.setText(book.getName());
            holder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), BookActivity.class);
                    intent.putExtra("book_id", book.getId());
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