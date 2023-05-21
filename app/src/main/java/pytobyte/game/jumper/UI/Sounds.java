package pytobyte.game.jumper.UI;

import android.app.Activity;
import android.media.MediaPlayer;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public interface Sounds {
    HashMap<Integer, MediaPlayer> soundBank = new HashMap<>();
    HashMap<Integer, MediaPlayer> soundBankBreakers = new HashMap<>();

    default MediaPlayer initSound(Activity activity, int soundID) {
        if (!soundBank.containsKey(soundID)) {
            MediaPlayer mPlayer= MediaPlayer.create(activity, soundID);
            mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    stopPlay(mPlayer, activity);
                }
            });
            soundBank.put(soundID, mPlayer);
            return mPlayer;
        } else {
            System.out.println(soundID+" already exist");
            return soundBank.get(soundID);
        }
    }

    default MediaPlayer initSoundOutOfCheck(Activity activity, int soundID) {
            MediaPlayer mPlayer= MediaPlayer.create(activity, soundID);
            soundBankBreakers.put(soundID, mPlayer);
            mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    stopPlay(mPlayer, activity);
                    soundBankBreakers.remove(mp);
                }
            });
            return mPlayer;
    }

    default void playSound(MediaPlayer mPlayer, Activity activity) {
        if (!(soundBankBreakers.size()>0) || (soundBankBreakers.containsValue(mPlayer))) {
            for (MediaPlayer sound : soundBank.values()) {
                stopPlay(sound, activity);
            }
            mPlayer.start();
        }

        if (soundBankBreakers.containsValue(mPlayer)) {
            soundBankBreakers.clear();
        }
    }

    default void playSoundOutOfCheck(MediaPlayer mPlayer) {
        mPlayer.start();
    }

    default void stopPlay(MediaPlayer mPlayer, Activity activity){
        if (mPlayer.isPlaying()) {
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
}
