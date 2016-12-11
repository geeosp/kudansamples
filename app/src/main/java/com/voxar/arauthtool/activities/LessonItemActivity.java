package com.voxar.arauthtool.activities;

import android.content.ClipboardManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.OpenableColumns;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.nbsp.materialfilepicker.MaterialFilePicker;
import com.voxar.arauthtool.models.LessonItem;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import cn.refactor.lib.colordialog.ColorDialog;
import eu.kudan.kudansamples.R;
import ir.sohreco.androidfilechooser.ExternalStorageNotAvailableException;
import ir.sohreco.androidfilechooser.FileChooserDialog;

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
                et_lesson_item_content.setText(lessonItem.getPath());
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


    public void paste(View v) {
        lessonItem.setPath(((ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE)).getPrimaryClip().getItemAt(0).getText().toString());
        et_lesson_item_content.setText(lessonItem.getPath());
        lessonItem.setType(LessonItem.TYPE_URL);
    }

    public void pickFile(View v) {
        // openByPicker();
        //  openByRequest();


        FileChooserDialog.Builder builder = new FileChooserDialog.Builder(FileChooserDialog.ChooserType.FILE_CHOOSER, new FileChooserDialog.ChooserListener() {
            @Override
            public void onSelect(String path) {
                et_lesson_item_content.setText(path);
                lessonItem.setType(LessonItem.TYPE_FILE);
            }
        });
        try {
            builder.build().show(getSupportFragmentManager(), null);
        } catch (ExternalStorageNotAvailableException e) {
            e.printStackTrace();
        }

    }


    void openByPicker() {
        new MaterialFilePicker()
                .withActivity(this)
                .withRequestCode(REQUEST_PICK_FILE)
                // .withFilter(Pattern.compile(".*\\.txt$")) // Filtering files and directories by file name using regexp
                //  .withFilterDirectories(true) // Set directories filterable (false by default)
                //  .withHiddenFiles(true) // Show hidden files and folders
                .start();

    }

    void openByRequest() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");
        startActivityForResult(intent, REQUEST_PICK_FILE);

    }


    void save() {
        lessonItem.setName(et_lesson_item_name.getText().toString());
        String path = et_lesson_item_content.getText().toString();
        if (path.equals("")) {
            path = "http://";

        }


        lessonItem.setPath(et_lesson_item_content.getText().toString());
        setResult(LessonActivity.RESULT_LESSON_ITEM_EDITED);
        Limbo.setCurrentLessonItem(lessonItem);
        finish();
    }

    void delete() {
        ColorDialog dialog = new ColorDialog(this);
        dialog.setAnimationEnable(true);
        dialog.setTitle(getString(R.string.sure_want_to_delete));
        dialog.setContentText(getString(R.string.lesson_item_lost_forever));
        dialog.setPositiveListener(getString(R.string.ok), new ColorDialog.OnPositiveListener() {
            @Override
            public void onClick(ColorDialog dialog) {
                dialog.dismiss();
                setResult(LessonActivity.RESULT_LESSON_ITEM_DELETED);
                Limbo.setCurrentLessonItem(lessonItem);
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

    void cancel() {
        ColorDialog dialog = new ColorDialog(this);
        dialog.setAnimationEnable(true);
        dialog.setTitle(getString(R.string.sure_want_to_cancel));
        dialog.setContentText(getString(R.string.changes_will_not_be_saved));
        dialog.setPositiveListener(getString(R.string.ok), new ColorDialog.OnPositiveListener() {
            @Override
            public void onClick(ColorDialog dialog) {
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            switch (requestCode) {
                case REQUEST_PICK_FILE:
                    Uri returnUri = null;
                    if (data != null) {
                        returnUri = data.getData();
                        Log.d("LESSON_ITEM_ACTIVITY", "URI_tostring: " + returnUri.toString());
                        Log.d("LESSON_ITEM_ACTIVITY", "URI_getPath: " + returnUri.getPath());
                        Log.d("LESSON_ITEM_ACTIVITY", "URI_getEncodedPath: " + returnUri.getEncodedPath());
                        Log.d("LESSON_ITEM_ACTIVITY", "File MYMETYPE: " + getContentResolver().getType(returnUri));
                        Cursor returnCursor =
                                getContentResolver().query(returnUri, null, null, null, null);

                        int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                        int sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE);
                        returnCursor.moveToFirst();
                        Log.d("LESSON_ITEM_ACTIVITY", "File Name: " + returnCursor.getString(nameIndex));
                        Log.d("LESSON_ITEM_ACTIVITY", "File Size: " + returnCursor.getLong(sizeIndex));

                        et_lesson_item_content.setText(returnUri.toString());
                        lessonItem.setType(LessonItem.TYPE_FILE);


                    }
            }
        } catch (Exception e) {

        }
    }

    public boolean copyFileFromUri(Context context, Uri fileUri) {
        InputStream inputStream = null;
        OutputStream outputStream = null;

        try {
            ContentResolver content = context.getContentResolver();
            inputStream = content.openInputStream(fileUri);

            File root = Environment.getExternalStorageDirectory();
            if (root == null) {
                Log.d("LESSON_ITEM_ACTIVITY", "Failed to get root");
            }

            // create a directory
            File saveDirectory = new File(Environment.getExternalStorageDirectory() + File.separator + "directory_name" + File.separator);
            // create direcotory if it doesn't exists
            saveDirectory.mkdirs();

            outputStream = new FileOutputStream(saveDirectory + "filename.extension"); // filename.png, .mp3, .mp4 ...
            if (outputStream != null) {
            }

            byte[] buffer = new byte[1000];
            int bytesRead = 0;
            while ((bytesRead = inputStream.read(buffer, 0, buffer.length)) >= 0) {
                outputStream.write(buffer, 0, buffer.length);
            }
        } catch (Exception e) {
            Log.e("LESSON_ITEM_ACTIVITY", "Exception occurred " + e.getMessage());
        } finally {

        }
        return true;
    }


}
