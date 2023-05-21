package pytobyte.game.jumper.sprites;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Paint;

import pytobyte.game.jumper.sprites.extensions.Collider;
import pytobyte.game.jumper.game.GameCore;
import pytobyte.game.jumper.sprites.extensions.Position;
import pytobyte.game.jumper.R;

import java.util.ArrayList;

public class Point extends Sprite {
    Bitmap bitmapSource;
    private boolean textureAssigned = false;

    {
        tag = "Point";
    }

    public Point(float x, float y, GameCore gameCore) {
        pos = new Position(x,y);
        col = new Collider();
        col.set(pos, 1, 1);
        this.gameCore = gameCore;
        bitmapSource = BitmapFactory.decodeResource(gameCore.gameContext.getResources(), POINT_0);
    }

    public static int getWidth(GameCore gameCore) {
        Bitmap bitmapSource = BitmapFactory.decodeResource(gameCore.gameContext.getResources(), POINT_0);

        int width = gameCore.canvas.getWidth();
        int height = gameCore.canvas.getHeight();

        int v = Math.min(width, height);

        return (int) ((((float)v/gameCore.camera.scale)/(float)bitmapSource.getWidth()))*bitmapSource.getWidth();
    }

    public static int getHeight(GameCore gameCore) {
        Bitmap bitmapSource = BitmapFactory.decodeResource(gameCore.gameContext.getResources(), POINT_0);

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
    public void collide(Sprite sprite) {
        if (sprite.tag.equals("Player")) {
            SharedPreferences sharedPreferences = gameCore.activity.getSharedPreferences("points", MODE_PRIVATE);
            SharedPreferences.Editor prefEditor = sharedPreferences.edit();
            prefEditor.putInt("count", sharedPreferences.getInt("count", 0)+1);
            prefEditor.apply();
            gameCore.getSprites().remove(this);
        }
    }

    @Override
    public void action() {}

    @Override
    public void draw(Position canvPos) {
        Paint paint = new Paint();

        assignTexture();

        gameCore.canvas.drawBitmap(this.bitmap, canvPos.x, canvPos.y, paint);
    }
}
