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
            if (instance.loadBooks().size() < 5) {
                for (int i = 0; i < 5; i++) {
                    Book b = new Book("Book " + i);
                    b.addLesson(new Lesson());
                    instance.saveBook( b);
                }
            }
        }
        return instance;
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
        return new Book(book.first());
    }

    @Override
    public void saveBook( Book book) {
        Realm realm = Realm.getDefaultInstance();
        Log.e("DatabaseBookLessonCount", "" + book.getLessons().size());
        List<Lesson> lessons = book.getLessons();
        realm.beginTransaction();

        realm.insertOrUpdate(book);


        
        realm.commitTransaction();




    }
}