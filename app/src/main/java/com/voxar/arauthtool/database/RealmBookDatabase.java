package com.voxar.arauthtool.database;

import android.content.Context;
import android.util.Base64;
import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.voxar.arauthtool.models.Book;
import com.voxar.arauthtool.models.Lesson;
import com.voxar.arauthtool.models.LessonItem;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Iterator;
import java.util.List;

import eu.kudan.kudansamples.R;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by geeo on 03/12/16.
 */

public class RealmBookDatabase extends BookDatabase {

    private static RealmBookDatabase instance;
    private static Context ctx;

    private RealmBookDatabase() {
    }

    public static RealmBookDatabase getInstance(Context aplicationCtx) {
        if (instance == null) {
            instance = new RealmBookDatabase();
            //   instance.deleteAll();
            //  prepopulate();
        }
        ctx = aplicationCtx;
        return instance;
    }

    static void prepopulate() {
        if (instance.loadBooks().size() < 5) {
            for (int i = 0; i < 5; i++) {
                Book b = new Book("Book " + i);
                instance.saveBook(b);
            }
        }
    }

    public static String convertStreamToString(InputStream is) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            sb.append(line).append("\n");
        }
        reader.close();
        return sb.toString();
    }

    public static String getStringFromFile(String filePath) throws Exception {
        File fl = new File(filePath);
        FileInputStream fin = new FileInputStream(fl);
        String ret = convertStreamToString(fin);
        //Make sure you close all streams.
        fin.close();
        return ret;
    }

    void deleteAll() {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.deleteAll();
        realm.commitTransaction();
    }

    @Override
    public void deleteBook(long bookId) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        RealmResults<Book> result = realm.where(Book.class).equalTo("id", bookId).findAll();
        result.deleteAllFromRealm();
        realm.commitTransaction();
    }

    @Override
    public List<Book> loadBooks() {
        Realm realm = Realm.getDefaultInstance();
        final RealmResults<Book> books = realm.where(Book.class).findAll();
        return books;
    }

    @Override
    public Book getBook(long id) {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<Book> book = realm.where(Book.class).equalTo("id", id).findAll();
        return realm.copyFromRealm(book.first());
        //return new Book(book.first());
    }

    @Override
    public void saveBook(Book book) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        Log.d("DatabaseBookLessonCount", "" + book.getLessons().size());
        for (int l = 0; l < book.getLessons().size(); l++) {
            //para cada liçao, salve a
            Lesson lesson = book.getLessons().get(l);
            book.updateLesson(prepareLessonToSave(lesson));
        }


        realm.insertOrUpdate(book);
        realm.commitTransaction();
    }

    public String getFilePathToExportBook(long bookId) {
        Book b = getBook(bookId);

        String path = ctx.getExternalFilesDir(null) + File.separator + b.getName() + ctx.getResources().getString(R.string.export_file_extension);
        final File file = new File(path);
        String data = bookToJsonElement(b).toString();
        // Save your stream, don't forget to flush() it before closing it.
        try {
            if (file.exists()) file.delete();
            file.createNewFile();
            FileOutputStream fOut = new FileOutputStream(file);
            OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
            myOutWriter.append(data);

            myOutWriter.close();

            fOut.flush();
            fOut.close();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
        return path;
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

    Lesson prepareLessonToSave(Lesson lesson) {
        String path = lesson.getPath();
        if (!path.contains("http")) {//is a file
            lesson.setPath(copyFileToInternStorage(path, lesson.getId()));
        }
        for (int i = 0; i < lesson.getLessonItems().size(); i++) {
            LessonItem item = prepareLessonItemToSave(lesson.getLessonItems().get(i));
            lesson.updateLessonItem(item);
        }

        return lesson;
    }

    LessonItem prepareLessonItemToSave(LessonItem lessonItem) {
        switch (lessonItem.getType()) {
            case LessonItem.TYPE_FILE:
                String path = lessonItem.getPath();
                lessonItem.setPath(copyFileToInternStorage(path, lessonItem.getId()));
                break;
        }
        return lessonItem;
    }

    String copyFileToInternStorage(String currPath, long fileId) {
        String extension = currPath.substring(currPath.lastIndexOf('.'));
        Log.d("COPYING_FILE", "lesson Extension: " + extension);
        File oldFile = new File(currPath);
        File newFile = new File(ctx.getFilesDir(), "" + fileId + extension);
        String newPath = newFile.getPath();
        if (!currPath.equals(newPath)) {
            Log.d("COPYING_FILE", "newFilePath: " + newPath);
            try {
                copy(oldFile, newFile);
            } catch (IOException e) {
                Log.e("COPYING_FILE", "Not Sucessfull" + e.getMessage());
            }
        }
        return newPath;
    }

    JsonElement lessonToJsonElement(Lesson lesson) {
        JsonObject json = new JsonObject();
        json.addProperty("id", lesson.getId());
        json.addProperty("name", lesson.getName());
        json.addProperty("extension", lesson.getExtension());
        String path = fileToJson(lesson.getPath());
        json.addProperty("path", path);
        JsonArray lessonItems = new JsonArray();
        Iterator<LessonItem> it = lesson.getLessonItems().iterator();
        while (it.hasNext()) {
            lessonItems.add(lessonItemToJsonElement(it.next()));
        }
        json.add("lessonItems", lessonItems);

        return json;
    }

    public JsonElement bookToJsonElement(Book book) {
        JsonObject json = new JsonObject();
        Iterator<Lesson> it = book.getLessons().iterator();
        JsonArray lessons = new JsonArray();
        while (it.hasNext()) {
            lessons.add(lessonToJsonElement(it.next()));
        }
        json.addProperty("id", book.getId());
        json.addProperty("name", book.getName());
        json.add("lessons", lessons);

        return json;

    }

    JsonElement lessonItemToJsonElement(LessonItem lessonItem) {
        JsonObject json = new JsonObject();
        json.addProperty("id", lessonItem.getId());
        json.addProperty("type", lessonItem.getType());
        json.addProperty("name", lessonItem.getName());

        String path = "";
        switch (lessonItem.getType()) {
            case LessonItem.TYPE_FILE:
                path = fileToJson(lessonItem.getPath());
                json.addProperty("extension", lessonItem.getExtension());

                break;
            case LessonItem.TYPE_URL:
                path = lessonItem.getPath();
        }

        json.addProperty("path", path);
        return json;
    }

    public String fileToJson(String path) {
        String encodedFile = "";
        File file = new File(path);
        try {
            byte[] bytes = fileToByteArray(file);
            encodedFile = Base64.encodeToString(bytes, Base64.DEFAULT);


        } catch (IOException ioe) {
            Log.e("Exception", ioe.getMessage());
        }
        return encodedFile;
    }

    byte[] fileToByteArray(File file) throws IOException {
        return org.apache.commons.io.FileUtils.readFileToByteArray(file);
    }

}
