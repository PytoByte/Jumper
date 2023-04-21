package com.example.project.sprites;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.example.project.Camera;
import com.example.project.Collider;
import com.example.project.GameCore;
import com.example.project.Position;
import com.example.project.R;

import java.util.ArrayList;
import java.util.Objects;

public class Player extends Sprite {
    {
        tag = "Player";
    }

    boolean inMotion = false;

    private Position prevPos = new Position();
    private Position stepPos = new Position();

    private final float velocity = 1.9f;
    private String side = "bottom";

    private boolean textureAssigned = false;
    private float width = 0;
    private float height = 0;

    public Player(float x, float y, GameCore gameCore) {
        pos = new Position(x,y);
        col = new Collider();
        this.gameCore = gameCore;

        gameCore.gameView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (!inMotion) {
                    int ex = (int) event.getX();
                    int ey = (int) event.getY();

                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN: // нажатие
                            prevPos.set(ex, ey);
                            break;
                        case MotionEvent.ACTION_MOVE: // движение
                            move(ex, ey);
                            prevPos.set(ex, ey);
                            break;
                        case MotionEvent.ACTION_UP: // отпускание
                            break;
                        case MotionEvent.ACTION_CANCEL:
                            break;
                    }
                    return true;
                }
                return false;
            }
        });
    }

    public static int getWidth(Camera camera, Context context) {
        Bitmap bitmapSource = BitmapFactory.decodeResource(context.getResources(), R.drawable.sample_player);

        int width = camera.canvas.getWidth();
        int height = camera.canvas.getHeight();

        int v = Math.min(width, height);

        return (int) ((((float)v/camera.scale)/(float)bitmapSource.getWidth()))*bitmapSource.getWidth();
    }

    public static int getHeight(Camera camera, Context context) {
        Bitmap bitmapSource = BitmapFactory.decodeResource(context.getResources(), R.drawable.sample_player);

        int width = camera.canvas.getWidth();
        int height = camera.canvas.getHeight();

        int v = Math.min(width, height);

        return ((v/camera.scale)/bitmapSource.getHeight())*bitmapSource.getHeight();
    }

    private void move(int ex, int ey) {
        Log.i("you see this flag?", "move: "+inMotion);
        if (!inMotion) {
            int deltaX = (int)prevPos.x-ex;
            int deltaY = (int)prevPos.y-ey;

            Log.i("Watch my move before", "move: "+deltaX+" "+deltaY);

            if (Math.abs(deltaX)<5) {
                deltaX = 0;
            }

            if (Math.abs(deltaY)<5) {
                deltaY = 0;
            }

            Log.i("Watch my move after", "move: "+deltaX+" "+deltaY);

            stepPos.x = 0;
            stepPos.y = 0;

            if (Math.abs(deltaX)>Math.abs(deltaY)) {
                if (deltaX>0) {
                    stepPos.x = -velocity;
                    side="left";
                } else if (deltaX<0) {
                    stepPos.x = velocity;
                    side="right";
                }
            } else {
                if (deltaY>0) {
                    stepPos.y = -velocity;
                    side="top";
                } else if (deltaY<0) {
                    stepPos.y = velocity;
                    side="bottom";
                }
            }

            if (!(stepPos.x==0)|| !(stepPos.y==0)) {
                inMotion = true;
            }
        }
    }

    private boolean checkBorders() {
        if (pos.x<=-1500) {
            pos.x = -1450;
            return true;
        }
        if (pos.x>=1500) {
            pos.x=1450;
            return true;
        }
        if (pos.y<=-1500) {
            pos.y = -1450;
            return true;
        }
        if (pos.y>=1500) {
            pos.y=1450;
            return true;
        }
        return false;
    }

    protected void assignTexture() {
        Bitmap bitmapSource = BitmapFactory.decodeResource(gameCore.gameContext.getResources(), R.drawable.sample_player);

        int width = gameCore.canvas.getWidth();
        int height = gameCore.canvas.getHeight();
        int v = Math.min(width, height);

        Matrix matrix = new Matrix();
        int degr = 0;

        switch (side) {
            case "bottom":
                degr=0;
                break;
            case "left":
                degr = 90;
                break;
            case "right":
                degr = 270;
                break;
            case "top":
                degr = 180;
                break;
        }

        if (!textureAssigned) {
            this.width = (((float)v/gameCore.camera.scale)/(float)bitmapSource.getWidth());
            this.height = (((float)v/gameCore.camera.scale)/(float)bitmapSource.getHeight());
            matrix.setScale( this.width, this.height);
        } else {
            matrix.setScale( this.width, this.height);
        }
        matrix.postRotate(degr);

        this.bitmap = Bitmap.createBitmap(bitmapSource, 0, 0, bitmapSource.getWidth(), bitmapSource.getHeight(), matrix, false);
        if (!textureAssigned) {
            col.set(pos, 1, 1);
            textureAssigned = true;
        }
    }

    @Override
    public void watchCollisions(ArrayList<Sprite> sprites) {
        for (Sprite sprite : sprites) {
            if (sprite.col.pos != null && sprite != this) {
                if (col.checkCollide(sprite.col)) {
                    collide(sprite);
                    sprite.collide(this);
                }
            }
        }
    }

    public void collide(Sprite sprite) {
        if (sprite.tag.equals("Wall")) {

            System.out.println("PL POS: "+pos);
            System.out.println("WALL POS: "+sprite.pos);

            if (side.equals("bottom")) {
                pos.y = sprite.pos.y-col.height;
            }
            else if (side.equals("top")) {
                pos.y = sprite.pos.y+col.height;
            }
            else if (side.equals("right")) {
                pos.x = sprite.pos.x-col.width;
            }
            else if (side.equals("left")) {
                pos.x = sprite.pos.x+col.width;
            }

            inMotion = false;
            stepPos.x = 0;
            stepPos.y = 0;
            Log.i("COLLIDER FOUND", "ok");
        }
    }

    @Override
    public void action() {
        pos.x = pos.x + stepPos.x;
        pos.y = pos.y + stepPos.y;
    }

    @Override
    public void draw(Position canvPos) {
        Paint paint = new Paint();
        assignTexture();

        gameCore.canvas.drawBitmap(bitmap, canvPos.x, canvPos.y, paint);

        if (checkBorders()) {
            inMotion = false;
            stepPos.x = 0;
            stepPos.y = 0;
            Log.i("BORDER FOUND", "fuck");
        }

    }
}
