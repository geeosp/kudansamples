package com.voxar.arauthtool.activities;


import android.content.Intent;
import android.database.DataSetObserver;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.voxar.arauthtool.models.Book;
import com.voxar.arauthtool.models.Lesson;
import com.voxar.arauthtool.models.LessonItem;

import java.io.File;
import java.util.List;

import eu.kudan.kudan.ARActivity;
import eu.kudan.kudan.ARImageTrackable;
import eu.kudan.kudan.ARImageTrackableListener;
import eu.kudan.kudan.ARImageTracker;
import eu.kudan.kudan.ARLightMaterial;
import eu.kudan.kudan.ARMeshNode;
import eu.kudan.kudan.ARModelImporter;
import eu.kudan.kudan.ARModelNode;
import eu.kudan.kudan.ARTexture2D;
import eu.kudan.kudan.ARView;
import eu.kudan.kudansamples.MyApplication;
import eu.kudan.kudansamples.R;

public class TrackingActivityFinal extends ARActivity {
    public Book book;
    // public ARNode tracable;
    public ListView listView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking);
        long bookId = getIntent().getLongExtra("bookId", -1);
        book = MyApplication.getDatabase().getBook(bookId);
        listView = (ListView) findViewById(R.id.lesson_list);

    }

    @Override
    public void setup() {
        super.setup();
        for (int i = 0; i < book.getLessons().size(); i++) {

            ARImageTrackable imageTracked = addImageTrackableFromPath(book.getLessons().get(i));

            addModelNode(imageTracked, "cube.jet");
        }


    }


    private ARImageTrackable addImageTrackableFromPath(final Lesson lesson) {


        // Initialise image trackable
        ARImageTrackable trackable = new ARImageTrackable("" + lesson.getId());
        trackable.loadFromPath(lesson.getPath());

        // Get instance of image tracker manager
        ARImageTracker trackableManager = ARImageTracker.getInstance();

        // Add image trackable to image tracker manager
        trackableManager.addTrackable(trackable);
        trackable.addListener(new ARImageTrackableListener() {
            @Override
            public void didDetect(ARImageTrackable trackable) {

                Log.i("geeo", "detected " + trackable.getName());
                Log.i("geeo", "lesson size " + lesson.getLessonItems().size());
                LessonItemAdapter adapter = new LessonItemAdapter(lesson.getLessonItems());
                listView.setAdapter(adapter);
                View v = findViewById(R.id.listviewroot);
                v.setVisibility(View.VISIBLE);
                v = findViewById(R.id.tracking_symbol);
                v.setVisibility(View.INVISIBLE);
            }


            @Override
            public void didTrack(ARImageTrackable trackable) {

                //    Log.i("geeo", "tracked" + trackable.getName());
            }

            @Override
            public void didLose(ARImageTrackable trackable) {

                Log.i("geeo", "lost " + trackable.getName());
                View v = findViewById(R.id.listviewroot);
                v.setVisibility(View.INVISIBLE);
                v = findViewById(R.id.tracking_symbol);
                v.setVisibility(View.VISIBLE);
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


    class LessonItemAdapter implements ListAdapter {
        List<LessonItem> itens;

        LessonItemAdapter(List<LessonItem> itens) {
            this.itens = itens;
        }

        @Override
        public boolean areAllItemsEnabled() {
            return true;
        }

        @Override
        public boolean isEnabled(int i) {
            return true;
        }


        @Override
        public void registerDataSetObserver(DataSetObserver dataSetObserver) {

        }

        @Override
        public void unregisterDataSetObserver(DataSetObserver dataSetObserver) {

        }

        @Override
        public int getCount() {
            return itens.size();
        }

        @Override
        public Object getItem(int i) {
            return itens.get(i);
        }

        @Override
        public long getItemId(int i) {
            return itens.get(i).getId();
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null) {

                LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
                convertView = inflater.inflate(R.layout.activity_tracking_lesson_item, parent, false);
            }
            TextView tv = (TextView) convertView.findViewById(R.id.tv_lesson_name);
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    LessonItem item = itens.get(position);
                    switch (item.getType()) {
                        case LessonItem.TYPE_URL:

                            Intent i = new Intent(Intent.ACTION_VIEW,
                                    Uri.parse(((LessonItem) getItem(position)).getPath()));
                            startActivity(i);
                            break;
                        case LessonItem.TYPE_FILE:
                            //Uri uri = Uri.parse(((LessonItem) getItem(position)).getPath());
                            String path = ((LessonItem) getItem(position)).getPath();
                            File temp_file = new File(path);
                            Uri contentUri = FileProvider.getUriForFile(getApplicationContext(), "eu.kudan.kudansamples.fileprovider", temp_file);
                            Log.d("Content", contentUri.toString());

                            Intent intent = new Intent(Intent.ACTION_VIEW, contentUri);

                            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            startActivity(intent);

                            break;
                    }
                }
            });
            tv.setText("" + itens.get(position).getName());
            return convertView;
        }

        @Override
        public int getItemViewType(int i) {
            return 0;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public boolean isEmpty() {
            return itens.size() == 0;
        }
    }
}