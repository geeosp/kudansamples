package com.voxar.arauthtool.database;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.voxar.arauthtool.models.Book;
import com.voxar.arauthtool.models.Lesson;
import com.voxar.arauthtool.models.LessonItem;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

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
        String path = lesson.getFilePath();
        if (!path.contains("http")) {//is a file
            lesson.setFilePath(copyFileToInternStorage(path, lesson.getId()));
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


    String bookToJson(long BookId) {
        String json = "";


        return json;
    }

    String lessonToJson(Lesson lesson) {
        String json = "";


        return json;
    }

    JsonElement lessonItemToJson(LessonItem lessonItem) {
        Gson gson = new Gson();
        JsonElement lessonItemJson = gson.toJsonTree(lessonItem);

        Log.e("JSON lessonItem: ", lessonItemJson.toString());

        return lessonItemJson;
    }


    byte[] fileToByteArray(File file) throws IOException {
        return org.apache.commons.io.FileUtils.readFileToByteArray(file);
    }
}
