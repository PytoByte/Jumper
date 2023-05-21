package com.example.project.UI;

import android.app.Activity;
import android.media.MediaPlayer;
import android.widget.Toast;

import com.example.project.R;

public interface Sounds {
    default MediaPlayer initSound(Activity activity, int soundID) {
        MediaPlayer mPlayer= MediaPlayer.create(activity, soundID);
        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                stopPlay(mPlayer, activity);
            }
        });

        return mPlayer;
    }

    default void playSound(MediaPlayer mPlayer) {
        mPlayer.start();
    }

    default void stopPlay(MediaPlayer mPlayer, Activity activity){
        mPlayer.stop();
        try {
            mPlayer.prepare();
            mPlayer.seekTo(0);
        }
        catch (Throwable t) {
            Toast.makeText(activity.getBaseContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
