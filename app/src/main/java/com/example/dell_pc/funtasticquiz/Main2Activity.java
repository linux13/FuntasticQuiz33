package com.example.dell_pc.funtasticquiz;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

public class Main2Activity extends AppCompatActivity implements MediaPlayer.OnPreparedListener {
    private  MediaPlayer mediaPlayer;

    Button start, instruction, about, exit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        initialize();
        StartAnimations();

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playMusic();

                Intent int1 = new Intent(Main2Activity.this, Start.class);

                startActivity(int1);
            }
        });

        instruction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playMusic();

                Intent int2 = new Intent(Main2Activity.this, Instruction.class);
                startActivity(int2);
            }
        });
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playMusic();

                Intent int3 = new Intent(Main2Activity.this, About.class);
                startActivity(int3);
            }
        });

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playMusic();
                finish();
                System.exit(0);
            }
        });

    }

    private void initialize() {
        start = findViewById(R.id.btnstart);
        instruction = findViewById(R.id.btninstruction);
        about = findViewById(R.id.btnabout);
        exit = findViewById(R.id.btnexit);
    }

    private void StartAnimations() {

       /* LinearLayout l = (LinearLayout) findViewById(R.id.lin_lay);
        l.clearAnimation();
        l.startAnimation(anim);
        */

        Animation anim = AnimationUtils.loadAnimation(this, R.anim.translate);
        Animation animleft = AnimationUtils.loadAnimation(this, R.anim.leftside);
        Animation animright = AnimationUtils.loadAnimation(this, R.anim.rightside);
        Animation animfromtop = AnimationUtils.loadAnimation(this, R.anim.fromtop);

        anim.reset();
        animleft.reset();
        animright.reset();
        animfromtop.reset();

        start.clearAnimation();
        start.startAnimation(anim);


        instruction.clearAnimation();
        instruction.startAnimation(animleft);
        exit.clearAnimation();
        exit.startAnimation(animright);

        about.clearAnimation();
        about.startAnimation(animfromtop);

    }
    private void playMusic() {
        if(mediaPlayer!=null)mediaPlayer.release();
            mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.buttonclick);

        mediaPlayer.setOnPreparedListener(this);

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                mediaPlayer.stop();
                mediaPlayer.release();
            }
        });

    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        mediaPlayer.start();
    }
}
