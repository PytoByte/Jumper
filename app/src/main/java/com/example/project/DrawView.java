package com.example.project;

import android.content.Context;
import android.graphics.Canvas;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.example.project.sprites.Player;
import com.example.project.sprites.Sprite;
import com.example.project.sprites.Wall;

import java.util.ArrayList;

public class DrawView extends SurfaceView implements SurfaceHolder.Callback {

    private DrawThread drawThread;
    private ArrayList<Sprite> sprites = new ArrayList<Sprite>();
    private Context context;

    public DrawView(Context context) {
        super(context);
        getHolder().addCallback(this);
        this.context = context;
    }
    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        drawThread = new DrawThread(getHolder(), sprites, this, context);
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
            }
        }
    }
}