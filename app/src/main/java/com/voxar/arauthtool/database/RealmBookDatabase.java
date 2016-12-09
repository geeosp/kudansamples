package com.voxar.arauthtool.database;

import android.content.Context;
import android.util.Log;

import com.voxar.arauthtool.models.Book;
import com.voxar.arauthtool.models.Lesson;

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

                    /*Lesson a = new Lesson("Lição 1", "https://static.pexels.com/photos/36487/above-adventure-aerial-air.jpg");
                    b.addLesson(a);
                    Log.e("lessonId", "" + a.getId());
                    instance.saveBook(b);
                    Lesson c = new Lesson("Lição 2", "http://www.w3schools.com/howto/img_fjords.jpg");
                    Log.e("lessonId", "" + c.getId());
                    b.addLesson(c);
                    instance.saveBook(b);
                    Lesson d = new Lesson("Lição 3", "https://www.planwallpaper.com/static/images/butterfly-wallpaper.jpeg");
                    b.addLesson(d);
                    Log.e("lessonId", "" + d.getId());
*/
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
            String path = lesson.getFilePath();
            if (!path.contains("http")) {//is a file
                String extension = path.substring(path.lastIndexOf('.'));
                Log.d("SavingBook", "lesson Extension: " + extension);
                File oldFile = new File(path);
                File newFile = new File(ctx.getFilesDir(), "" + lesson.getId() + extension);
                String newPath = newFile.getPath();
                Log.d("SavingBook", "newFilePath: " + newPath);
                try {
                    if (newFile.exists()) {
                        newFile.delete();
                        newFile.createNewFile();

                    }
                    copy(oldFile, newFile);
                    Log.e("SavingBook", "Sucessfull" + newFile.getPath());
                } catch (IOException e) {
                    Log.e("SavingBook", "Not Sucessfull" + e.getMessage());
                }
                lesson.setFilePath(newPath);
                book.getLessons().set(l, lesson);
            }
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
}
