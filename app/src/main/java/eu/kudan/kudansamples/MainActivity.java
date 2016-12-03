package eu.kudan.kudansamples;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.voxar.arauthtool.activities.BookListActivity;
import com.voxar.arauthtool.models.Lesson;
import com.voxar.arauthtool.models.LessonItem;
import com.voxar.arauthtool.activities.TrackingActivity;

import gun0912.tedbottompicker.TedBottomPicker;


public class MainActivity extends AppCompatActivity {
    final int MY_PERMISSION_CAMERA = 1;
    final int MY_PERMISSION_READ_EXTERNAL_STORAGE = 2;
    final int MY_PERMISSION_WRITE_EXTERNAL_STORAGE = 3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onResume() {
        //chamado quando oapp fica em evidencia;
        super.onResume();
        permissionsRequest();
        //openARActivity();
    }

    public void onButtonPressed(View v) {
        permissionsRequest();
        TedBottomPicker tedBottomPicker = new TedBottomPicker.Builder(MainActivity.this)
                .setOnImageSelectedListener(new TedBottomPicker.OnImageSelectedListener() {
                    @Override
                    public void onImageSelected(Uri uri) {
                        Lesson lesson = new Lesson("Revolução Francesa1", uri.getPath());
                        LessonItem item0 = new LessonItem(LessonItem.TYPE_URL, "Wikipedia", "https://pt.wikipedia.org/wiki/Revolu%C3%A7%C3%A3o_Francesa");
                        lesson.addLessonItem(item0);
                        LessonItem item1 = new LessonItem(LessonItem.TYPE_URL, "Youtube", "https://www.youtube.com/watch?v=4nkwvmBKxek");
                        lesson.addLessonItem(item1);
                        LessonItem item3 = new LessonItem(LessonItem.TYPE_URL, "Mapa", "https://www.google.com.br/maps/place/Fran%C3%A7a/@46.1350721,-2.28879,6z/data=!3m1!4b1!4m5!3m4!1s0xd54a02933785731:0x6bfd3f96c747d9f7!8m2!3d46.227638!4d2.213749");
                        Log.i("geeo", uri.getPath());
                        Intent intent = new Intent(getApplicationContext(), TrackingActivity.class);
                        lesson.addLessonItem(item3);
                        intent.putExtra("lesson", lesson);
                        startActivity(intent);
                    }
                }).setOnErrorListener(new TedBottomPicker.OnErrorListener() {
                    @Override
                    public void onError(String message) {
                        Log.e("geeo", message);
                    }

                })
               // .showCameraTile(false)
                .create();

        tedBottomPicker.show(getSupportFragmentManager());
    }
public void  openBooksActivity(View v){
    Intent intent = new Intent(this, BookListActivity.class);
    startActivity(intent);
}

    protected void openARActivity() {

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
        } else {
            Intent intent = new Intent(this, TrackingActivity.class);
            startActivity(intent);

        }


    }

    // Requests app permissions
    public void permissionsRequest() {

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.INTERNET)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED ||ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.INTERNET, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 111);

        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 111: {
                if (grantResults.length > 0 && grantResults[0]== PackageManager.PERMISSION_GRANTED ) {

                } else {
                    permissionsNotSelected();
                }
            }
        }
    }

    private void permissionsNotSelected() {
        AlertDialog.Builder builder = new AlertDialog.Builder (this);
        builder.setTitle("Permissions Requred");
        builder.setMessage("Please enable the requested permissions in the app settings in order to use this demo app");
        builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener () {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
                System.exit(1);
            }
        });
        AlertDialog noInternet = builder.create();
        noInternet.show();
    }

}
