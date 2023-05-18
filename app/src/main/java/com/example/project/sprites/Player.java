package com.example.project.sprites;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.example.project.game.Camera;
import com.example.project.sprites.extensions.Collider;
import com.example.project.game.GameCore;
import com.example.project.sprites.extensions.Position;
import com.example.project.R;

import java.util.ArrayList;

public class Player extends Sprite {
    {
        tag = "Player";
    }

    boolean inMotion = false;

    private final Position prevPos = new Position();
    private final Position stepPos = new Position();

    private final float velocity = 1.1f;
    private final long deltaTime = (long)(0.02f*1_000_000_000L);

    private boolean textureAssigned = false;
    private float width = 0;
    private float height = 0;

    private long timeBefore = 0;

    private Boolean lose = false;

    public Player(float x, float y, GameCore gameCore) {
        pos = new Position(x,y);
        col = new Collider();
        col.set(pos, 1, 1);
        this.gameCore = gameCore;
        rotate=0;
        timeBefore = System.nanoTime();
        createMovement();
    }

    public Player(float x, float y, int rotate, GameCore gameCore) {
        pos = new Position(x,y);
        col = new Collider();
        col.set(pos, 1, 1);
        this.gameCore = gameCore;
        this.rotate = rotate;
        timeBefore = System.nanoTime();
        createMovement();
    }

    private void createMovement() {
        gameCore.fragment.getView().setOnTouchListener(new View.OnTouchListener() {
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
                    rotate=90;
                } else if (deltaX<0) {
                    stepPos.x = velocity;
                    rotate=270;
                }
            } else {
                if (deltaY>0) {
                    stepPos.y = -velocity;
                    rotate=180;
                } else if (deltaY<0) {
                    stepPos.y = velocity;
                    rotate=0;
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

        if (!textureAssigned) {
            this.width = (((float)v/gameCore.camera.scale)/(float)bitmapSource.getWidth());
            this.height = (((float)v/gameCore.camera.scale)/(float)bitmapSource.getHeight());
            matrix.setScale( this.width, this.height);
            textureAssigned = true;
        } else {
            matrix.setScale( this.width, this.height);
        }
        matrix.postRotate(rotate);

        this.bitmap = Bitmap.createBitmap(bitmapSource, 0, 0, bitmapSource.getWidth(), bitmapSource.getHeight(), matrix, false);
    }

    @Override
    public void watchCollisions(ArrayList<Sprite> sprites) {
        ArrayList<Sprite> spikes = new ArrayList<>();

        for (Sprite sprite : sprites) {
            if (sprite.col.pos != null && sprite != this) {
                if (col.checkCollide(sprite.col)) {
                    if (sprite.tag.equals("Spike")) {
                        spikes.add(sprite);
                    }
                    collide(sprite);
                    sprite.collide(this);
                }
            }
        }

        if (lose) {
            lose = false;
            for (Sprite sprite : spikes) {
                if (sprite.col.pos != null && sprite != this) {
                    if (col.checkCollide(sprite.col)) {
                        collide(sprite);
                        sprite.collide(this);
                    }
                }
            }
            if (lose) {
                gameCore.lose();
            }
        }
    }

    public void collide(Sprite sprite) {
        if (sprite.tag.equals("Wall")) {
            stepPos.x = 0;
            stepPos.y = 0;

            System.out.println("PL POS: "+pos);
            System.out.println("WALL POS: "+sprite.pos);

            if (rotate==0) {
                pos.y = sprite.pos.y-col.height;
            }
            else if (rotate==180) {
                pos.y = sprite.pos.y+col.height;
            }
            else if (rotate==270) {
                pos.x = sprite.pos.x-col.width;
            }
            else if (rotate==90) {
                pos.x = sprite.pos.x+col.width;
            }
            inMotion = false;
            Log.i("COLLIDER FOUND", "ok");
        } else if (sprite.tag.equals("Spike")) {
            lose = true;
        }
    }

    @Override
    public void action() {
        pos.x = pos.x + stepPos.x*(System.nanoTime()-timeBefore)/deltaTime;
        pos.y = pos.y + stepPos.y*(System.nanoTime()-timeBefore)/deltaTime;
        timeBefore = System.nanoTime();
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
