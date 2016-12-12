package eu.kudan.kudansamples;

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
        String apiKey = getString(R.string.kudan_api);
        key.setAPIKey(apiKey);
        Realm.init(getApplicationContext());
        database = RealmBookDatabase.getInstance(getApplicationContext());

    }


}
