package com.example.project.sprites;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.example.project.R;
import com.example.project.UI.Sounds;
import com.example.project.game.Camera;
import com.example.project.sprites.extensions.Collider;
import com.example.project.game.GameCore;
import com.example.project.sprites.extensions.Position;

import java.util.ArrayList;

public class Player extends Sprite implements Sounds {
    {
        tag = "Player";
    }

    boolean inMotion = false;
    boolean useProtection = false;
    MediaPlayer soundStep;

    private final Position prevPos = new Position();
    private final Position stepPos = new Position();

    private final Position lastPosition = new Position();

    private final float velocity = 1.1f;
    private final long deltaTime = (long)(0.05f*1_000_000_000L);

    private boolean textureAssigned = false;
    private float width = 0;
    private float height = 0;

    private long timeBefore = 0;

    public Position expectingPosition = new Position();

    private Boolean lose = false;

    public Player(float x, float y, GameCore gameCore) {
        pos = new Position(x,y);
        col = new Collider();
        col.set(pos, 1, 1);
        this.gameCore = gameCore;
        rotate=0;
        timeBefore = System.nanoTime();
        soundStep = initSound(gameCore.activity, R.raw.step_sound);
        createMovement();
    }

    public Player(float x, float y, int rotate, GameCore gameCore) {
        pos = new Position(x,y);
        col = new Collider();
        col.set(pos, 1, 1);
        this.gameCore = gameCore;
        this.rotate = rotate;
        timeBefore = System.nanoTime();
        soundStep = initSound(gameCore.activity, R.raw.step_sound);
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
        Bitmap bitmapSource = BitmapFactory.decodeResource(context.getResources(), PLAYER_0);

        int width = camera.canvas.getWidth();
        int height = camera.canvas.getHeight();

        int v = Math.min(width, height);

        return (int) ((((float)v/camera.scale)/(float)bitmapSource.getWidth()))*bitmapSource.getWidth();
    }

    public static int getHeight(Camera camera, Context context) {
        Bitmap bitmapSource = BitmapFactory.decodeResource(context.getResources(), PLAYER_0);

        int width = camera.canvas.getWidth();
        int height = camera.canvas.getHeight();

        int v = Math.min(width, height);

        return ((v/camera.scale)/bitmapSource.getHeight())*bitmapSource.getHeight();
    }

    private void move(int ex, int ey) {
        if (!inMotion) {
            lastPosition.set(pos);
            int deltaX = (int)prevPos.x-ex;
            int deltaY = (int)prevPos.y-ey;

            if (Math.abs(deltaX)<5) {
                deltaX = 0;
            }

            if (Math.abs(deltaY)<5) {
                deltaY = 0;
            }

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

            //exceptWallHack();
        }
    }

    protected void assignTexture() {
        SharedPreferences sp = gameCore.activity.getSharedPreferences("shop", Context.MODE_PRIVATE);
        Bitmap bitmapSource = BitmapFactory.decodeResource(gameCore.gameContext.getResources(), sp.getInt("active", PLAYER_0));

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

    private float delta(float a, float b) {
        if (a>b) {
            return a-b;
        } else {
            return b-a;
        }
    }

    public void stop() {
        stepPos.x = 0;
        stepPos.y = 0;
        inMotion = false;
    }

    @Override
    public void watchCollisions(ArrayList<Sprite> sprites) {
        ArrayList<Sprite> spikes = new ArrayList<>();
        /*
        if (useProtection) {
            if (rotate == 0) {
                if (!(pos.y > expectingPosition.y)) {
                    pos.y = expectingPosition.y;
                }
            } else if (rotate == 180) {
                if (!(pos.y < expectingPosition.y)) {
                    pos.y = expectingPosition.y;
                }
            } else if (rotate == 270) {
                if (!(pos.x < expectingPosition.x)) {
                    pos.x = expectingPosition.x;
                }
            } else if (rotate == 90) {
                if (!(pos.x > expectingPosition.x)) {
                    pos.x = expectingPosition.x;
                }
            }
            useProtection= false;
        }*/

        for (Sprite sprite : (ArrayList<Sprite>) sprites.clone()) {
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

    public void exceptWallHack() {
        float closestX = pos.x;
        float closestY = pos.y;
        boolean first = true;
        try {
            for (Sprite sprite : (ArrayList<Sprite>) gameCore.getSprites().clone()) {
                if (sprite.tag.equals("Wall")) {
                    if (rotate == 0) {
                        if (first) {
                            closestY = sprite.pos.y;
                            first = false;
                        } else if (pos.y > sprite.pos.y && pos.y - col.height < closestY && sprite.pos.x == pos.x) {
                            closestY = sprite.pos.y - col.height;
                        }
                    } else if (rotate == 180) {
                        if (first) {
                            closestY = sprite.pos.y;
                            first = false;
                        } else if (pos.y < sprite.pos.y && pos.y + col.height > closestY && sprite.pos.x == pos.x) {
                            closestY = sprite.pos.y + col.height;
                        }
                    } else if (rotate == 270) {
                        if (first) {
                            closestX = sprite.pos.x;
                            first = false;
                        } else if (pos.x < sprite.pos.x && pos.x + col.width > closestX && sprite.pos.y == pos.y) {
                            closestX = sprite.pos.x + col.width;
                        }
                    } else if (rotate == 90) {
                        if (first) {
                            closestX = sprite.pos.x;
                            first = false;
                        } else if (pos.x > sprite.pos.x && pos.x - col.width < closestX && sprite.pos.y == pos.y) {
                            closestX = sprite.pos.x - col.width;
                        }
                    }
                }
            }
            useProtection = true;
        } catch (Exception er) {
            useProtection = false;
        }
        expectingPosition = new Position(closestX, closestY);
        Log.e("TAG", "except: "+expectingPosition);
        Log.e("TAG", "now: "+pos);
    }

    public void collide(Sprite sprite) {
        if (sprite.tag.equals("Wall")) {
            if (delta(lastPosition.x, sprite.pos.x)+delta(lastPosition.y, sprite.pos.y)>1) {
                stopPlay(soundStep, gameCore.activity);
                playSound(soundStep);
            }
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
            stop();

            Log.i("COLLIDER FOUND", "ok");
        } else if (sprite.tag.equals("Spike")) {
            lose = true;
        }
    }

    public void resume() {
        timeBefore = System.nanoTime();
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
    }
}
