package com.example.tongsynguyen;

import androidx.appcompat.app.AppCompatActivity;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class SongActivity extends AppCompatActivity {
    ImageView imageView, play, prev, next;
    TextView songTitle, songSinger;
    SeekBar mSeekBarTime, mSeekBarVol;

    List<Song> mSongs;
    Song song;
    static MediaPlayer mMediaPlayer;
    private Runnable runnable;
    private AudioManager mAudioManager;
    int currentIndex;

    TextView playerDuration, playerPosition;
    ImageView btFF, btRew;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song);

        imageView = findViewById(R.id.imageView9);
        play = findViewById(R.id.btnplay);
        prev = findViewById(R.id.btnprev);
        next = findViewById(R.id.btnnext);
        songTitle = findViewById(R.id.songTitle);
        songSinger = findViewById(R.id.songSinger);
        mSeekBarTime = findViewById(R.id.seekBarTime);
        mSeekBarVol = findViewById(R.id.seekBarVol);

        playerDuration = findViewById(R.id.playerDuration);
        playerPosition = findViewById(R.id.playerPosition);
        btFF = findViewById(R.id.btnff);
        btRew = findViewById(R.id.btnrew);

        //Bước 1: Lấy Song từ Intent
        song = (Song) getIntent().getSerializableExtra("song");
        mSongs = (List<Song>) getIntent().getSerializableExtra("listSong");
        currentIndex = getIntent().getIntExtra("index", 0);

        if (song != null) {
            if (mMediaPlayer != null) {
                mMediaPlayer.stop();
            }
            imageView.setImageResource(song.getImage());
            songTitle.setText(song.getTitle());
            songSinger.setText(song.getSingle());
            mMediaPlayer = MediaPlayer.create(getApplicationContext(), song.getResource());
            //set Text Duration
            playerDuration.setText(convertFormat(mMediaPlayer.getDuration()));
            mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mMediaPlayer.start();
                }
            });


        }
    }
    private String convertFormat(int duration) {
        return String.format("%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(duration),
                TimeUnit.MILLISECONDS.toSeconds(duration) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration)));
    }
}


