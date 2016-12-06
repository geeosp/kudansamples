package com.voxar.arauthtool.activities;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.voxar.arauthtool.models.LessonItem;

import eu.kudan.kudansamples.R;

public class LessonItemActivity extends AppCompatActivity {
    final static int REQUEST_PICK_FILE = 1;
    int request;
    LessonItem lessonItem;
    Button bt_copy;
    Button bt_pick;
    EditText et_lesson_item_name;
    EditText et_lesson_item_content;
    // Spinner sp_choose_lesson_item_type;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson_item);
        bt_copy = (Button) findViewById(R.id.bt_copy_clipboard);
        bt_pick = (Button) findViewById(R.id.bt_pick_file);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(R.string.edit_lesson_item);
        et_lesson_item_name = (EditText) findViewById(R.id.et_lesson_item_name);

        et_lesson_item_content = (EditText) findViewById(R.id.et_lesson_item_content);
        request = getIntent().getIntExtra("request", -1);
        switch (request) {
            case LessonActivity.REQUEST_CREATE_LESSON_ITEM:
                lessonItem = new LessonItem();
                Limbo.setCurrentLessonItem(lessonItem);
                break;
            case LessonActivity.REQUEST_EDIT_LESSON_ITEM:
                lessonItem = Limbo.getCurrentLessonItem();
                et_lesson_item_content.setText(lessonItem.getContent());
                et_lesson_item_name.setText(lessonItem.getName());


                break;
        }
       /*
        sp_choose_lesson_item_type = (Spinner) findViewById(R.id.sp_choose_lesson_item_type);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
               R.array.list_item_types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_choose_lesson_item_type.setAdapter(adapter);
        sp_choose_lesson_item_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
               switch (i){
                   case 0://url
                       bt_pick.setVisibility(View.GONE);
                       bt_copy.setVisibility(View.VISIBLE);
                       break;

                   case 1://file
                       bt_pick.setVisibility(View.VISIBLE);
                       bt_copy.setVisibility(View.GONE);
                       break;


               }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

*/


        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        switch (request) {
            case LessonActivity.REQUEST_CREATE_LESSON_ITEM:
                getMenuInflater().inflate(R.menu.menu_lesson_item_create, menu);
                break;
            case LessonActivity.REQUEST_EDIT_LESSON_ITEM:
                getMenuInflater().inflate(R.menu.menu_lesson_item_edit, menu);
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


    public void paste(View v) {
        lessonItem.setContent(((ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE)).getPrimaryClip().getItemAt(0).getText().toString());
        et_lesson_item_content.setText(lessonItem.getContent());
        lessonItem.setType(LessonItem.TYPE_URL);
    }

    public void pickFile(View v) {
      /*
        new MaterialFilePicker()
                .withActivity(this)
                .withRequestCode(REQUEST_PICK_FILE)
                // .withFilter(Pattern.compile(".*\\.txt$")) // Filtering files and directories by file name using regexp
                //  .withFilterDirectories(true) // Set directories filterable (false by default)
                //  .withHiddenFiles(true) // Show hidden files and folders
                .start();
                */
        // ACTION_OPEN_DOCUMENT is the intent to choose a file via the system's file
        // browser.
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);

        // Filter to only show results that can be "opened", such as a
        // file (as opposed to a list of contacts or timezones)
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        // Filter to show only images, using the image MIME data type.
        // If one wanted to search for ogg vorbis files, the type would be "audio/ogg".
        // To search for all documents available via installed storage providers,
        // it would be "*/*".
        intent.setType("*/*");

        startActivityForResult(intent, REQUEST_PICK_FILE);


    }

    void save() {
        lessonItem.setName(et_lesson_item_name.getText().toString());
        lessonItem.setContent(et_lesson_item_content.getText().toString());
        setResult(LessonActivity.RESULT_LESSON_ITEM_EDITED);
        Limbo.setCurrentLessonItem(lessonItem);
        finish();
    }

    void delete() {
        setResult(LessonActivity.RESULT_LESSON_ITEM_DELETED);
        Limbo.setCurrentLessonItem(lessonItem);
        finish();
    }

    void cancel() {
        Limbo.setCurrentLesson(null);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_PICK_FILE:
                Uri uri = null;
                if (data != null) {
                    uri = data.getData();
                    Log.e("TAG", "Uri: " + uri.toString());
                    Log.e("TAG", "Uri: " + uri.getPath());
                    et_lesson_item_content.setText(uri.toString());
                    lessonItem.setType(LessonItem.TYPE_FILE);

                }
        }
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("LessonItem Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }


}
