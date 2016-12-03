package com.voxar.arauthtool.database;

import com.voxar.arauthtool.models.Book;

import java.util.ArrayList;
import java.util.ListIterator;

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
                    Book b = new Book("Book " + i );
                    instance.saveBook(i, b);
                }
            }


        }
        return instance;

    }

    @Override
    public ArrayList<Book> loadBooks() {
        Realm realm = Realm.getDefaultInstance();
        final RealmResults<Book> books = realm.where(Book.class).findAll();


        ListIterator<Book> iterator = books.listIterator();
        ArrayList<Book> arrayList = new ArrayList<>();
        while (iterator.hasNext()) {
            arrayList.add(iterator.next());
        }

        return arrayList;
    }

    @Override
    public Book getBook(long id) {

        Realm realm = Realm.getDefaultInstance();
        final RealmResults<Book> book = realm.where(Book.class).equalTo("id", id).findAll();
        return book.first();


    }

    @Override
    public void saveBook(long id, Book book) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.insert(book);
        realm.commitTransaction();
    }
}
