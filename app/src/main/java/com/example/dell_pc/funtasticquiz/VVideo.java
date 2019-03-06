package com.example.dell_pc.funtasticquiz;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.CountDownTimer;
import android.support.design.widget.Snackbar;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

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

public class VVideo extends AppCompatActivity implements MediaPlayer.OnPreparedListener {
    View view;
    LinearLayout linearLayout;
    MediaPlayer mediaPlayer;
    private boolean isThr = false;
    boolean btani1 = false, btani2 = false, btani3 = false, btani4 = false;
    ObjectAnimator objectAnimator;
    Intermediary inter = new Intermediary();
    private String mAnswer;
    private VideoView vvdio;
    private TextView vdScoreView;
    private TextView vdQuestionView, timertext;
    private TextView vdCountq;
    private Button vdButtonChoice1;
    private Button vdButtonChoice2;
    private Button vdButtonChoice3;
    private Button vdButtonChoice4;
    private Button vdButtonHint;
    private long video = 0;
    MediaController mediaController;
    CountDownTimer countDownTimer;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vvideo);
        toolbar = findViewById(R.id.ttoolbar);
        setSupportActionBar(toolbar);
        mediaController = new MediaController(this);
        initializevd();
        if (level != 1) {
            timertext.setVisibility(View.VISIBLE);
        }
        maintainLayout();
        vd_updateScore(0);
    }

    private void maintainLayout() {
        if (qN < 20) {
            if (aNo[qN] == 2 || aNo[qN] == 6 || aNo[qN] == 12 || aNo[qN] == 18) showLayout(2);
            else if (aNo[qN] == 4 || aNo[qN] == 14) showLayout(3);
            else if (aNo[qN] == 8) showLayout(4);
            else showLayout(1);
        } else {
            chkforsorryorcongrats();
        }
    }

    private void showLayout(int i) {
        if (i == 1) {
            if (timeleft > 999) timeleft -= 1000;
            Intent intent = new Intent(VVideo.this, Simple.class);
            startActivity(intent);
            finish();
        } else if (i == 2) {
            if (timeleft > 999) timeleft -= 1000;
            Intent intent = new Intent(VVideo.this, IImage.class);
            startActivity(intent);
            finish();

        } else if (i == 3) {
            if (timeleft > 999) timeleft -= 1000;
            Intent intent = new Intent(VVideo.this, SSound.class);
            startActivity(intent);
            finish();

        } else {
            if (level != 1) showTime();
            if (objectAnimator != null) objectAnimator.cancel();
            setButton();
            StartAnimations();
            playVideo();
        }
    }

    private void playVideo() {
        String path = "android.resource://" + getPackageName() + "/" + video;
        Uri uri = Uri.parse(path);
        vvdio.setVideoURI(uri);
        vvdio.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                showBtn();
            }
        });
        vvdio.setMediaController(mediaController);
        mediaController.setAnchorView(vvdio);
        vvdio.start();
    }

    private void initializevd() {   //vd=video layout
        LayoutInflater layoutInflater = getLayoutInflater();
        view = layoutInflater.inflate(R.layout.toolbar, (ViewGroup) findViewById(R.id.ttoolbar));
        vdScoreView = view.findViewById(R.id.score);
        vdQuestionView = findViewById(R.id.vd_question);

        vdButtonChoice1 = findViewById(R.id.vd_btn_one);
        vdButtonChoice2 = findViewById(R.id.vd_btn_two);
        vdButtonChoice3 = findViewById(R.id.vd_btn_three);
        vdButtonChoice4 = findViewById(R.id.vd_btn_four);
        vdButtonHint = findViewById(R.id.btn_hint);
        vvdio = findViewById(R.id.videoView2);
        timertext = view.findViewById(R.id.timertext);
        linearLayout = findViewById(R.id.linearlayout);

        if (qN < 20)
            video = inter.getIVS(level, aNo[qN]);
        vdCountq = view.findViewById(R.id.countq);

    }


    private void updateQuestionvd() {
        vdQuestionView.setText(inter.getQuestion(level, aNo[qN]));
        vdButtonChoice1.setText(inter.getOp1(level, aNo[qN]));
        vdButtonChoice2.setText(inter.getOp2(level, aNo[qN]));
        vdButtonChoice3.setText(inter.getOp3(level, aNo[qN]));
        vdButtonChoice4.setText(inter.getOp4(level, aNo[qN]));
        mAnswer = inter.getCorrectAns(level, aNo[qN]).toLowerCase();
        chkVideo();
    }

    private void vd_updateScore(int y) {
        vdScoreView.setText("" + mscore);
        if (y == 0)
            vdCountq.setText((qN + 1) + "/20");

    }

    //buttons for choice
    public void btnone(View view) {
        btani1 = true;
        String s = vdButtonChoice1.getText().toString().toLowerCase().trim();
        chkVideo();
        compareAns(s);
    }

    public void btntwo(View view) {
        btani2 = true;
        String s = vdButtonChoice2.getText().toString().toLowerCase().trim();
        chkVideo();
        compareAns(s);
    }

    public void btnthree(View view) {
        btani3 = true;
        String s = vdButtonChoice3.getText().toString().toLowerCase().trim();
        chkVideo();
        compareAns(s);
    }

    public void btnfour(View view) {
        btani4 = true;
        String s = vdButtonChoice4.getText().toString().toLowerCase().trim();
        chkVideo();
        compareAns(s);
    }
    //End buttons

    private void chkVideo() {
        if (vvdio.isPlaying()) {
            vvdio.stopPlayback();
        }
    }

    public void compareAns(String s) {
        isThr = false;
        if (s.equals(mAnswer)) {
            playMusic(1);

            if (level == 3) hardqcount = 0;
            //showToast(1);
            mscore += 50;
            vd_updateScore(1);
            updateQnumb();
            after_ans_animation(true);
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
            if (level == 3) ++hardqcount;
            //showToast(2);
            if (hardqcount >= 2 && level == 3) {
                countDownTimer.cancel();
                Toast.makeText(this, "Consiqutively wrong answered!!Be carefull next time", Toast.LENGTH_LONG).show();
                chkforsorryorcongrats();
            } else {
                playMusic(2);

                vd_updateScore(1);
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

    public void showBtn() {
        vdQuestionView.setVisibility(View.VISIBLE);
        vdButtonChoice1.setVisibility(View.VISIBLE);
        vdButtonChoice2.setVisibility(View.VISIBLE);
        vdButtonChoice3.setVisibility(View.VISIBLE);
        vdButtonChoice4.setVisibility(View.VISIBLE);
        vdButtonHint.setVisibility(View.VISIBLE);

        updateQuestionvd();
    }

    private void updateQnumb() {
        qN++;
    }

    private void StartAnimations() {


       /* LinearLayout l = (LinearLayout) findViewById(R.id.lin_lay);
        l.clearAnimation();
        l.startAnimation(anim);
        */
        Animation slidedown = AnimationUtils.loadAnimation(this, R.anim.slidedown);
        Animation slideup = AnimationUtils.loadAnimation(this, R.anim.slide_up);

        Animation anim = AnimationUtils.loadAnimation(this, R.anim.translate);
        Animation animleft = AnimationUtils.loadAnimation(this, R.anim.leftside);
        Animation animright = AnimationUtils.loadAnimation(this, R.anim.rightside);

        anim.reset();
        animleft.reset();
        animright.reset();
        slidedown.reset();
        slideup.reset();

        vdButtonChoice1.clearAnimation();
        vdButtonChoice1.startAnimation(slideup);


        vdButtonChoice2.clearAnimation();
        vdButtonChoice2.startAnimation(animleft);
        vdButtonChoice3.clearAnimation();
        vdButtonChoice3.startAnimation(animright);

        vdButtonChoice4.clearAnimation();
        vdButtonChoice4.startAnimation(slidedown);
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
    public void vdHint(View view) {
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

        AlertDialog.Builder builder = new AlertDialog.Builder(VVideo.this);
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
        mscore -= 20;
        vd_updateScore(1);
        ++hintcount;
        AlertDialog.Builder builder = new AlertDialog.Builder(VVideo.this);
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
                Toast.makeText(VVideo.this, "Times Upp!!Do hurry next time", Toast.LENGTH_LONG).show();
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
                objectAnimator = ObjectAnimator.ofInt(vdButtonChoice1, "backgroundColor", Color.parseColor("#C6FF00"), Color.parseColor("#AEEA00"));
            } else
                objectAnimator = ObjectAnimator.ofInt(vdButtonChoice1, "backgroundColor", Color.parseColor("#f44336"), Color.parseColor("#d50000"));

        } else if (btani1 == false) vdButtonChoice1.setBackgroundColor(Color.parseColor("#FF6E40"));
        if (btani2 == true) {
            if (chk == true) {
                objectAnimator = ObjectAnimator.ofInt(vdButtonChoice2, "backgroundColor", Color.parseColor("#C6FF00"), Color.parseColor("#AEEA00"));
            } else
                objectAnimator = ObjectAnimator.ofInt(vdButtonChoice2, "backgroundColor", Color.parseColor("#f44336"), Color.parseColor("#d50000"));

        } else if (btani2 == false) vdButtonChoice2.setBackgroundColor(Color.parseColor("#FF6E40"));

        if (btani3 == true) {
            if (chk == true) {
                objectAnimator = ObjectAnimator.ofInt(vdButtonChoice3, "backgroundColor", Color.parseColor("#C6FF00"), Color.parseColor("#AEEA00"));
            } else
                objectAnimator = ObjectAnimator.ofInt(vdButtonChoice3, "backgroundColor", Color.parseColor("#f44336"), Color.parseColor("#d50000"));

        } else if (btani3 == false) vdButtonChoice3.setBackgroundColor(Color.parseColor("#FF6E40"));

        if (btani4 == true) {
            if (chk == true) {
                objectAnimator = ObjectAnimator.ofInt(vdButtonChoice4, "backgroundColor", Color.parseColor("#C6FF00"), Color.parseColor("#AEEA00"));
            } else
                objectAnimator = ObjectAnimator.ofInt(vdButtonChoice4, "backgroundColor", Color.parseColor("#f44336"), Color.parseColor("#d50000"));

        } else if (btani4 == false) vdButtonChoice4.setBackgroundColor(Color.parseColor("#FF6E40"));

        objectAnimator.setDuration(300);
        objectAnimator.setEvaluator(new ArgbEvaluator());
        objectAnimator.setRepeatMode(ValueAnimator.REVERSE);
        objectAnimator.setRepeatCount(ValueAnimator.INFINITE);
        objectAnimator.start();
        vdButtonChoice1.setClickable(false);
        vdButtonChoice2.setClickable(false);
        vdButtonChoice3.setClickable(false);
        vdButtonChoice4.setClickable(false);
    }

    public void setButton() {//initial button color set kora
        btani1 = false;
        btani2 = false;
        btani3 = false;
        btani4 = false;
        vdButtonChoice1.setBackgroundResource(R.drawable.qbutton);
        vdButtonChoice2.setBackgroundResource(R.drawable.qbutton);

        vdButtonChoice3.setBackgroundResource(R.drawable.qbutton);

        vdButtonChoice4.setBackgroundResource(R.drawable.qbutton);

        vdButtonChoice1.setClickable(true);
        vdButtonChoice2.setClickable(true);
        vdButtonChoice3.setClickable(true);
        vdButtonChoice4.setClickable(true);

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
        if (countDownTimer != null) countDownTimer.cancel();
        loadScore();
        if (level == 1) {

            if (simplescore >= 750) {//lvl-1 or lagi score ageo 750 oilay direct result page o new or
                Intent intent = new Intent(VVideo.this, FinalResult.class);
                startActivity(intent);
                finish();

            } else if (mscore >= 750) {//lvl-1 or lagi score 750 na oilay congrats giff img dekhani or
                Intent intent = new Intent(VVideo.this, ResultSplash.class);
                startActivity(intent);
                finish();
            } else {//lvl 1 score ekhono 750 oisay na thats why sorry dekhani or
                Intent intent = new Intent(VVideo.this, FinalResultFailSplash.class);
                startActivity(intent);
                finish();
            }
        }
        if (level == 2) {

            if (hardscore >= 800) {//lvl-2 or lagi score ageo 800 oilay direct result page o new or
                Intent intent = new Intent(VVideo.this, FinalResult.class);
                startActivity(intent);
                finish();

            } else if (mscore >= 800) {//lvl-2 or lagi score 800 na oilay congrats giff img dekhani or
                Intent intent = new Intent(VVideo.this, ResultSplash.class);
                startActivity(intent);
                finish();
            } else {//lvl 2 score ekhono 800 oisay na thats why sorry dekhani or
                Intent intent = new Intent(VVideo.this, FinalResultFailSplash.class);
                startActivity(intent);
                finish();
            }
        }
        if (level == 3) {
            if (extremescore >= 900) {//lvl-3 or lagi score ageo 900 oilay direct result page o new or
                Intent intent = new Intent(VVideo.this, FinalResult.class);
                startActivity(intent);
                finish();

            } else if (mscore >= 900) {//lvl-3 or lagi score 900 na oilay congrats giff img dekhani or
                Intent intent = new Intent(VVideo.this, ResultSplash.class);
                startActivity(intent);
                finish();
            } else {//lvl 3 score ekhono 900 oisay na thats why sorry dekhani or
                Intent intent = new Intent(VVideo.this, FinalResultFailSplash.class);
                startActivity(intent);
                finish();
            }
        }


    }
}
