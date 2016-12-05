package com.voxar.arauthtool.database;

import android.util.Log;

import com.voxar.arauthtool.models.Book;
import com.voxar.arauthtool.models.Lesson;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by geeo on 03/12/16.
 */

public class RealmBookDatabase extends BookDatabase {


    private static RealmBookDatabase instance;

    public RealmBookDatabase() {
    }

    public static RealmBookDatabase getInstance() {
        if (instance == null) {
            instance = new RealmBookDatabase();
         //   instance.deleteAll();


            if (instance.loadBooks().size() < 5) {
                for (int i = 0; i < 5; i++) {
                    Book b = new Book("Book " + i);

                    Lesson a = new Lesson(b.getName() + 1, "https://static.pexels.com/photos/36487/above-adventure-aerial-air.jpg");
                    b.addLesson(a);
                    Log.e("lessonId", "" + a.getId());
                    instance.saveBook(b);


                    Lesson c = new Lesson(b.getName() + 2, "http://www.w3schools.com/howto/img_fjords.jpg");


                    Log.e("lessonId", "" + c.getId());
                    b.addLesson(c);
                    instance.saveBook(b);


                    Lesson d = new Lesson("Geo", "https://www.planwallpaper.com/static/images/butterfly-wallpaper.jpeg");
                    b.addLesson(d);
                    Log.e("lessonId", "" + d.getId());
                    instance.saveBook(b);

                }
            }
        }
        return instance;
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
    public void saveBook(Book b) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();


        Log.e("DatabaseBookLessonCount", "" + b.getLessons().size());


        realm.insertOrUpdate(b);
        realm.commitTransaction();


    }
}
