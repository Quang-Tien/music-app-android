package com.example.musicapp;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    TextView tvSongName, tvCurrent, tvTotal;
    SeekBar skSong;
    ImageButton ibPrev, ibStop, ibPlay, ibNext;
    ImageView imgDvd;
    ArrayList<Song> arraySong;
    Animation animation;
    int pos = 0;
    MediaPlayer mediaPlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        animation = AnimationUtils.loadAnimation(this, R.anim.dvd_rotate);
        Mapping();
        addSong();

        mediaInitialize();

        ibPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pos--;
                if(pos == -1){
                    pos = arraySong.size() - 1;
                }
                if(mediaPlayer.isPlaying()){
                    mediaPlayer.stop();
                }
                mediaInitialize();
                mediaPlayer.start();
                ibPlay.setImageResource(R.drawable.ic_pause);
                setTotalTime();
                updateSongTime();
            }
        });

        ibNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pos++;
                if(pos > arraySong.size() - 1){
                    pos = 0;
                }
                if(mediaPlayer.isPlaying()){
                    mediaPlayer.stop();
                }
                mediaInitialize();
                mediaPlayer.start();
                ibPlay.setImageResource(R.drawable.ic_pause);
                setTotalTime();
                updateSongTime();
            }
        });

        ibStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.stop();
                mediaPlayer.release();
                ibPlay.setImageResource(R.drawable.ic_play);
                mediaInitialize();
            }
        });

        ibPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mediaPlayer.isPlaying()){
                    mediaPlayer.pause();
                    ibPlay.setImageResource(R.drawable.ic_play);
                }
                else{
                    mediaPlayer.start();
                    ibPlay.setImageResource(R.drawable.ic_pause);
                }
                setTotalTime();
                updateSongTime();
                imgDvd.startAnimation(animation);
            }
        });

        skSong.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(skSong.getProgress());
            }
        });
    }

    private void updateSongTime(){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                SimpleDateFormat dateFormat = new SimpleDateFormat("mm:ss");
                tvCurrent.setText(dateFormat.format(mediaPlayer.getCurrentPosition()));
                skSong.setProgress(mediaPlayer.getCurrentPosition());
                //check het time de chuyen bai
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        pos++;
                        if(pos > arraySong.size() - 1){
                            pos = 0;
                        }
                        if(mediaPlayer.isPlaying()){
                            mediaPlayer.stop();
                        }
                        mediaInitialize();
                        mediaPlayer.start();
                        ibPlay.setImageResource(R.drawable.ic_pause);
                        setTotalTime();
                        updateSongTime();
                    }
                });
                handler.postDelayed(this, 500);
            }
        }, 100);
    }

    private void setTotalTime(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("mm:ss");
        tvTotal.setText(dateFormat.format(mediaPlayer.getDuration()));
        skSong.setMax(mediaPlayer.getDuration());
    }

    private void mediaInitialize(){
        mediaPlayer = MediaPlayer.create(MainActivity.this, arraySong.get(pos).getResId());
        tvSongName.setText(arraySong.get(pos).getTitle());
    }
    private void addSong(){
        arraySong = new ArrayList<>();
        arraySong.add(new Song("Industry Baby", R.raw.industry_mp3));
        arraySong.add(new Song("Memories", R.raw.memories_mp3));
        arraySong.add(new Song("Work From Home", R.raw.wfh_mp3));
    }

    private void Mapping(){
        tvSongName = findViewById(R.id.textViewSongName);
        tvCurrent = findViewById(R.id.textViewCurrent);
        tvTotal = findViewById(R.id.textViewTotal);
        skSong = findViewById(R.id.seekBar);
        ibPrev = findViewById(R.id.imageButtonPrev);
        ibStop = findViewById(R.id.imageButtonStop);
        ibPlay = findViewById(R.id.imageButtonPlay);
        ibNext = findViewById(R.id.imageButtonNext);
        imgDvd = findViewById(R.id.imageViewDVD);
    }
}