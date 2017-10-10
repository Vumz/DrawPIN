package com.example.vamsee.drawapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.SyncStateContract;
import android.support.wearable.view.WatchViewStub;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;



import java.io.ByteArrayOutputStream;
import java.util.Collection;
import java.util.HashSet;

import static android.graphics.Color.YELLOW;

public class SetPasswordActivity extends Activity {
        public static final int REQUEST_CODE_SETTING = 101;
        private ImageView prefButton;
        private DrawingView drawingView;
        private ImageView okButton;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_set_password);
            final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
            stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
                @Override
                public void onLayoutInflated(WatchViewStub stub) {
                    drawingView = (DrawingView) findViewById(R.id.drawingView);
                }
            });

        }

        @Override
        protected void onStart() {
            super.onStart();
        }

        @Override
        protected void onStop() {
            super.onStop();
        }

    }