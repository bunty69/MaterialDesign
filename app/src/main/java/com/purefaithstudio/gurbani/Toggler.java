package com.purefaithstudio.gurbani;

import android.util.Log;
import android.widget.ImageView;

/**
 * Created by MY System on 12/16/2015.
 */
public class Toggler {
    private static boolean state;
    private static boolean stateNull;
    private ImageView playIcon;

    public Toggler() {

    }

    public static void checkSetState(ImageView playIcon) {
        try {
            if (Toggler.check()) {
                playIcon.setImageResource(R.drawable.stop_blue);
            } else {
                playIcon.setImageResource(R.drawable.play);
            }
        } catch (IllegalStateException e) {
            Log.i("Toggler", "Cannot toggle");
            e.printStackTrace();
        }
    }

    public static boolean check() throws IllegalStateException {
        if (Mp3PlayerService.player != null) {
            if (Mp3PlayerService.player.isPlaying()) {
                state = true;//play state
            } else {
                state = false;//pause/stop state
            }
            stateNull = false;
        } else {
            state = false;
            stateNull = true;
        }

        return state;
    }

    public static void resetStates() {
        state = false;
    }

    public static boolean ifStateNull() {
        return stateNull;
    }
}
