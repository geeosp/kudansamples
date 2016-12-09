package com.voxar.arauthtool.tests;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.voxar.arauthtool.models.Book;

import java.util.Iterator;
import java.util.List;

import eu.kudan.kudansamples.R;
import io.realm.Realm;

public class FileTest extends AppCompatActivity {
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_test);
        textView = (TextView) findViewById(R.id.textView);


        Realm realm = Realm.getDefaultInstance();
        List<Book> books = realm.copyFromRealm(realm.where(Book.class).findAll());
        Iterator<Book> it = books.iterator();
        String json = "";
        while (it.hasNext()) {
            json += it.next().toJSON();
            if (it.hasNext()) {
                json += ",";
            }
        }


        Log.d("FILETEST", json);

        //Book b = new Gson().fromJson(json, Book.class);
        textView.setText(json);
    }
}
