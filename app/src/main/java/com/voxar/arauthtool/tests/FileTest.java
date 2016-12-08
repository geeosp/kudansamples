package com.voxar.arauthtool.tests;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import eu.kudan.kudansamples.R;
import io.realm.Realm;

public class FileTest extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_test);


        Realm.getDefaultInstance();


    }
}
