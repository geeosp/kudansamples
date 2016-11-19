package com.voxar.arauthtool;

/**
 * Created by Geovane on 19/11/2016.
 */
public class BookDatabaseSing {
    private static BookDatabaseSing ourInstance = new BookDatabaseSing();

    public static BookDatabaseSing getInstance() {
        return ourInstance;
    }

    private BookDatabaseSing() {
    }
}
