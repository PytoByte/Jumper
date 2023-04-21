package com.example.project.sprites;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.widget.Toast;

import com.example.project.Camera;
import com.example.project.Collider;
import com.example.project.GameCore;
import com.example.project.Position;
import com.example.project.R;

import java.util.ArrayList;

public class Finish extends Sprite {

    private boolean textureAssigned = false;

    {
        tag = "Finish";
    }

    public Finish(float x, float y, GameCore gameCore) {
        pos = new Position(x,y);
        col = new Collider();
        this.gameCore = gameCore;
    }

    @Override
    public void draw(Position canvPos) {
        Paint paint = new Paint();
        assignTexture();

        gameCore.canvas.drawBitmap(this.bitmap, canvPos.x, canvPos.y, paint);
    }

    @Override
    protected void assignTexture() {
        Bitmap bitmapSource = BitmapFactory.decodeResource(gameCore.gameContext.getResources(), R.drawable.sample_finish);

        int width = gameCore.canvas.getWidth();
        int height = gameCore.canvas.getHeight();

        int v = Math.min(width, height);

        if (!textureAssigned) {
            Matrix matrix = new Matrix();

            matrix.setScale( (((float)v/gameCore.camera.scale)/(float)bitmapSource.getWidth()), (((float)v/gameCore.camera.scale)/(float)bitmapSource.getHeight()));

            this.bitmap = Bitmap.createBitmap(bitmapSource, 0, 0, bitmapSource.getWidth(), bitmapSource.getHeight(), matrix, false);
            col.set(pos, 1, 1);
            textureAssigned = true;
        }
    }

    @Override
    public void watchCollisions(ArrayList<Sprite> sprites) {}

    @Override
    public void collide(Sprite sprite) {
        if (sprite.tag.equals("Player")) {
            Paint paint = new Paint();
            paint.setColor(Color.GREEN);
            paint.setTextSize(100);
            paint.setFakeBoldText(true);
            gameCore.canvas.drawText("YOU WON",(int) (gameCore.canvas.getWidth()/2-20), (int) (gameCore.canvas.getHeight()/2), paint);
        }
    }

    @Override
    public void action() {}
}
