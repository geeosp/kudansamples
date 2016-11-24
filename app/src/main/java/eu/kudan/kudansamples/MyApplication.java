package eu.kudan.kudansamples;

import android.app.Application;

import com.voxar.arauthtool.database.BookDatabase;
import com.voxar.arauthtool.database.NaiveBookDatabase;

import eu.kudan.kudan.ARAPIKey;


/**
 * Created by geeo on 29/10/16.
 */

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
    ARAPIKey key = ARAPIKey.getInstance();
    key.setAPIKey("GAWAE-FBVCC-XA8ST-GQVZV-93PQB-X7SBD-P6V4W-6RS9C-CQRLH-78YEU-385XP-T6MCG-2CNWB-YK8SR-8UUQ");

    }

   public  static BookDatabase getDatabase(){
        return NaiveBookDatabase.getInstance();
    }



}
