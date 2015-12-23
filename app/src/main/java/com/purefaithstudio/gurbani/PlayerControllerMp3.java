package com.purefaithstudio.gurbani;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by MY System on 12/16/2015.
 */
public class PlayerControllerMp3 {
    private final Context context;
    private final Intent intent;
    private boolean isPlaying;
    public int type;

    public PlayerControllerMp3(Context context) {
        this.context = context;
        intent = new Intent(context, Mp3PlayerService.class);
    }

    public void stop() {
        try {
            context.stopService(intent);
            isPlaying = false;
        } catch (Exception e) {
            Log.i("Playercheck", "cannot stop");
            e.printStackTrace();
        }

    }

    public void play(String path) {
        try {
            if (isPlaying == true) {
                stop();
            }
            isPlaying = true;
            Bundle b = new Bundle();
            b.putString("url", path);
            b.putInt("type", type);
            intent.putExtras(b);
            context.startService(intent);
        } catch (Exception e) {
            Log.i("Playercheck", "cannot play");
            e.printStackTrace();
        }
    }

    public void audioPause() {
        try {
            if (Toggler.check()) {
                Mp3PlayerService.player.pause();
            }
        } catch (Exception e) {
            Log.i("Playercheck", "cannot pause");
            e.printStackTrace();
        }

    }

    public void audioResume() {
        try {
            if (!Toggler.check()) {
                Mp3PlayerService.player.start();
            }
        } catch (Exception e) {
            Log.i("Playercheck", "cannot resume");
            e.printStackTrace();
        }


    }
}
