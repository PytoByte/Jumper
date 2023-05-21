package pytobyte.game.jumper.sprites;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Paint;

import pytobyte.game.jumper.sprites.extensions.Collider;
import pytobyte.game.jumper.game.GameCore;
import pytobyte.game.jumper.sprites.extensions.Position;
import pytobyte.game.jumper.R;

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
        bitmapSource = BitmapFactory.decodeResource(gameCore.gameContext.getResources(), WALL_0);
    }

    public Wall(float x, float y, int rotate, GameCore gameCore) {
        pos = new Position(x,y);
        col = new Collider();
        col.set(pos, 1, 1);
        this.gameCore = gameCore;
        this.rotate = rotate;
        bitmapSource = BitmapFactory.decodeResource(gameCore.gameContext.getResources(), WALL_0);
    }

    public void setType(int type) {
        try {
            bitmapSource = BitmapFactory.decodeResource(gameCore.gameContext.getResources(), getWallTexture(type));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static int getWidth(GameCore gameCore) {
        Bitmap bitmapSource = BitmapFactory.decodeResource(gameCore.gameContext.getResources(), WALL_0);

        int width = gameCore.canvas.getWidth();
        int height = gameCore.canvas.getHeight();

        int v = Math.min(width, height);

        return (int) ((((float)v/gameCore.camera.scale)/(float)bitmapSource.getWidth()))*bitmapSource.getWidth();
    }

    public static int getHeight(GameCore gameCore) {
        Bitmap bitmapSource = BitmapFactory.decodeResource(gameCore.gameContext.getResources(), WALL_0);

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
