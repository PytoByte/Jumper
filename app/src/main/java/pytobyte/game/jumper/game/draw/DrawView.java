package pytobyte.game.jumper.game.draw;

import android.content.Context;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.fragment.app.Fragment;

public class DrawView extends SurfaceView implements SurfaceHolder.Callback {

    public DrawThread drawThread;
    public Context context;
    public Fragment fragment;
    public SurfaceHolder surfaceHolder;

    public DrawView(SurfaceHolder surfaceHolder, Context context, Fragment fragment) {
        super(context);
        this.surfaceHolder = surfaceHolder;
        surfaceHolder.addCallback(this);
        this.context = context;
        this.fragment = fragment;
    }
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        drawThread = new DrawThread(holder, this);
        drawThread.start();
        // создание SurfaceView
    }
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        // изменение размеров SurfaceView
    }
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.e("wdffw", "surfaceDestroyed: hello?");
        drawThread.requestStop();
        boolean retry = true;
        while (retry) {
            try {
                drawThread.join();
                retry = false;
            } catch (InterruptedException e) {
                Log.e("What?", "surfaceDestroyed: "+e);
            }
        }
    }
}