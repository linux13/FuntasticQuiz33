package com.example.dell_pc.funtasticquiz;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class FinalResultFailSplash extends AppCompatActivity implements MediaPlayer.OnPreparedListener {
        MediaPlayer mediaPlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_result_fail_splash);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        playMusic();
        Thread timer = new Thread(){

            public void run(){
                try{
                    sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }finally {
                    Intent intent = new Intent(FinalResultFailSplash.this,FinalResult.class);
                    startActivity(intent);
                    finish();
                }
            }
        };
        timer.start();
    }
    private void playMusic() {
        if(mediaPlayer!=null)mediaPlayer.release();
            mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.levelfailed);

        mediaPlayer.setOnPreparedListener(this);

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                mediaPlayer.stop();
            }
        });

    }
    @Override
    protected void onPause(){
        super.onPause();
        finish();
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        mediaPlayer.start();
    }
}
