package com.voxar.arauthtool;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import eu.kudan.kudan.ARActivity;
import eu.kudan.kudan.ARView;
import eu.kudan.kudansamples.R;

public class TrackingActivity extends ARActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking);
    }

    @Override
    public void setup() {
        super.setup();
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
}
