package com.voxar.arauthtool.database;

import com.voxar.arauthtool.models.Book;
import com.voxar.arauthtool.models.Lesson;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Geovane on 19/11/2016.
 */

public class NaiveBookDatabase extends BookDatabase {
    private static NaiveBookDatabase instance=new NaiveBookDatabase();

    private NaiveBookDatabase(){
        loadBooks();
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
                int rand = new Random().nextInt(10);
                for (int k =0;k< rand;k++){
                    Lesson l = new Lesson("Lesson "+k, "");
                    b.addLesson(l);
                }



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

