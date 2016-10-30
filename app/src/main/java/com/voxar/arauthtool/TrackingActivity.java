package com.voxar.arauthtool;

import android.app.Activity;
import android.content.DialogInterface;


import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import eu.kudan.kudan.ARActivity;
import eu.kudan.kudan.ARImageNode;
import eu.kudan.kudan.ARImageTrackable;
import eu.kudan.kudan.ARImageTrackableListener;
import eu.kudan.kudan.ARImageTracker;
import eu.kudan.kudan.ARNode;
import eu.kudan.kudan.ARVideoNode;
import eu.kudan.kudan.ARVideoTexture;
import eu.kudan.kudan.ARView;
import eu.kudan.kudansamples.R;

public class TrackingActivity extends ARActivity implements ARImageTrackableListener {
    public String uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking);
        uri =  getIntent().getExtras().getString("filepath");

    }

    @Override
    public void setup() {
        super.setup();

        ARImageTrackable waves = new ARImageTrackable("waves");
        //waves.loadFromAsset("waves.png");
        waves.loadFromPath(uri);
        ARImageTracker tracker = ARImageTracker.getInstance();
        Log.i("geeo", "Initialized Tracker");
        tracker.addTrackable(waves);
        Log.i("geeo", "Added Trackable");



      //  ARImageTrackable legoTrackable = tracker.findTrackable("waves");
        ARImageNode imageNode = new ARImageNode("waves.png");
       // ARVideoTexture bugTexture = new ARVideoTexture();

      //  bugTexture.loadFromAsset("bug.mp4");
      //  ARVideoNode bug = new ARVideoNode(bugTexture);
       // bug.preRender();

        // make it smaller.
       //imageNode.scaleBy(0.5f, 0.5f, 0.5f);

        // add it to the lego trackable.
        waves.getWorld().addChild(imageNode);
    //    legoTrackable.getWorld().addChild(bug);
        waves.getWorld().getFullTransform();



    }

    @Override
    public ARView getARView() {
        //return super.getARView();
        return (ARView)findViewById(R.id.arview);

    }

    @Override
    public boolean hasCameraPermission() {
        return true;

    }

    @Override
    public void didDetect(ARImageTrackable trackable) {
        new AlertDialog.Builder(getApplicationContext())
                .setTitle("Delete entry")
                .setMessage("Are you sure you want to delete this entry?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
        Log.i("KudanSamples", "detected " + trackable.getName());
    }


    @Override
    public void didTrack(ARImageTrackable trackable) {
        new AlertDialog.Builder(getApplicationContext())
                .setTitle("Delete entry")
                .setMessage("Are you sure you want to delete this entry?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
		Log.i("KudanSamples", "tracked"+ trackable.getName());
    }

    @Override
    public void didLose(ARImageTrackable trackable) {
        new AlertDialog.Builder(getApplicationContext())
                .setTitle("Delete entry")
                .setMessage("Are you sure you want to delete this entry?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
        Log.i("KudanSamples", "lost " + trackable.getName());
    }
}
