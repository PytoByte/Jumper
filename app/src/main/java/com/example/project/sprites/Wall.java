package com.example.project.sprites;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;

import com.example.project.sprites.extensions.Collider;
import com.example.project.game.GameCore;
import com.example.project.sprites.extensions.Position;
import com.example.project.R;
import com.example.project.sprites.extensions.SpriteWorking;

import java.util.ArrayList;

public class Wall extends Sprite {
    Bitmap bitmapSource;
    private boolean textureAssigned = false;

    {
        tag = "Wall";
    }

    public Wall(float x, float y, GameCore gameCore) {
        pos = new Position(x,y);
        col = new Collider();
        col.set(pos, 1, 1);
        this.gameCore = gameCore;
        bitmapSource = BitmapFactory.decodeResource(gameCore.gameContext.getResources(), R.drawable.sample_wall);
    }

    public Wall(float x, float y, int rotate, GameCore gameCore) {
        pos = new Position(x,y);
        col = new Collider();
        col.set(pos, 1, 1);
        this.gameCore = gameCore;
        this.rotate = rotate;
        bitmapSource = BitmapFactory.decodeResource(gameCore.gameContext.getResources(), R.drawable.sample_wall);
    }

    public void setType(int type) {
        if (type==0) {
            bitmapSource = BitmapFactory.decodeResource(gameCore.gameContext.getResources(), R.drawable.sample_wall);
        } else if (type==30) {
            bitmapSource = BitmapFactory.decodeResource(gameCore.gameContext.getResources(), R.drawable.angle_wall);
        } else if (type==50) {
            bitmapSource = BitmapFactory.decodeResource(gameCore.gameContext.getResources(), R.drawable.real_angle_wall);
        } else if (type==3) {
            bitmapSource = BitmapFactory.decodeResource(gameCore.gameContext.getResources(), R.drawable.third_wall);
        }
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
        int width = gameCore.canvas.getWidth();
        int height = gameCore.canvas.getHeight();

        int v = Math.min(width, height);

        if (!textureAssigned) {
            Matrix matrix = new Matrix();

            matrix.setScale( (((float)v/gameCore.camera.scale)/(float)bitmapSource.getWidth()), (((float)v/gameCore.camera.scale)/(float)bitmapSource.getHeight()));
            matrix.postRotate(rotate);
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
