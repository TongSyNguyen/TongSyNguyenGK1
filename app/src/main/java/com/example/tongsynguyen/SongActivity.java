package com.example.tongsynguyen;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
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
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        int maxV = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int curV = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        mSeekBarVol.setMax(maxV);
        mSeekBarVol.setProgress(curV);

        mSeekBarVol.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC,progress,0);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSeekBarTime.setMax(mMediaPlayer.getDuration());
                if(mMediaPlayer != null && mMediaPlayer.isPlaying()){
                    mMediaPlayer.pause();
                    play.setImageResource(R.drawable.pause);
                }else{
                    mMediaPlayer.start();
                    play.setImageResource(R.drawable.play);
                }
                SongNames();
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mMediaPlayer != null){
                    play.setImageResource(R.drawable.play);
                }
                if(currentIndex < mSongs.size() - 1){
                    currentIndex++;
                }else{
                    currentIndex = 0;
                }
                if(mMediaPlayer.isPlaying()){
                    mMediaPlayer.stop();;
                }
                mMediaPlayer = MediaPlayer.create(getApplicationContext(),mSongs.get(currentIndex).getResource());
                mMediaPlayer.start();
                SongNames();
            }
        });
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mMediaPlayer != null){
                    play.setImageResource(R.drawable.play);
                }
                if(currentIndex > 0){
                    currentIndex--;
                }else{
                    currentIndex = mSongs.size()-1;
                }
                if(mMediaPlayer.isPlaying()){
                    mMediaPlayer.stop();
                }
                mMediaPlayer = MediaPlayer.create(getApplicationContext(),mSongs.get(currentIndex).getResource());
                mMediaPlayer.start();
                SongNames();
            }
        });
        btRew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentPosition = mMediaPlayer.getCurrentPosition();
                if(mMediaPlayer.isPlaying() && currentPosition > 5000){
                    currentPosition = currentPosition-5000;
                    mMediaPlayer.seekTo(currentPosition);

                    playerPosition.setText(convertFormat(currentPosition));
                }
            }
        });
        btFF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentPosition = mMediaPlayer.getCurrentPosition();
                int duration = mMediaPlayer.getDuration();
                if(mMediaPlayer.isPlaying() && duration!= currentPosition){
                    currentPosition = currentPosition+5000;
                    mMediaPlayer.seekTo(currentPosition);

                    playerPosition.setText(convertFormat(currentPosition));
                }
            }
        });
    }
    private String convertFormat(int duration) {
        return String.format("%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(duration),
                TimeUnit.MILLISECONDS.toSeconds(duration) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration)));
    }
    private void SongNames(){
        Song song = mSongs.get(currentIndex);
        imageView.setImageResource(song.getImage());
        songTitle.setText(song.getTitle());
        songSinger.setText(song.getSingle());

        //seek bar duration
        mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mSeekBarTime.setMax(mMediaPlayer.getDuration());
                mMediaPlayer.start();
            }
        });
        mSeekBarTime.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser){
                    mMediaPlayer.seekTo(progress);
                    mSeekBarTime.setProgress(progress);
                }
                playerPosition.setText(convertFormat(mMediaPlayer.getCurrentPosition()));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (mMediaPlayer!= null){
                    try {
                        if(mMediaPlayer.isPlaying()){
                            Message message = new Message();

                            message.what = mMediaPlayer.getCurrentPosition();
                            handler.sendMessage(message);
                            Thread.sleep(1000);
                        }
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
    @SuppressLint("Handle Leak")
    Handler handler = new Handler () {
        @Override
        public void handleMessage (Message msg) {
            mSeekBarTime.setProgress(msg.what);
        }
    };
}


