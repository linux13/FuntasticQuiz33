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
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;


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

public class IImage extends AppCompatActivity implements MediaPlayer.OnPreparedListener {
    View view;
    LinearLayout linearLayout;
    private boolean isThr = false;
    MediaPlayer mediaPlayer;
    ObjectAnimator objectAnimator;
    boolean btani1 = false, btani2 = false, btani3 = false, btani4 = false;
    Intermediary inter = new Intermediary();
    private ImageView iimage;
    private TextView imgScoreView;
    private TextView imgQuestionView, timertext;
    private TextView imgCountq;
    private Button imgButtonChoice1;
    private Button imgButtonChoice2;
    private Button imgButtonChoice3;
    private Button imgButtonChoice4;
    private Button imgButtonHint;
    private String mAnswer;
    private int id = 0;
    CountDownTimer countDownTimer;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iimage);
        toolbar = findViewById(R.id.ttoolbar);
        setSupportActionBar(toolbar);
        initializeimg();
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

            chkforsorryorcongrats();
        }
    }

    private void chkforsorryorcongrats() {
        loadScore();
        if (level == 1) {

            if (simplescore >= 750) {//lvl-1 or lagi score ageo 750 oilay direct result page o new or
                Intent intent = new Intent(IImage.this, FinalResult.class);
                startActivity(intent);
                finish();

            } else if (mscore >= 750) {//lvl-1 or lagi score 750 na oilay congrats giff img dekhani or
                Intent intent = new Intent(IImage.this, ResultSplash.class);
                startActivity(intent);
                finish();
            } else {//lvl 1 score ekhono 750 oisay na thats why sorry dekhani or
                Intent intent = new Intent(IImage.this, FinalResultFailSplash.class);
                startActivity(intent);
                finish();
            }
        }
        if (level == 2) {

            if (hardscore >= 800) {//lvl-2 or lagi score ageo 800 oilay direct result page o new or
                Intent intent = new Intent(IImage.this, FinalResult.class);
                startActivity(intent);
                finish();

            } else if (mscore >= 800) {//lvl-2 or lagi score 800 na oilay congrats giff img dekhani or
                Intent intent = new Intent(IImage.this, ResultSplash.class);
                startActivity(intent);
                finish();
            } else {//lvl 2 score ekhono 800 oisay na thats why sorry dekhani or
                Intent intent = new Intent(IImage.this, FinalResultFailSplash.class);
                startActivity(intent);
                finish();
            }
        }
        if (level == 3) {
            if (extremescore >= 900) {//lvl-3 or lagi score ageo 900 oilay direct result page o new or
                Intent intent = new Intent(IImage.this, FinalResult.class);
                startActivity(intent);
                finish();

            } else if (mscore >= 900) {//lvl-3 or lagi score 900 na oilay congrats giff img dekhani or
                Intent intent = new Intent(IImage.this, ResultSplash.class);
                startActivity(intent);
                finish();
            } else {//lvl 3 score ekhono 900 oisay na thats why sorry dekhani or
                Intent intent = new Intent(IImage.this, FinalResultFailSplash.class);
                startActivity(intent);
                finish();
            }
        }


    }

    private void showLayout(int i) {
        if (i == 1) {
            if (timeleft > 999) timeleft -= 1000;
            Intent intent = new Intent(IImage.this, Simple.class);
            startActivity(intent);
            finish();

        } else if (i == 2) {
            if (level != 1) showTime();
            id = (int) inter.getIVS(level, aNo[qN]);
            if (objectAnimator != null) objectAnimator.cancel();
            setButton();
            img_updateScore(0);
            updateQuestionimg();
            StartAnimations();
        } else if (i == 3) {
            if (timeleft > 999) timeleft -= 1000;
            Intent intent = new Intent(IImage.this, SSound.class);
            startActivity(intent);
            finish();

        } else {
            if (timeleft > 999) timeleft -= 1000;
            Intent intent = new Intent(IImage.this, VVideo.class);
            startActivity(intent);
            finish();
        }
    }

    private void initializeimg() { //img=image layout
        LayoutInflater layoutInflater = getLayoutInflater();
        view = layoutInflater.inflate(R.layout.toolbar, (ViewGroup) findViewById(R.id.ttoolbar));
        imgScoreView = view.findViewById(R.id.score);
        imgQuestionView = findViewById(R.id.img_question);
        imgButtonChoice1 = findViewById(R.id.img_btn_one);
        imgButtonChoice2 = findViewById(R.id.img_btn_two);
        imgButtonChoice3 = findViewById(R.id.img_btn_three);
        imgButtonChoice4 = findViewById(R.id.img_btn_four);
        imgButtonHint = findViewById(R.id.btn_hint);
        iimage = findViewById(R.id.imageView2);
        imgCountq = view.findViewById(R.id.countq);
        timertext = view.findViewById(R.id.timertext);
        linearLayout = findViewById(R.id.linearlayout);

    }

    private void updateQuestionimg() {

        imgQuestionView.setText(inter.getQuestion(level, aNo[qN]));
        imgButtonChoice1.setText(inter.getOp1(level, aNo[qN]));
        imgButtonChoice2.setText(inter.getOp2(level, aNo[qN]));
        imgButtonChoice3.setText(inter.getOp3(level, aNo[qN]));
        imgButtonChoice4.setText(inter.getOp4(level, aNo[qN]));

        iimage.setImageResource(id);
        mAnswer = inter.getCorrectAns(level, aNo[qN]).toLowerCase();
    }

    private void img_updateScore(int y) {
        imgScoreView.setText("" + mscore);
        if (y == 0)
            imgCountq.setText((qN + 1) + "/20");

    }

    public void btnone(View view) {
        btani1 = true;
        String s = imgButtonChoice1.getText().toString().toLowerCase().trim();
        compareAns(s);
    }

    public void btntwo(View view) {
        btani2 = true;
        String s = imgButtonChoice2.getText().toString().toLowerCase().trim();
        compareAns(s);
    }

    public void btnthree(View view) {
        btani3 = true;
        String s = imgButtonChoice3.getText().toString().toLowerCase().trim();
        compareAns(s);
    }

    public void btnfour(View view) {
        btani4 = true;
        String s = imgButtonChoice4.getText().toString().toLowerCase().trim();
        compareAns(s);
    }

    public void compareAns(String s) {
        isThr = false;
        if (s.equals(mAnswer)) {
            playMusic(1);

            if (level == 3) hardqcount = 0;
            // showToast(1);
            mscore += 50;
            img_updateScore(1);
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
            playMusic(2);

            if (level == 3) ++hardqcount;
            //showToast(2);
            if (hardqcount >= 2 && level == 3) {
                countDownTimer.cancel();
                Toast.makeText(this, "Consiqutively wrong answered!!Be carefull next time", Toast.LENGTH_LONG).show();
                chkforsorryorcongrats();
            } else {
                img_updateScore(1);
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

    private void updateQnumb() {
        qN++;
    }

    private void StartAnimations() {

        Animation slideup = AnimationUtils.loadAnimation(this, R.anim.slide_up);
        Animation slidedown = AnimationUtils.loadAnimation(this, R.anim.slidedown);
        slidedown.reset();
        slideup.reset();

        imgButtonChoice1.clearAnimation();
        imgButtonChoice1.startAnimation(slideup);


        imgButtonChoice2.clearAnimation();
        imgButtonChoice2.startAnimation(slideup);
        imgButtonChoice3.clearAnimation();
        imgButtonChoice3.startAnimation(slidedown);

        imgButtonChoice4.clearAnimation();
        imgButtonChoice4.startAnimation(slidedown);

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

    public void imgHint(View view) {
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

        AlertDialog.Builder builder = new AlertDialog.Builder(IImage.this);
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
        img_updateScore(1);
        ++hintcount;
        AlertDialog.Builder builder = new AlertDialog.Builder(IImage.this);
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
                Toast.makeText(IImage.this, "Times Upp!!Do hurry next time", Toast.LENGTH_LONG).show();
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
                objectAnimator = ObjectAnimator.ofInt(imgButtonChoice1, "backgroundColor", Color.parseColor("#C6FF00"), Color.parseColor("#AEEA00"));
            } else
                objectAnimator = ObjectAnimator.ofInt(imgButtonChoice1, "backgroundColor", Color.parseColor("#f44336"), Color.parseColor("#d50000"));

        } else if (btani1 == false)
            imgButtonChoice1.setBackgroundColor(Color.parseColor("#FF6E40"));
        if (btani2 == true) {
            if (chk == true) {
                objectAnimator = ObjectAnimator.ofInt(imgButtonChoice2, "backgroundColor", Color.parseColor("#C6FF00"), Color.parseColor("#AEEA00"));
            } else
                objectAnimator = ObjectAnimator.ofInt(imgButtonChoice2, "backgroundColor", Color.parseColor("#f44336"), Color.parseColor("#d50000"));

        } else if (btani2 == false)
            imgButtonChoice2.setBackgroundColor(Color.parseColor("#FF6E40"));

        if (btani3 == true) {
            if (chk == true) {
                objectAnimator = ObjectAnimator.ofInt(imgButtonChoice3, "backgroundColor", Color.parseColor("#C6FF00"), Color.parseColor("#AEEA00"));
            } else
                objectAnimator = ObjectAnimator.ofInt(imgButtonChoice3, "backgroundColor", Color.parseColor("#f44336"), Color.parseColor("#d50000"));

        } else if (btani3 == false)
            imgButtonChoice3.setBackgroundColor(Color.parseColor("#FF6E40"));

        if (btani4 == true) {
            if (chk == true) {
                objectAnimator = ObjectAnimator.ofInt(imgButtonChoice4, "backgroundColor", Color.parseColor("#C6FF00"), Color.parseColor("#AEEA00"));
            } else
                objectAnimator = ObjectAnimator.ofInt(imgButtonChoice4, "backgroundColor", Color.parseColor("#f44336"), Color.parseColor("#d50000"));

        } else if (btani4 == false)
            imgButtonChoice4.setBackgroundColor(Color.parseColor("#FF6E40"));
        objectAnimator.setDuration(300);
        objectAnimator.setEvaluator(new ArgbEvaluator());
        objectAnimator.setRepeatMode(ValueAnimator.REVERSE);
        objectAnimator.setRepeatCount(ValueAnimator.INFINITE);
        objectAnimator.start();
        imgButtonChoice1.setClickable(false);
        imgButtonChoice2.setClickable(false);
        imgButtonChoice3.setClickable(false);
        imgButtonChoice4.setClickable(false);
    }

    public void setButton() {//initial button color set kora
        btani1 = false;
        btani2 = false;
        btani3 = false;
        btani4 = false;
        imgButtonChoice1.setBackgroundResource(R.drawable.qbutton);
        imgButtonChoice2.setBackgroundResource(R.drawable.qbutton);

        imgButtonChoice3.setBackgroundResource(R.drawable.qbutton);

        imgButtonChoice4.setBackgroundResource(R.drawable.qbutton);
        imgButtonChoice1.setClickable(true);
        imgButtonChoice2.setClickable(true);
        imgButtonChoice3.setClickable(true);
        imgButtonChoice4.setClickable(true);

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
}
