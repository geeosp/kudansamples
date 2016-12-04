package com.voxar.arauthtool.activities;

import com.voxar.arauthtool.models.Book;
import com.voxar.arauthtool.models.Lesson;
import com.voxar.arauthtool.models.LessonItem;

/**
 * Created by Geovane on 04/12/2016.
 */

public class Limbo {
    static private Book currentBook;
    static private Lesson currentLesson;
    static private LessonItem currentLessonItem;

    public static Book getCurrentBook() {
        return currentBook;
    }

    public static void setCurrentBook(Book currentBook) {
        Limbo.currentBook = currentBook;
    }

    public static Lesson getCurrentLesson() {
        return currentLesson;
    }

    public static void setCurrentLesson(Lesson currentLesson) {
        Limbo.currentLesson = currentLesson;
    }

    public static LessonItem getCurrentLessonItem() {
        return currentLessonItem;
    }

    public static void setCurrentLessonItem(LessonItem currentLessonItem) {
        Limbo.currentLessonItem = currentLessonItem;
    }
}
