package eu.kudan.kudansamples;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.voxar.arauthtool.TrackingActivity;

import eu.kudan.kudan.ARAPIKey;
import eu.kudan.kudan.ARNode;




public class MainActivity extends AppCompatActivity {
 final int  MY_PERMISSION_CAMERA =1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ARAPIKey key = ARAPIKey.getInstance();
        key.setAPIKey("GAWAE-FBVCC-XA8ST-GQVZV-93PQB-X7SBD-P6V4W-6RS9C-CQRLH-78YEU-385XP-T6MCG-2CNWB-YK8SR-8UUQ");

    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onResume() {
    //chamado quando oapp fica em evidencia;
        super.onResume();
        openARActivity();
    }

    protected void openARActivity(){

        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.CAMERA)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA},
                        MY_PERMISSION_CAMERA);


            }
        }else{
            Intent intent= new Intent(this, TrackingActivity.class);
            startActivity(intent);

        }






    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
                case MY_PERMISSION_CAMERA: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openARActivity();

                } else {

                  }
                }

        }
}}
