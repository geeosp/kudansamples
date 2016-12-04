package com.voxar.arauthtool.database;

import com.voxar.arauthtool.models.Book;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Geovane on 19/11/2016.
 */

public abstract class BookDatabase {

    public abstract List<Book> loadBooks();
    public abstract Book getBook(long id);
    public abstract void saveBook(Book book);


    public abstract void deleteBook(long id);
}
