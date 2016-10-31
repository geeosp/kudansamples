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

import com.voxar.arauthtool.Lesson;
import com.voxar.arauthtool.LessonItem;
import com.voxar.arauthtool.TrackingActivity;

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
                            Lesson lesson  = new Lesson("Lesson 01" ,uri.getPath());

                            LessonItem item0 = new LessonItem(LessonItem.LessonItemType.URL,"Internet link", "http://www.google.com");
                            lesson.addLessonItem(item0);


                           LessonItem item1 = new LessonItem(LessonItem.LessonItemType.URL,"Youtube Link", "https://www.youtube.com/watch?v=ouq9tU5DUOc");
                            lesson.addLessonItem(item1);


                            LessonItem item2 = new LessonItem(LessonItem.LessonItemType.URL,"Dropbox link", "https://www.dropbox.com/s/f5jghxyxxz5faul/CG-2-2016.pptx?dl=0");
                            lesson.addLessonItem(item2);
                            LessonItem item3 = new LessonItem(LessonItem.LessonItemType.URL,"Maps Link", "https://www.google.com.br/maps/place/R%C3%BAssia/@33.8094122,40.7922993,3z/data=!3m1!4b1!4m5!3m4!1s0x453c569a896724fb:0x1409fdf86611f613!8m2!3d61.52401!4d105.318756?hl=pt-BR");
                            lesson.addLessonItem(item3);



                            Log.i("geeo",uri.getPath());
                            Intent intent = new Intent(getApplicationContext(), TrackingActivity.class);
                            intent.putExtra("lesson",lesson);
                            startActivity(intent);
                        }
                    })
                    .create();

            tedBottomPicker.show(getSupportFragmentManager());

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
