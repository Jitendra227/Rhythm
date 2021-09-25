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
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
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
    double cur_pos , total_duration;
    int index = 0;

    Button bottomSheetBtn;

    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       if(checkAndRequestPermission()) {
           setAudio();
       }

       bottomSheetBtn = findViewById(R.id.bottom_sheet_btn);

       bottomSheetBtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               BottomSheetFragment bottomSheetFragment = new BottomSheetFragment();
               bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());
           }
       });
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
}