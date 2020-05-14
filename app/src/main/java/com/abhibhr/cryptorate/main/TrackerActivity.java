package com.abhibhr.cryptorate.main;

import android.os.Bundle;
import android.os.PersistableBundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.abhibhr.cryptorate.tracking.Tracker;

public class TrackerActivity extends AppCompatActivity {

    protected Tracker mTracker;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTracker = new Tracker(this);
    }
}
