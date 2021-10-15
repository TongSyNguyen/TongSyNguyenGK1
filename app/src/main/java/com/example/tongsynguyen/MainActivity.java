package com.example.tongsynguyen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnClickListener{
    RecyclerView rcv;
    CustomAdapter adt;
    List<Song> mSongs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rcv = findViewById(R.id.rcv);
        mSongs = new ArrayList<>();
        adt  =new CustomAdapter(mSongs,this);

        mSongs.add(new Song("Hero","Enrique Iglesias",R.drawable.singer,R.raw.gia_nhu_ngay_dau));
        mSongs.add(new Song("Hero1","Enrique Iglesias1",R.drawable.singer,R.raw.nhac_a));
        mSongs.add(new Song("Hero2","Enrique Iglesias2",R.drawable.singer,R.raw.nhac_b));
        mSongs.add(new Song("Hero3","Enrique Iglesias3",R.drawable.singer,R.raw.nhac_d));
        mSongs.add(new Song("Hero4","Enrique Iglesias4",R.drawable.singer,R.raw.gia_nhu_ngay_dau));


        rcv.setHasFixedSize(true);
        rcv.setAdapter(adt);
        rcv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));
    }

    @Override
    public void itemClick(Song song) {
        Intent intent = new Intent(MainActivity.this, SongActivity.class);
        intent.putExtra("song",song);
        intent.putExtra("listSong", (Serializable) mSongs);
        intent.putExtra("index",mSongs.indexOf(song));
        startActivity(intent);

    }
}