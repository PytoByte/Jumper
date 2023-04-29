package com.example.project;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.example.project.sprites.Player;
import com.example.project.sprites.Sprite;
import com.example.project.sprites.Wall;

import java.util.ArrayList;

public class DrawView extends SurfaceView implements SurfaceHolder.Callback {

    private DrawThread drawThread;
    public Context context;
    public Activity activity;

    public DrawView(Context context, Activity activity) {
        super(context);
        getHolder().addCallback(this);
        this.context = context;
        this.activity = activity;
    }
    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        drawThread = new DrawThread(getHolder(), this);
        drawThread.start();
        // создание SurfaceView
    }
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        // изменение размеров SurfaceView
    }
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        drawThread.requestStop();
        boolean retry = true;
        while (retry) {
            try {
                drawThread.join();
                retry = false;
            } catch (InterruptedException e) {
                Log.e("What?", "surfaceDestroyed: "+e);
            }
        }
    }
}