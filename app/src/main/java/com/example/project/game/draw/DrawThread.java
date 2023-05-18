package com.example.project.game.draw;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.SurfaceHolder;

import com.example.project.game.LevelLoader;
import com.example.project.game.GameCore;

import org.json.JSONException;

import java.io.IOException;

public class DrawThread extends Thread {
    private final SurfaceHolder surfaceHolder;
    private volatile boolean running = true; //флаг для остановки потока
    private final Paint backgroundPaint = new Paint();
    private final GameCore gameCore;
    private long startTime = 0;
    public int FPSLimit = 60;

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

    private void startTimer() {
        startTime = System.nanoTime();
    }

    private boolean checkDrawBreak(int frames) {
        return FPSLimit >= frames;
    }

    private boolean checkTimerRestart() {
        long delta = System.nanoTime() - startTime;
        if (1_000_000_000L/delta>=1) {
            startTime = System.nanoTime();
            return true;
        }
        return false;
    }

    @Override
    public void run() {
        Canvas canvas = surfaceHolder.lockCanvas();
        gameCore.setCanvas(canvas);
        gameCore.initCamera();
        surfaceHolder.unlockCanvasAndPost(canvas);

        LevelLoader levelLoader = new LevelLoader(gameCore);

        try {
            levelLoader.loadFromPreference();
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        levelLoader.joinToCore();
        int frames = 0;

        if (FPSLimit>0) {
            startTimer();
        }

        gameCore.startGame();

        while (running && !gameCore.extraShutdown) {
            canvas = surfaceHolder.lockCanvas();
            if (checkTimerRestart()) {
                frames = 0;
            }

            if (canvas != null && checkDrawBreak(frames) && !gameCore.extraShutdown) {
                try {
                    gameCore.drawFrame();
                    if (FPSLimit>0) {
                        frames++;
                    }
                } catch (Throwable er) {
                    throw er;
                    //Log.e("ERROR WHILE DRAWING", "run: " + er);
                }
                finally {
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }
            }
        }
    }
}