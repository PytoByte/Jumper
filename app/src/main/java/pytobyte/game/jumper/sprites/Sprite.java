package pytobyte.game.jumper.sprites;

import android.graphics.Bitmap;

import pytobyte.game.jumper.sprites.extensions.BitmapBank;
import pytobyte.game.jumper.sprites.extensions.Collider;
import pytobyte.game.jumper.game.GameCore;
import pytobyte.game.jumper.sprites.extensions.Position;

import java.util.ArrayList;

public abstract class Sprite implements BitmapBank {
    public String tag;
    public Position pos;
    public Collider col;
    Bitmap bitmap;
    public GameCore gameCore;
    public int rotate;

    public abstract void draw(Position canvPos);

    protected abstract void assignTexture();

    public abstract void watchCollisions(ArrayList<Sprite> sprites);

    public abstract void collide(Sprite sprite);

    public abstract void action();

    public void setPos(float x, float y) {
        pos.set(x,y);
    }

    public void setPos(Position pos) {
        this.pos.set(pos);
    }

    public float getX() {
        return pos.getX();
    }

    public float getY() {
        return pos.getY();
    }
}
