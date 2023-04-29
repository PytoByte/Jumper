package com.example.project;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.util.Log;
import android.view.SurfaceHolder;

import com.example.project.sprites.Finish;
import com.example.project.sprites.Player;
import com.example.project.sprites.Sprite;
import com.example.project.sprites.Wall;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

public class DrawThread extends Thread {
    private final SurfaceHolder surfaceHolder;
    private volatile boolean running = true; //флаг для остановки потока
    private final Paint backgroundPaint = new Paint();
    private final GameCore gameCore;

    {
        backgroundPaint.setColor(Color.BLACK);
        backgroundPaint.setStyle(Paint.Style.FILL);
    }

    public DrawThread(SurfaceHolder surfaceHolder, DrawView dw) {
        this.surfaceHolder = surfaceHolder;
        gameCore = new GameCore();
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

        try {
            level.loadLevel("level1");
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

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