package com.voxar.arauthtool;

import java.util.ArrayList;

/**
 * Created by Geovane on 19/11/2016.
 */

public class NaiveBookDatabase extends BookDatabase {
    private static NaiveBookDatabase instance=new NaiveBookDatabase();

    private NaiveBookDatabase(){

    }


    public static  NaiveBookDatabase getInstance(){
        if (instance ==null){
            instance = new NaiveBookDatabase();
        }
        return instance;

    }


    ArrayList<Book> books;
    @Override
    public ArrayList<Book> loadBooks() {
        if(books==null){
            books=new ArrayList<Book>();
            for (int i =0; i<50;i++){
                Book b = new Book("Book" + i);
                books.add(b);
            }
        }
        return books;
    }

    @Override
    public Book getBook(int id) {
        return books.get(id);
    }

    @Override
    public void saveBook(int id, Book b) {
        books.set(id, b);
    }
}

