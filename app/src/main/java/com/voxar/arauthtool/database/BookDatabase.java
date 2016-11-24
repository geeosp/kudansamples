package com.voxar.arauthtool.database;

import com.voxar.arauthtool.models.Book;

import java.util.ArrayList;

/**
 * Created by Geovane on 19/11/2016.
 */

public abstract class BookDatabase {

    public abstract ArrayList<Book> loadBooks();
    public abstract Book getBook(int id);
    public abstract void saveBook(int id, Book book);
    private static BookDatabase instance;



}
