package com.voxar.arauthtool;

import android.app.Application;

import com.voxar.arauthtool.database.BookDatabase;
import com.voxar.arauthtool.database.RealmBookDatabase;

import eu.kudan.kudan.ARAPIKey;
import io.realm.Realm;


/**
 * Created by geeo on 29/10/16.
 */

public class MyApplication extends Application {

    private static BookDatabase database;

    public static BookDatabase getDatabase() {
        return database;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ARAPIKey key = ARAPIKey.getInstance();
        String apiKey = "gcHUoWNeJTjZ6Cboz7OtIFD/be8kfAPJ46ElM8iJCJjUeHaRTPCIuIjFRqvicN92PC/haijxLhZe9CQjeQcyMzQknb9vUwyMnInc9hGpg2dgC97cE0tl3t63dwi7T8Qp9GtCRFrLJHY1ocrEYmRwBp0gJibc0MGb2mfmOS4169FJOK6L465DJKKdDNs8UXSA5t0nq+QrgX06Qe+Bl9x5gqn19W24kr0wdx/aoXG/q/EPcom1etAXI+109jExzQ4EIs5a54k1EIM+0Dk9I+9IOVC9CeqGpZ48yJYqe7DqnrjOea4kQtsI2glhPf8kGdJ1az/FiJZo4L7F7RMtgIzvwW9ILm2EjTUf2JFV3hA+hfzrFVRjpHtU/ItxgD1dD1tygB9MVThFrwcTf5V4T9X0nKEbKN/X6aS/jOYa0QPMj/VLEpqdOPscNkKoKAd/V1F+c7b6fcmv3+opID2NBRTDBQmOL0Wx20YP2Uz2eC/8WtkTDksqruivMmBa4+bbj6unQ44N8fV63wmIHSv+nLgMZ1Jb61/jmfKxnn4R/eKsOQbEhp0ntI77hHfrZJtjUiY5WpgHjN08Fc0PJ899H6A6xMvEpKodmEa79IJZ91NKs9NrWlAc2JgZ8DSjvbOifIEiY0LhSsUTkQZ9Jcj68KL35zDTUjBaOZ0B9QWpCFZKRqc=";

        key.setAPIKey(apiKey);
        Realm.init(getApplicationContext());

        database = RealmBookDatabase.getInstance(getApplicationContext());

    }


}
