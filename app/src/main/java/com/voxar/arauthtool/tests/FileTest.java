package com.voxar.arauthtool.tests;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.TextView;

import com.voxar.arauthtool.models.Book;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.List;

import eu.kudan.kudansamples.R;
import io.realm.Realm;
import ir.sohreco.androidfilechooser.ExternalStorageNotAvailableException;
import ir.sohreco.androidfilechooser.FileChooserDialog;

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

    public void pickFile(View v) {
        FileChooserDialog.Builder builder = new FileChooserDialog.Builder(FileChooserDialog.ChooserType.FILE_CHOOSER, new FileChooserDialog.ChooserListener() {
            @Override
            public void onSelect(String path) {
                textView.setText(path);
            }
        });
        try {
            builder.build().show(getSupportFragmentManager(), null);
        } catch (ExternalStorageNotAvailableException e) {
            e.printStackTrace();
        }
    }

    public void copy(View v) {
        String path = textView.getText().toString();

        String extension = path.substring(path.lastIndexOf('.'));
        Log.d("SavingBook", "lesson Extension: " + extension);
        File oldFile = new File(path);
        File newFile = new File(getApplicationContext().getFilesDir(), "" + System.currentTimeMillis() + extension);
        String newPath = newFile.getPath();
        Log.d("SavingBook", "newFilePath: " + newPath);
        try {
            if (newFile.exists()) {
                newFile.delete();
                newFile.createNewFile();

            }
            copy(oldFile, newFile);
            Log.e("SavingBook", "Sucessfull" + newFile.getPath());
            textView.setText(newFile.getPath());
        } catch (IOException e) {
            Log.e("SavingBook", "Not Sucessfull" + e.getMessage());
        }


    }


    void copy(File src, File dst) throws IOException {
        InputStream in = new FileInputStream(src);
        OutputStream out = new FileOutputStream(dst);

        // Transfer bytes from in to out
        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        in.close();
        out.close();
    }

    public void fileToJson(View v) {
        String path = textView.getText().toString();
        File file = new File(path);
        try {
            byte[] bytes = fileToByteArray(file);
            String encodedFile = Base64.encodeToString(bytes, Base64.DEFAULT);
            textView.setText(encodedFile);

        } catch (IOException ioe) {
            Log.e("Exception", ioe.getMessage());
        }

    }

    public void jsonToFile(View v) {
        String encoded = textView.getText().toString();
        String path = getFilesDir().getPath() + "/" + System.currentTimeMillis() + ".jpg";
        File s = Base64ToFile(textView.getText().toString(), path);
        textView.setText(s.getPath());


    }


    byte[] fileToByteArray(File file) throws IOException {
        return org.apache.commons.io.FileUtils.readFileToByteArray(file);
    }

    File Base64ToFile(String encoded, String fullPath) {
        encoded = textView.getText().toString();
        byte[] data = Base64.decode(encoded, Base64.DEFAULT);
        File file = new File(fullPath);
        try {
            org.apache.commons.io.FileUtils.writeByteArrayToFile(file, data);
        } catch (IOException ioe) {
            Log.e("Exception", ioe.getMessage());
        }


        return file;
    }




    public void openInternFile(View v){
        /*
        String path= textView.getText().toString();
        File temp_file=new File(path);
        Intent intent = new Intent();
        intent.setAction(android.content.Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setDataAndType(Uri.fromFile(temp_file),getMimeType(temp_file.getAbsolutePath()));
        startActivity(intent);*/

        String path= textView.getText().toString();
        File temp_file=new File(path);
        Uri contentUri = FileProvider.getUriForFile(getApplicationContext(), "eu.kudan.kudansamples.fileprovider", temp_file);
        Log.d("Content", contentUri.toString());

        Intent intent = new Intent(Intent.ACTION_VIEW, contentUri);

        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(intent);



    }
    private String getMimeType(String url)
    {
        String parts[]=url.split("\\.");
        String extension=parts[parts.length-1];
        String type = null;
        if (extension != null) {
            MimeTypeMap mime = MimeTypeMap.getSingleton();
            type = mime.getMimeTypeFromExtension(extension);
        }
        return type;
    }


}
