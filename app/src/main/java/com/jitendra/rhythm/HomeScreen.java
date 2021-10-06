package com.jitendra.rhythm;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.exoplayer2.ExoPlayer;
import com.jitendra.rhythm.model.AudioItems;
import com.jitendra.rhythm.practice.BottomSheetFragment;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static com.jitendra.rhythm.R.id.current_time_txt;

public class HomeScreen extends AppCompatActivity {



    ArrayList<AudioItems> audioItemsList;
    RecyclerView recyclerView;
    ExoPlayer exoPlayer;
    TextView audioName, currentAudio, total;
    ImageView prev, next, pause;
    SeekBar seekBar;
    MediaPlayer mediaPlayer;
    double cur_pos , total_duration;
    int index = 0;

    Button bottomSheetBtn;

    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (checkAndRequestPermission()) {
            setAudio();
        }
    }

    private void setAudio() {
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        audioName = findViewById(R.id.audio_name);
        currentAudio = findViewById(current_time_txt);
        total = findViewById(R.id.total_time_txt);
        prev = findViewById(R.id.prev_btn);
        next = findViewById(R.id.next_btn);
        pause = findViewById(R.id.pause_btn);
        seekBar = findViewById(R.id.seekbar);

        audioItemsList = new ArrayList<>();
        //exoPlayer = new ExoPlayer();
        mediaPlayer = new MediaPlayer();
        
        getAudioFiles();

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                cur_pos = seekBar.getProgress();
                mediaPlayer.seekTo((int) cur_pos);
            }
        });

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                index++;
                if (index < (audioItemsList.size())) {
                    playAudio(index);
                } else {
                    index = 0;
                    playAudio(index);
                }
            }
        });

        if (!audioItemsList.isEmpty()) {
            playAudio(index);
            prevAudio();
            nextAudio();
            setPause();
        }
    }

    private void playAudio(int pos) {
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(this, audioItemsList.get(pos).getAudioUri());
            mediaPlayer.prepare();
            mediaPlayer.start();
            pause.setImageResource(R.drawable.exo_ic_pause_circle_filled);
            audioName.setText(audioItemsList.get(pos).getAudioTitle());
            index = pos;
        } catch (Exception e) {
            e.printStackTrace();
        }
        setAudioProgress();
    }

    private void setAudioProgress() {
        cur_pos = mediaPlayer.getCurrentPosition();
        total_duration = mediaPlayer.getDuration();

        total.setText(timerConvertor((long) total_duration));
        currentAudio.setText(timerConvertor((long) cur_pos));
        seekBar.setMax((int) total_duration);
        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    cur_pos = mediaPlayer.getCurrentPosition();
                    currentAudio.setText(timerConvertor((long) cur_pos));
                    seekBar.setProgress((int) cur_pos);
                    handler.postDelayed(this, 1000);
                } catch (IllegalStateException ed){
                    ed.printStackTrace();
                }
            }
        };
        handler.postDelayed(runnable, 1000);
    }

    private void prevAudio() {
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (index > 0) {
                    index --;
                    playAudio(index);
                } else {
                    index = audioItemsList.size() - 1;
                    playAudio(index);
                }
            }
        });
    }

    private void nextAudio() {
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (index < (audioItemsList.size()-1)) {
                    index++;
                    playAudio(index);
                } else {
                    index = 0;
                    playAudio(index);
                }
            }
        });
    }

    private void setPause() {
        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    pause.setImageResource(R.drawable.exo_ic_play_circle_filled);
                } else {
                    mediaPlayer.start();
                    pause.setImageResource(R.drawable.ic_baseline_pause_circle_filled_24);
                }
            }
        });
    }

    @SuppressLint("DefaultLocale")
    private String timerConvertor(long value) {
        String audioTime;
         int dur = (int) value;
         int hrs = (dur / 360000);
         int mns = (dur / 6000) % 60000;
         int sec = dur % 60000 / 1000;

         if (hrs > 1) {
             audioTime = String.format("%02d:%02d:%02d", hrs, mns, sec);
         } else {
             audioTime = String.format("%02d:%02d", mns, sec);
         }
         return audioTime;
    }

    private void getAudioFiles() {
        ContentResolver contentResolver = getContentResolver();
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

        Cursor cursor = contentResolver.query(uri, null, null, null, null);

        //looping through all rows and adding to list
        if (cursor != null && cursor.moveToFirst()) {
            do {

                String title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
                String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                String duration = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
                String url = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                 AudioItems audioItems = new AudioItems();
                audioItems.setAudioTitle(title);
                audioItems.setAudioArtist(artist);
                audioItems.setAudioUri(Uri.parse(url));
                audioItems.setDuration(duration);
                audioItemsList.add(audioItems);

            } while (cursor.moveToNext());
        }

        AudioAdapter adapter = new AudioAdapter(this, audioItemsList);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new AudioAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int pos, View v) {
                playAudio(pos);
            }
        });
    }

    private boolean checkAndRequestPermission() {
        int storageWrite = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int storageRead = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        List<String> listPermissionsNeeded = new ArrayList<>();

        if (storageWrite != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (storageRead != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray
                    (new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer!=null){
            mediaPlayer.release();
        }
    }
}