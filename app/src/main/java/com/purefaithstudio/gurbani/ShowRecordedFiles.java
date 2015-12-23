package com.purefaithstudio.gurbani;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.FileFilter;

/**
 * Created by MY System on 12/16/2015.
 */
public class ShowRecordedFiles extends Fragment {

    private String folder = null;
    private View rootView;
    private File[] files;
    private PlayerControllerMp3 playerController;
    private ImageView playIcon;
    private boolean toggle;
    private Wait wait;
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            wait.dismiss();
            Toggler.checkSetState(playIcon);
        }
    };
    private Context context;

    public ShowRecordedFiles() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
            folder = getArguments().getString("path");
        playerController = new PlayerControllerMp3(getActivity().getApplicationContext());
        context = getActivity().getApplicationContext();
        context.registerReceiver(receiver, new IntentFilter("com.purefaithstudio.gurbani.Mp3Player"));
        //reset state of play icon(this is required if u ar calling stopservice in ondestroy)
        Toggler.resetStates();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.show_recorded, null);
        ListView listView = (ListView) rootView.findViewById(R.id.list);
        playIcon = (ImageView) rootView.findViewById(R.id.play);
        Toggler.checkSetState(playIcon);
        files = getFilesList();
        MyRecordsList adapter = new MyRecordsList(context, R.layout.custom_row_hukum, files);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                File file = files[position];
                playAudio(file.getAbsolutePath());
            }
        });
        playIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Toggler.ifStateNull()) {
                    if (Toggler.check()) {//we ar pausing audio
                        playerController.audioPause();
                        Toggler.checkSetState(playIcon);
                    } else {//resuming audio
                        playerController.audioResume();
                        Toggler.checkSetState(playIcon);
                    }
                } else
                    Toast.makeText(context, "Select Any Item to Play!!", Toast.LENGTH_SHORT).show();

            }
        });
        return rootView;
    }


    private void playAudio(String path) {
        Log.i("RecordShow", "play Path " + path);
        wait = new Wait();
        wait.show(getFragmentManager(), "tag");
        playerController.type = 0;
        playerController.play(path);
    }

    public File[] getFilesList() {
        File file = new File(folder);

        FileFilter fileFilter = new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                String name = pathname.getName();
                Log.i("RecordShow", "Here " + pathname + " " + name);
                if (name.substring(name.length() - 4, name.length()).equals(".mp3"))
                    return true;
                return false;
            }
        };
        File[] files = file.listFiles(fileFilter);
        Log.i("RecordShow", "first File " + files[0].getName().toString());
        return files;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (MainActivity.isMyServiceRunning(Mp3PlayerService.class, rootView.getContext())) {
            playerController.stop();
        }
        context.unregisterReceiver(receiver);
    }
}
