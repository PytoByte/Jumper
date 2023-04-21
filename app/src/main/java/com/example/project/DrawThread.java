package com.example.project;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.SurfaceHolder;

import com.example.project.sprites.Finish;
import com.example.project.sprites.Player;
import com.example.project.sprites.Sprite;
import com.example.project.sprites.Wall;

import java.util.ArrayList;

public class DrawThread extends Thread {
    private SurfaceHolder surfaceHolder;
    private volatile boolean running = true; //флаг для остановки потока
    private Paint backgroundPaint = new Paint();
    private ArrayList<Sprite> sprites;
    private GameCore gameCore;

    {
        backgroundPaint.setColor(Color.BLACK);
        backgroundPaint.setStyle(Paint.Style.FILL);
    }

    public DrawThread(SurfaceHolder surfaceHolder, ArrayList<Sprite> sprites, DrawView dw, Context context) {
        this.surfaceHolder = surfaceHolder;
        this.sprites = sprites;
        gameCore = new GameCore();
        gameCore.setGameContext(context);
        gameCore.setGameView(dw);
    }
    public void requestStop() {
        running = false;
    }

    @Override
    public void run() {
        Canvas canvas = surfaceHolder.lockCanvas();
        gameCore.setCanvas(canvas);
        gameCore.initCamera();

        Level level = new Level(gameCore);
        level.initDefaultLevel();
        level.joinToCore();

        while (running) {
            /*
            if (canvas != null) {
                try {
                    gameCore.drawFrame();
                } catch (Throwable er) {
                    Log.e("ERROR WHILE DRAWING", "run: "+er);
                } finally {
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }
            }
             */

            gameCore.drawFrame();
            surfaceHolder.unlockCanvasAndPost(canvas);
            canvas = surfaceHolder.lockCanvas();
        }
    }
}