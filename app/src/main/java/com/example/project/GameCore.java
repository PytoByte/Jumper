package com.example.project;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.View;

import com.example.project.sprites.Sprite;

import java.util.ArrayList;

public class GameCore {
    public Context gameContext = null;
    public View gameView = null;
    public Canvas canvas = null;
    public Camera camera;

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
        camera.setLockPos(level.player.pos);
    }

    public Level level;

    public int meshWidth;
    public int meshHeight;

    public Camera getCamera() {
        return camera;
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
    }

    public void initCamera() {
        camera = new Camera();
        camera.setGameCore(this);
    }

    public ArrayList<Sprite> getSprites() {
        return level.sprites;
    }

    public void setSprites(ArrayList<Sprite> sprites) {
        level.sprites = sprites;
    }

    public Context getGameContext() {
        return gameContext;
    }

    public void setGameContext(Context gameContext) {
        this.gameContext = gameContext;
    }

    public View getGameView() {
        return gameView;
    }

    public void setGameView(View gameView) {
        this.gameView = gameView;
    }

    public Canvas getCanvas() {
        return canvas;
    }

    public void setCanvas(Canvas canvas) {
        this.canvas = canvas;
    }


    public void drawFrame() {
        createMesh();
        drawBackgroung();
        ArrayList<Sprite> drawableSprites = calcActions();
        calcCollisions(drawableSprites);
        camera.draw(drawableSprites);
    }

    private void calcCollisions(ArrayList<Sprite> drawableSprites) {
        for (Sprite sprite : drawableSprites) {
            sprite.watchCollisions(level.sprites);
        }
    }

    private ArrayList<Sprite> calcActions() {
        ArrayList<Sprite> drawableSprites = new ArrayList<>();
        for (Sprite sprite : level.sprites) {
            Position canvPos = camera.toCanvasPos(sprite.pos);

            sprite.action();

            if (!(canvPos.x<-camera.border || canvPos.y<-camera.border || canvPos.x>camera.width+camera.border || canvPos.y>camera.height+camera.border)) {
                drawableSprites.add(sprite);
            }
        }
        return drawableSprites;
    }

    private void drawBackgroung() {
        Paint backgroundPaint = new Paint();
        backgroundPaint.setColor(Color.BLACK);
        canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), backgroundPaint);
    }

    private void createMesh() {
        int width = canvas.getWidth();
        int height = canvas.getHeight();
        int v = Math.min(width, height);

        this.meshWidth = v/camera.scale;
        this.meshHeight = v/camera.scale;

        //System.out.println("MESH = width:"+this.meshWidth+" height:"+this.meshHeight);
    }

    public void clearGame() {
        gameContext = null;
        gameView = null;
        canvas = null;
    }
}
