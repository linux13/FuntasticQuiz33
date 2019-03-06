package com.example.dell_pc.funtasticquiz;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.graphics.ColorUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import static android.graphics.Color.parseColor;
import static com.example.dell_pc.funtasticquiz.Start.aNo;
import static com.example.dell_pc.funtasticquiz.Start.extremescore;
import static com.example.dell_pc.funtasticquiz.Start.hardqcount;
import static com.example.dell_pc.funtasticquiz.Start.hardscore;
import static com.example.dell_pc.funtasticquiz.Start.hintcount;
import static com.example.dell_pc.funtasticquiz.Start.level;
import static com.example.dell_pc.funtasticquiz.Start.mscore;
import static com.example.dell_pc.funtasticquiz.Start.qN;
import static com.example.dell_pc.funtasticquiz.Start.simplescore;
import static com.example.dell_pc.funtasticquiz.Start.timeleft;

public class SSound extends AppCompatActivity implements MediaPlayer.OnPreparedListener {
    View view;
    LinearLayout linearLayout;
    private boolean isThr = false;
    ObjectAnimator objectAnimator;
    Intermediary inter = new Intermediary();
    private TextView sndScoreView;
    private TextView sndQuestionView;
    private TextView sndCountq, timertext;
    private Button sndButtonChoice1;
    private Button sndButtonChoice2;
    private Button sndButtonChoice3;
    private Button sndButtonChoice4;
    private Button sndButtonHint;
    private Button play, pause, stop;
    private int sound = 0, crntPos = 0;
    private String mAnswer;
    MediaPlayer mediaPlayer;
    boolean bl = false, track = false, chk = false;
    boolean btani1 = false, btani2 = false, btani3 = false, btani4 = false;
    CountDownTimer countDownTimer;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ssound);
        initializesnd();
        toolbar = findViewById(R.id.ttoolbar);
        setSupportActionBar(toolbar);
        if (level != 1) {
            timertext.setVisibility(View.VISIBLE);
        }
        maintainLayout();
    }

    private void maintainLayout() {
        if (qN < 20) {
            if (aNo[qN] == 2 || aNo[qN] == 6 || aNo[qN] == 12 || aNo[qN] == 18) showLayout(2);
            else if (aNo[qN] == 4 || aNo[qN] == 14) showLayout(3);
            else if (aNo[qN] == 8) showLayout(4);
            else showLayout(1);
        } else {
            chkSound();
            chkforsorryorcongrats();
        }
    }

    private void showLayout(int i) {
        if (i == 1) {
            if (timeleft > 999) timeleft -= 1000;
            Intent intent = new Intent(SSound.this, Simple.class);
            startActivity(intent);
            finish();

        } else if (i == 2) {
            if (timeleft > 999) timeleft -= 1000;
            Intent intent = new Intent(SSound.this, IImage.class);
            startActivity(intent);
            finish();
        } else if (i == 3) {
            if (level != 1) showTime();
            sound = (int) inter.getIVS(level, aNo[qN]);  //sound or resource neoa or (int) dia type cast kora or long to int o karon media player o long suppor korey na
            hideBtn();
            if (objectAnimator != null) objectAnimator.cancel();
            setButton();
            snd_updateScore(0);
            playMusic();
            StartAnimations();
        } else {
            //Log.e("vd ","oy");
            if (timeleft > 999) timeleft -= 1000;
            Intent intent = new Intent(SSound.this, VVideo.class);
            startActivity(intent);
            finish();

        }
    }


    private void updateQuestionsnd() {

        sndQuestionView.setText(inter.getQuestion(level, aNo[qN]));
        sndButtonChoice1.setText(inter.getOp1(level, aNo[qN]));
        sndButtonChoice2.setText(inter.getOp2(level, aNo[qN]));
        sndButtonChoice3.setText(inter.getOp3(level, aNo[qN]));
        sndButtonChoice4.setText(inter.getOp4(level, aNo[qN]));
        mAnswer = inter.getCorrectAns(level, aNo[qN]).toLowerCase();
    }

    private void initializesnd() { //img=image layout
        LayoutInflater layoutInflater = getLayoutInflater();
        view = layoutInflater.inflate(R.layout.toolbar, (ViewGroup) findViewById(R.id.ttoolbar));
        sndScoreView = view.findViewById(R.id.score);
        sndQuestionView = findViewById(R.id.snd_question);
        sndButtonChoice1 = findViewById(R.id.snd_btn_one);
        sndButtonChoice2 = findViewById(R.id.snd_btn_two);
        sndButtonChoice3 = findViewById(R.id.snd_btn_three);
        sndButtonChoice4 = findViewById(R.id.snd_btn_four);
        sndButtonHint = findViewById(R.id.btn_hint);
        sndCountq = view.findViewById(R.id.countq);
        timertext = view.findViewById(R.id.timertext);
        linearLayout = findViewById(R.id.linearlayout);
    }

    private void snd_updateScore(int y) {
        sndScoreView.setText("" + mscore);
        if (y == 0)
            sndCountq.setText((qN + 1) + "/20");
    }

    //Button for choices start
    public void btnone(View view) {
        btani1 = true;
        String s = sndButtonChoice1.getText().toString().toLowerCase().trim();
        chkSound();  //user answer dilaily sound r solar dorkar nay ou off kora oy
        compareAns(s);
    }

    public void btntwo(View view) {
        btani2 = true;
        String s = sndButtonChoice2.getText().toString().toLowerCase().trim();
        chkSound();
        compareAns(s);
    }

    public void btnthree(View view) {
        btani3 = true;
        String s = sndButtonChoice3.getText().toString().toLowerCase().trim();
        chkSound();
        compareAns(s);
    }

    public void btnfour(View view) {
        btani4 = true;
        String s = sndButtonChoice4.getText().toString().toLowerCase().trim();
        chkSound();
        compareAns(s);
    }
    //Buttons end

    //Buttons for mPlayer start
    public void play(View view) {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(getApplicationContext(), sound);
            mediaPlayer.start();
            chk = false;
        } else if (!mediaPlayer.isPlaying()) {
            mediaPlayer.seekTo(crntPos);
            mediaPlayer.start();
        }

    }

    public void pause(View view) {
        if (mediaPlayer != null) {
            mediaPlayer.pause();
            crntPos = mediaPlayer.getCurrentPosition();
        }

    }

    public void stop(View view) {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;

            crntPos = 0;
            if (track == false) {  //jatey button gula ekhbar ou show oy track dia okhta dekha oisay.user a duibar play stop korta faroin erday chk kora
                showBtn();
                StartAnimations();
            }
            track = true;
            chk = true;
        }

    }

    //Buttons end
    public void compareAns(String s) {
        isThr = false;
        if (s.equals(mAnswer)) {
            if (level == 3) hardqcount = 0;
            // showToast(1);
            mscore += 50;
            snd_updateScore(1);
            updateQnumb();
            after_ans_animation(true);
            playMusic(1);
            Thread t = new Thread() {
                public void run() {
                    try {
                        Thread.sleep(2000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (level != 1) countDownTimer.cancel();
                                isThr = true;
                                maintainLayout();
                                if (isThr == true) {
                                    return;
                                }
                            }
                        });
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            };
            t.start();

        } else {
            playMusic(2);

            if (level == 3) ++hardqcount;
            //showToast(2);
            if (hardqcount >= 2 && level == 3) {
                countDownTimer.cancel();
                Toast.makeText(this, "Consiqutively wrong answered!!Be carefull next time", Toast.LENGTH_LONG).show();
                chkforsorryorcongrats();
            } else {
                snd_updateScore(1);
                updateQnumb();
                after_ans_animation(false);

                Thread t = new Thread() {
                    public void run() {

                        try {
                            Thread.sleep(2000);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (level != 1) countDownTimer.cancel();
                                    isThr = true;
                                    maintainLayout();
                                    if (isThr == true) {
                                        return;
                                    }
                                }
                            });
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }


                    }
                };
                t.start();

            }
        }


    }

    //check kora jen sound kita soler ni jodi running thakey tailey cancel kora or
    void chkSound() {
        if (chk == false) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
                mediaPlayer = null;
            }
        }
    }

    //button show kora
    public void showBtn() {
        sndQuestionView.setVisibility(View.VISIBLE);
        sndButtonChoice1.setVisibility(View.VISIBLE);
        sndButtonChoice2.setVisibility(View.VISIBLE);
        sndButtonChoice3.setVisibility(View.VISIBLE);
        sndButtonChoice4.setVisibility(View.VISIBLE);
        sndButtonHint.setVisibility(View.VISIBLE);
        updateQuestionsnd();
    }

    //button hide korar lagi
    public void hideBtn() {
        sndQuestionView.setVisibility(View.GONE);
        sndButtonChoice1.setVisibility(View.GONE);
        sndButtonChoice2.setVisibility(View.GONE);
        sndButtonChoice3.setVisibility(View.GONE);
        sndButtonChoice4.setVisibility(View.GONE);
        sndButtonHint.setVisibility(View.GONE);

    }

    private void updateQnumb() {
        qN++;
    }

    private void StartAnimations() {

       /* LinearLayout l = (LinearLayout) findViewById(R.id.lin_lay);
        l.clearAnimation();
        l.startAnimation(anim);
        */

        Animation animleft = AnimationUtils.loadAnimation(this, R.anim.leftside);
        Animation animright = AnimationUtils.loadAnimation(this, R.anim.rightside);
        Animation animfromtop = AnimationUtils.loadAnimation(this, R.anim.fromtop);

        animleft.reset();
        animright.reset();
        animfromtop.reset();

        sndButtonChoice1.clearAnimation();
        sndButtonChoice1.startAnimation(animfromtop);


        sndButtonChoice2.clearAnimation();
        sndButtonChoice2.startAnimation(animleft);
        sndButtonChoice3.clearAnimation();
        sndButtonChoice3.startAnimation(animright);

        sndButtonChoice4.clearAnimation();
        sndButtonChoice4.startAnimation(animfromtop);

    }

    private void showToast(int i) {
        LayoutInflater layoutInflater = getLayoutInflater();
        View layout;
        if (i == 1) {
            layout = layoutInflater.inflate(R.layout.toastforcorrectans, (ViewGroup) findViewById(R.id.toastcorrect));

        } else
            layout = layoutInflater.inflate(R.layout.toastforworngans, (ViewGroup) findViewById(R.id.toastwrong));

        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.BOTTOM, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }
//hint button

    public void sndHint(View view) {
        if (hintcount >= 3 || mscore < 20) {
            if (hintcount >= 3 && mscore < 20) {
                showhintDialog1(1);
            } else if (hintcount >= 3) {
                showhintDialog1(2);

            } else showhintDialog1(3);

        } else {
            showhintDialog2();
        }
    }


    private void showhintDialog1(int a) {
        LayoutInflater layoutInflater = getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.viewforhint2, (ViewGroup) findViewById(R.id.hintlayout2));
        TextView textView = view.findViewById(R.id.hinttext2);
        String s = "";

        AlertDialog.Builder builder = new AlertDialog.Builder(SSound.this);
        if (a == 1)
            s = "Sorry you can't get hint\nYour score is below 20 and you have already used hint 3 times";
        else if (a == 2) s = "Sorry you can't get hint\nYour already have used hint 3 times";
        else s = "Sorry you can't get hint\nYour score is below 20";
        textView.setText(s);
        builder.setPositiveButton("Oukay", null);
        builder.setView(view);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    private void showhintDialog2() {
        Snackbar.make(linearLayout, "Score is reduced by 20", Snackbar.LENGTH_LONG).setAction("Action", null).show();

        ++hintcount;
        mscore -= 20;
        snd_updateScore(1);
        AlertDialog.Builder builder = new AlertDialog.Builder(SSound.this);
        LayoutInflater layoutInflater = getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.viewforhints, (ViewGroup) findViewById(R.id.layouthint));
        TextView textView = view.findViewById(R.id.hinttext);
        textView.setText("" + inter.getHint(level, aNo[qN]));
        builder.setView(view);
        builder.setPositiveButton("Got it", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showTime() {

        countDownTimer = new CountDownTimer(timeleft, 1000) {


            @Override
            public void onTick(long l) {

                timeleft = l;
                int min = (int) timeleft / 60000;
                int sec = (int) timeleft % 60000 / 1000;
                String tt = "";
                tt += min;
                tt += ":";
                if (sec < 10) tt += "0";
                tt += sec;
                timertext.setText(tt);
            }

            @Override
            public void onFinish() {
                // aBoolean=true;
                Toast.makeText(SSound.this, "Times Upp!!Do hurry next time", Toast.LENGTH_LONG).show();
                countDownTimer.cancel();
                loadScore();
                chkforsorryorcongrats();


            }
        }.start();
    }

    private void loadScore() {
        SharedPreferences sharedPreferences = getSharedPreferences("Score", Context.MODE_PRIVATE);
        simplescore = sharedPreferences.getInt("simple", 0);
        hardscore = sharedPreferences.getInt("hard", 0);
        extremescore = sharedPreferences.getInt("extreme", 0);
    }

    private void after_ans_animation(boolean chk) {//chk determines whether the ans is correct or wrong
        objectAnimator = null;
        if (btani1 == true) {
            if (chk == true) {
                objectAnimator = ObjectAnimator.ofInt(sndButtonChoice1, "backgroundColor", Color.parseColor("#C6FF00"), Color.parseColor("#AEEA00"));
            } else
                objectAnimator = ObjectAnimator.ofInt(sndButtonChoice1, "backgroundColor", Color.parseColor("#f44336"), Color.parseColor("#d50000"));

        } else if (btani1 == false)
            sndButtonChoice1.setBackgroundColor(Color.parseColor("#FF6E40"));
        if (btani2 == true) {
            if (chk == true) {
                objectAnimator = ObjectAnimator.ofInt(sndButtonChoice2, "backgroundColor", Color.parseColor("#C6FF00"), Color.parseColor("#AEEA00"));
            } else
                objectAnimator = ObjectAnimator.ofInt(sndButtonChoice2, "backgroundColor", Color.parseColor("#f44336"), Color.parseColor("#d50000"));

        } else if (btani2 == false)
            sndButtonChoice2.setBackgroundColor(Color.parseColor("#FF6E40"));

        if (btani3 == true) {
            if (chk == true) {
                objectAnimator = ObjectAnimator.ofInt(sndButtonChoice3, "backgroundColor", Color.parseColor("#C6FF00"), Color.parseColor("#AEEA00"));
            } else
                objectAnimator = ObjectAnimator.ofInt(sndButtonChoice3, "backgroundColor", Color.parseColor("#f44336"), Color.parseColor("#d50000"));

        } else if (btani3 == false)
            sndButtonChoice3.setBackgroundColor(Color.parseColor("#FF6E40"));

        if (btani4 == true) {
            if (chk == true) {
                objectAnimator = ObjectAnimator.ofInt(sndButtonChoice4, "backgroundColor", Color.parseColor("#C6FF00"), Color.parseColor("#AEEA00"));
            } else
                objectAnimator = ObjectAnimator.ofInt(sndButtonChoice4, "backgroundColor", Color.parseColor("#f44336"), Color.parseColor("#d50000"));

        } else if (btani4 == false)
            sndButtonChoice4.setBackgroundColor(Color.parseColor("#FF6E40"));
        objectAnimator.setDuration(300);
        objectAnimator.setEvaluator(new ArgbEvaluator());
        objectAnimator.setRepeatMode(ValueAnimator.REVERSE);
        objectAnimator.setRepeatCount(ValueAnimator.INFINITE);
        objectAnimator.start();

        sndButtonChoice1.setClickable(false);
        sndButtonChoice2.setClickable(false);
        sndButtonChoice3.setClickable(false);
        sndButtonChoice4.setClickable(false);
    }

    public void setButton() {//initial button color set kora
        btani1 = false;
        btani2 = false;
        btani3 = false;
        btani4 = false;
        sndButtonChoice1.setBackgroundResource(R.drawable.qbutton);
        sndButtonChoice2.setBackgroundResource(R.drawable.qbutton);

        sndButtonChoice3.setBackgroundResource(R.drawable.qbutton);

        sndButtonChoice4.setBackgroundResource(R.drawable.qbutton);
        sndButtonChoice1.setClickable(true);
        sndButtonChoice2.setClickable(true);
        sndButtonChoice3.setClickable(true);
        sndButtonChoice4.setClickable(true);

    }

    private void playMusic() {
        if (mediaPlayer != null) mediaPlayer.release();
        mediaPlayer = MediaPlayer.create(getApplicationContext(), sound);
        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                crntPos = 0;
                showBtn();
            }
        });

    }

    private void playMusic(int i) {
        if (mediaPlayer != null) mediaPlayer.release();
        if (i == 1)
            mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.correct);
        else mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.wrong);

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

    private void chkforsorryorcongrats() {
        loadScore();
        if (level == 1) {

            if (simplescore >= 750) {//lvl-1 or lagi score ageo 750 oilay direct result page o new or
                Intent intent = new Intent(SSound.this, FinalResult.class);
                startActivity(intent);
                finish();

            } else if (mscore >= 750) {//lvl-1 or lagi score 750 na oilay congrats giff img dekhani or
                Intent intent = new Intent(SSound.this, ResultSplash.class);
                startActivity(intent);
                finish();
            } else {//lvl 1 score ekhono 750 oisay na thats why sorry dekhani or
                Intent intent = new Intent(SSound.this, FinalResultFailSplash.class);
                startActivity(intent);
                finish();
            }
        }
        if (level == 2) {

            if (hardscore >= 800) {//lvl-2 or lagi score ageo 800 oilay direct result page o new or
                Intent intent = new Intent(SSound.this, FinalResult.class);
                startActivity(intent);
                finish();

            } else if (mscore >= 800) {//lvl-2 or lagi score 800 na oilay congrats giff img dekhani or
                Intent intent = new Intent(SSound.this, ResultSplash.class);
                startActivity(intent);
                finish();
            } else {//lvl 2 score ekhono 800 oisay na thats why sorry dekhani or
                Intent intent = new Intent(SSound.this, FinalResultFailSplash.class);
                startActivity(intent);
                finish();
            }
        }
        if (level == 3) {
            if (extremescore >= 900) {//lvl-3 or lagi score ageo 900 oilay direct result page o new or
                Intent intent = new Intent(SSound.this, FinalResult.class);
                startActivity(intent);
                finish();

            } else if (mscore >= 900) {//lvl-3 or lagi score 900 na oilay congrats giff img dekhani or
                Intent intent = new Intent(SSound.this, ResultSplash.class);
                startActivity(intent);
                finish();
            } else {//lvl 3 score ekhono 900 oisay na thats why sorry dekhani or
                Intent intent = new Intent(SSound.this, FinalResultFailSplash.class);
                startActivity(intent);
                finish();
            }
        }


    }
}
