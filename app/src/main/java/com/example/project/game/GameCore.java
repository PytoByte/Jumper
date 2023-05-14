package com.example.project.game;

import static androidx.navigation.fragment.FragmentKt.findNavController;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

import androidx.fragment.app.Fragment;

import com.example.project.R;
import com.example.project.UI.activities.MainActivity;
import com.example.project.UI.fragments.dialog.DialogLose;
import com.example.project.UI.fragments.dialog.DialogWin;
import com.example.project.databinding.FragmentGameUiBinding;
import com.example.project.game.draw.DrawView;
import com.example.project.sprites.Sprite;
import com.example.project.sprites.extensions.Position;

import java.util.ArrayList;

public class GameCore {
    public Context gameContext = null;
    public DrawView gameView = null;
    public Canvas canvas = null;
    public Camera camera;
    public Activity activity;
    public Fragment fragment;
    public boolean extraShutdown = false;

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public LevelLoader getLevel() {
        return levelLoader;
    }

    public void setLevel(LevelLoader levelLoader) {
        this.levelLoader = levelLoader;
        camera.setLockPos(levelLoader.player.pos);
    }

    public LevelLoader levelLoader;

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
        return levelLoader.sprites;
    }

    public void setSprites(ArrayList<Sprite> sprites) {
        levelLoader.sprites = sprites;
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

    public void setGameView(DrawView dw) {
        this.gameContext = dw.context;
        this.activity = dw.fragment.getActivity();
        this.fragment = dw.fragment;
        this.gameView = dw;
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
            sprite.watchCollisions(levelLoader.sprites);
        }
    }

    private ArrayList<Sprite> calcActions() {
        ArrayList<Sprite> drawableSprites = new ArrayList<>();
        for (Sprite sprite : levelLoader.sprites) {
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

    public void lose() {
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        paint.setTextSize(100);
        paint.setFakeBoldText(true);
        canvas.drawText("YOU DEAD", canvas.getWidth()/2-20, canvas.getHeight()/2, paint);
        if (activity instanceof MainActivity) {
            extraShutdown = true;
            DialogLose dialoglose = new DialogLose(gameView, fragment);
            dialoglose.show(fragment.getParentFragmentManager(), "win");
        }
    }

    public void win() {
        if (activity instanceof MainActivity) {
            extraShutdown = true;
            DialogWin dialogWin = new DialogWin(gameView, fragment);
            dialogWin.show(fragment.getParentFragmentManager(), "win");
        }
    }

    public void clearGame() {
        gameContext = null;
        gameView = null;
        canvas = null;
    }
}
