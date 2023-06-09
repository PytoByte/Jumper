package pytobyte.game.jumper.game;

import android.graphics.Canvas;

import pytobyte.game.jumper.sprites.Sprite;
import pytobyte.game.jumper.sprites.extensions.Position;

import java.util.ArrayList;

public class Camera {
    Position pos = new Position();
    public Canvas canvas;
    public int width = 0;
    public int height = 0;
    public int scale = 12;
    public GameCore gameCore;
    public final int border = 5;

    public GameCore getGameCore() {
        return gameCore;
    }

    public void setGameCore(GameCore gameCore) {
        this.gameCore = gameCore;

        if (gameCore.canvas!=null) {
            this.canvas = gameCore.canvas;
            width = canvas.getWidth();
            height = canvas.getHeight();
            lockPos = new Position(width/2, height/2);
        }
    }

    Position lockPos = null;

    Camera(Canvas canvas) {
        this.canvas = canvas;
        width = canvas.getWidth();
        height = canvas.getHeight();
        lockPos = new Position(width/2, height/2);
    }

    public Camera() {}

    public void setCanvas(Canvas canvas) {
        this.canvas = canvas;
        width = canvas.getWidth();
        height = canvas.getHeight();
        if (lockPos==null) {
            lockPos = new Position();
        }
    }

    public void setLockPos(Position lockPos) {
        this.lockPos = lockPos;
    }

    public void updateLockedPosition() {
        Position newPos = toCanvasPos(lockPos);
        pos.set(newPos.x- (width/2) +pos.x, newPos.y- (height/2) +pos.y);
    }

    public Position toCanvasPos(Position position) {
        return new Position(toCanvasX(position.x), toCanvasY(position.y));
    }

    public int toCanvasX(float x) {
        return (int)((x*gameCore.meshWidth)-pos.x);
    }

    public int toCanvasY(float y) {
        return (int)((y*gameCore.meshHeight)-pos.y);
    }

    public void draw() {
        for (Sprite sprite : gameCore.levelLoader.sprites) {
            updateLockedPosition();
            Position canvPos = toCanvasPos(sprite.pos);
            if (!(canvPos.x<0 || canvPos.y<0 || canvPos.x>width || canvPos.y>height)) {
                sprite.draw(toCanvasPos(sprite.pos));
            }
        }
    }

    public void draw(ArrayList<Sprite> drawableSprites) {
        updateLockedPosition();
        for (Sprite sprite : drawableSprites) {
            sprite.draw(toCanvasPos(sprite.pos));
        }
    }
}
