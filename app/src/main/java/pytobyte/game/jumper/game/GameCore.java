package pytobyte.game.jumper.game;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.view.View;

import androidx.fragment.app.Fragment;

import pytobyte.game.jumper.UI.Sounds;
import pytobyte.game.jumper.UI.fragments.dialog.DialogLose;
import pytobyte.game.jumper.UI.fragments.dialog.DialogWin;
import pytobyte.game.jumper.game.draw.DrawView;
import pytobyte.game.jumper.sprites.Player;
import pytobyte.game.jumper.sprites.Sprite;
import pytobyte.game.jumper.sprites.extensions.Position;
import pytobyte.game.jumper.sprites.living_event.DeadZone;
import pytobyte.game.jumper.R;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

public class GameCore implements Sounds {
    public Context gameContext = null;
    public DrawView gameView = null;
    public Canvas canvas = null;
    public Camera camera;
    public Activity activity;
    public Fragment fragment;
    public boolean extraShutdown = false;
    public String mode;
    public DeadZone deadZone;
    private final int arcadeDistance = 20;
    public boolean pause = false;

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
        if (mode.equals("arcade")) {
            deadZone.move();
        }
        ArrayList<Sprite> drawableSprites = calcActions();
        calcCollisions(drawableSprites);
        camera.draw(drawableSprites);

        if (mode.equals("arcade")) {
            deadZone.draw();
        }
    }

    public void resumeEvent() {
        if (mode.equals("arcade")) {
            deadZone.resume();
        }
        for (Sprite sprite : getSprites()) {
            if (sprite.tag.equals("Player")) {
                ((Player) sprite).resume();
                break;
            }
        }
    }

    public void startGame() {
        SharedPreferences sharedPreferences = activity.getSharedPreferences("points", MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = sharedPreferences.edit();
        prefEditor.putInt("count", 0);
        prefEditor.apply();

        sharedPreferences = activity.getSharedPreferences("level", MODE_PRIVATE);
        sharedPreferences.getString("mode", "");
        mode = sharedPreferences.getString("mode", null);

        if (mode.equals("arcade")) {
            deadZone = new DeadZone(this);
            deadZone.pos = new Position(0, (camera.pos.y+camera.height)/meshHeight);
        }
    }

    private void calcCollisions(ArrayList<Sprite> drawableSprites) {
        for (Sprite sprite : drawableSprites) {
            sprite.watchCollisions(levelLoader.sprites);
        }
    }

    private ArrayList<Sprite> calcActions() {
        boolean makeStepBack = false;
        ArrayList<Sprite> drawableSprites = new ArrayList<>();
        for (Sprite sprite : (ArrayList<Sprite>) getSprites().clone()) {
            Position canvPos = camera.toCanvasPos(sprite.pos);

            sprite.action();

            if (mode.equals("arcade")) {
                if (sprite.pos.y+sprite.col.height>deadZone.pos.y && sprite.tag.equals("Player")) {
                    lose();
                    break;
                }
                else if (sprite.pos.y>deadZone.pos.y) {
                    makeStepBack = true;
                    getSprites().remove(sprite);
                    continue;
                }
            }

            if (!(canvPos.x<-camera.border || canvPos.y<-camera.border || canvPos.x>camera.width+camera.border || canvPos.y>camera.height+camera.border)) {
                drawableSprites.add(sprite);
            }
        }

        if (makeStepBack) {
            deadZone.pos.y++;
            for (Sprite sprite : getSprites()) {
                sprite.pos.y++;
                if (sprite.tag.equals("Player")) {
                    ((Player)sprite).expectingPosition.y++;
                }
            }
        }

        if (mode.equals("arcade")) {
            if (camera.toCanvasY(deadZone.pos.y)>camera.height+camera.border) {
                deadZone.pos.y = (camera.pos.y+camera.height)/meshHeight;
            }

            float maxY = levelLoader.findMaxY(getSprites());
            if (maxY>-arcadeDistance) {
                try {
                    levelLoader.loadPartLevel(getSprites());
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
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
    }

    public void lose() {
        MediaPlayer loseSound = initSound(getActivity(), R.raw.lose_sound);
        playSound(loseSound, activity);
        extraShutdown = true;
        DialogLose dialoglose = new DialogLose(gameView, fragment);
        dialoglose.show(fragment.getParentFragmentManager(), "win");
    }

    public void win() {
        MediaPlayer winSound = initSoundOutOfCheck(getActivity(), R.raw.win_sound);
        playSound(winSound, activity);
        extraShutdown = true;
        DialogWin dialogWin = new DialogWin(gameView, fragment);
        dialogWin.show(fragment.getParentFragmentManager(), "win");
    }

    public void clearGame() {
        gameContext = null;
        gameView = null;
        canvas = null;
    }
}
