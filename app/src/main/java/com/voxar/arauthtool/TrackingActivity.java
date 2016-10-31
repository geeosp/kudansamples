package com.voxar.arauthtool;


import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;

import eu.kudan.kudan.ARActivity;
import eu.kudan.kudan.ARImageTrackable;
import eu.kudan.kudan.ARImageTrackableListener;
import eu.kudan.kudan.ARImageTracker;
import eu.kudan.kudan.ARLightMaterial;
import eu.kudan.kudan.ARMeshNode;
import eu.kudan.kudan.ARModelImporter;
import eu.kudan.kudan.ARModelNode;
import eu.kudan.kudan.ARNode;
import eu.kudan.kudan.ARTexture2D;
import eu.kudan.kudan.ARView;
import eu.kudan.kudansamples.R;

public class TrackingActivity extends ARActivity {
    public Lesson lesson;
    public ARNode tracable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking);
        lesson = (Lesson) getIntent().getExtras().get("lesson");

        //   uri = getIntent().getExtras().getString("filepath");

    }

    @Override
    public void setup() {
        super.setup();
        ArrayList<LessonItem> lessons = lesson.getLessons();
        ARImageTrackable imageTracked = addImageTrackableFromPath(lesson.getName(), lesson.getFilePath());

            addModelNode(imageTracked, "cube.jet");

    }


    private ARImageTrackable addImageTrackableFromPath(String name, String path) {

        // Initialise image trackable
        ARImageTrackable trackable = new ARImageTrackable(name);
        trackable.loadFromPath(path);

        // Get instance of image tracker manager
        ARImageTracker trackableManager = ARImageTracker.getInstance();

        // Add image trackable to image tracker manager
        trackableManager.addTrackable(trackable);
        trackable.addListener(new ARImageTrackableListener() {
            @Override
            public void didDetect(ARImageTrackable trackable) {

                Log.i("geeo", "detected " + trackable.getName());
            }


            @Override
            public void didTrack(ARImageTrackable trackable) {

                //    Log.i("geeo", "tracked" + trackable.getName());
            }

            @Override
            public void didLose(ARImageTrackable trackable) {

                Log.i("geeo", "lost " + trackable.getName());
            }
        });
        return trackable;

    }

    private void addModelNode(ARImageTrackable trackable, String modelAsset) {
        // Import model
        ARModelImporter modelImporter = new ARModelImporter();
        modelImporter.loadFromAsset(modelAsset);
        ARModelNode modelNode = (ARModelNode) modelImporter.getNode();


        //Load model texture
        ARTexture2D texture2D = new ARTexture2D();
        texture2D.loadFromAsset("green.png");


        // Apply model texture to model texture material
        ARLightMaterial material = new ARLightMaterial();
        material.setTexture(texture2D);
        material.setAmbient(0.8f, 0.8f, 0.8f);

        // Apply texture material to models mesh nodes
        for (ARMeshNode meshNode : modelImporter.getMeshNodes()) {
            meshNode.setMaterial(material);
        }


        modelNode.rotateByDegrees(90, 1, 0, 0);
        modelNode.scaleByUniform(150f);

        // Add model node to image trackable
        trackable.getWorld().addChild(modelNode);
        modelNode.setVisible(true);

    }


    @Override
    public ARView getARView() {
        //return super.getARView();
        return (ARView) findViewById(R.id.arview);

    }

    @Override
    public boolean hasCameraPermission() {
        return true;

    }


}
