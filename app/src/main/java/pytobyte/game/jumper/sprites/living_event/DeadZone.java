package pytobyte.game.jumper.sprites.living_event;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import pytobyte.game.jumper.game.GameCore;
import pytobyte.game.jumper.sprites.extensions.Position;

public class DeadZone {
    public Position pos;
    public GameCore gameCore;
    public final Position stepPos = new Position(0, -0.05f);
    long timeBefore;
    final long deltaTime = (long)(0.1f*1_000_000_000L);

    public DeadZone(GameCore gameCore) {
        this.gameCore = gameCore;
    }

    public void resume() {
        timeBefore = System.nanoTime();
    }

    public void move() {
        pos.x = pos.x + stepPos.x*(System.nanoTime()-timeBefore)/deltaTime;
        pos.y = pos.y + stepPos.y*(System.nanoTime()-timeBefore)/deltaTime;
        timeBefore = System.nanoTime();
    }

    public void draw() {
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        gameCore.canvas.drawRect(new Rect(0, gameCore.camera.toCanvasY(pos.y), gameCore.canvas.getWidth(), gameCore.canvas.getHeight()), paint);
    }
}
