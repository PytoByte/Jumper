package com.example.project.sprites;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;

import com.example.project.Camera;
import com.example.project.Collider;
import com.example.project.GameCore;
import com.example.project.Position;
import com.example.project.R;

import java.util.ArrayList;

public class Wall extends Sprite {
    private boolean textureAssigned = false;

    {
        tag = "Wall";
    }

    public Wall(float x, float y, GameCore gameCore) {
        pos = new Position(x,y);
        col = new Collider();
        col.set(pos, 1, 1);
        this.gameCore = gameCore;
    }

    public static int getWidth(GameCore gameCore) {
        Bitmap bitmapSource = BitmapFactory.decodeResource(gameCore.gameContext.getResources(), R.drawable.sample_wall);

        int width = gameCore.canvas.getWidth();
        int height = gameCore.canvas.getHeight();

        int v = Math.min(width, height);

        return (int) ((((float)v/gameCore.camera.scale)/(float)bitmapSource.getWidth()))*bitmapSource.getWidth();
    }

    public static int getHeight(GameCore gameCore) {
        Bitmap bitmapSource = BitmapFactory.decodeResource(gameCore.gameContext.getResources(), R.drawable.sample_wall);

        int width = gameCore.canvas.getWidth();
        int height = gameCore.canvas.getHeight();

        int v = Math.min(width, height);

        return (int) ((((float)v/gameCore.camera.scale)/(float)bitmapSource.getHeight())*bitmapSource.getHeight());
    }

    protected void assignTexture() {
        Bitmap bitmapSource = BitmapFactory.decodeResource(gameCore.gameContext.getResources(), R.drawable.sample_wall);

        int width = gameCore.canvas.getWidth();
        int height = gameCore.canvas.getHeight();

        int v = Math.min(width, height);

        if (!textureAssigned) {
            Matrix matrix = new Matrix();

            matrix.setScale( (((float)v/gameCore.camera.scale)/(float)bitmapSource.getWidth()), (((float)v/gameCore.camera.scale)/(float)bitmapSource.getHeight()));

            this.bitmap = Bitmap.createBitmap(bitmapSource, 0, 0, bitmapSource.getWidth(), bitmapSource.getHeight(), matrix, false);
            textureAssigned = true;
        }
    }

    @Override
    public void watchCollisions(ArrayList<Sprite> sprites) {}

    @Override
    public void collide(Sprite sprite) {}

    @Override
    public void action() {}

    @Override
    public void draw(Position canvPos) {
        Paint paint = new Paint();

        assignTexture();

        gameCore.canvas.drawBitmap(this.bitmap, canvPos.x, canvPos.y, paint);
    }
}
