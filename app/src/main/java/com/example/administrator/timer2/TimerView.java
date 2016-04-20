package com.example.administrator.timer2;

import android.app.AlertDialog;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.content.Context;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.Timer;
import java.util.TimerTask;

public class TimerView extends AppCompatActivity {

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);
        btnPause = (Button) findViewById(R.id.btnPause);
        btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopTimer();
                btnPause.setVisibility(View.GONE);
                btnResume.setVisibility(View.VISIBLE);//when clicking Pause,the button pause will disappear and resume button will appear.
            }
        });
        btnReset = (Button) findViewById(R.id.btnReset);
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopTimer();//it is a method to stop timer constructed later
                etHour.setText("0");
                etMin.setText("0");
                etSec.setText("0");//when you click reset button, all the text will become 0.

                btnReset.setVisibility(View.GONE);
                btnResume.setVisibility(View.GONE);
                btnPause.setVisibility(View.GONE);
                btnStart.setVisibility(View.VISIBLE);//and reset, resume, pause button will disappear, the Start button will appear.you can record again.
            }
        });
        btnResume = (Button) findViewById(R.id.btnResume);
        btnResume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTimer();
                btnResume.setVisibility(View.GONE);
                btnPause.setVisibility(View.VISIBLE);//when clicking Resume button, the resume button will
                // disappear and the pause button will be visible.The function is that you can continue to record time.
            }
        });
        btnStart = (Button) findViewById(R.id.btnStart);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                startTimer();// it is the method to start timer constructed later.
                btnStart.setVisibility(View.GONE);
                btnPause.setVisibility(View.VISIBLE);
                btnReset.setVisibility(View.VISIBLE);//when you click start button,pause button and reset button will be visible
                //and start button will disappear.
            }
        });
        etHour = (EditText) findViewById(R.id.etHour);
        etMin = (EditText) findViewById(R.id.etMin);
        etSec = (EditText) findViewById(R.id.etSec);
        etHour.setText("00");// at the beginning all the editText will be 00
        etHour.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s)) {//elimate the error when the text is empty
                    int value = Integer.parseInt(s.toString());
                    if (Integer.parseInt(s.toString()) > 59) {
                        etHour.setText("59");
                    } else if (value < 0) {
                        etHour.setText("0");
                    }
                }// limit hour , minute and seconds less than 59 and more than 0
                checkToEnableBtnStart();//it is a method to enable start button constructed later
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        etMin.setText("00");
        etMin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s)) {
                    int value = Integer.parseInt(s.toString());

                    if (Integer.parseInt(s.toString()) > 59) {
                        etMin.setText("59");
                    } else if (value < 0) {
                        etMin.setText("0");
                    }
                }
                checkToEnableBtnStart();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        etSec.setText("00");
        etSec.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s)) {
                    int value = Integer.parseInt(s.toString());

                    if (Integer.parseInt(s.toString()) > 59) {
                        etSec.setText("59");
                    } else if (value < 0) {
                        etSec.setText("0");
                    }
                }
                    checkToEnableBtnStart();
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        btnStart.setVisibility(View.VISIBLE);
        btnStart.setEnabled(false);
        btnPause.setVisibility(View.GONE);
        btnResume.setVisibility(View.GONE);
        btnReset.setVisibility(View.GONE);//at the beginning, only start button appears and the other button disappear.

    }

    private void checkToEnableBtnStart() {
        btnStart.setEnabled((!TextUtils.isEmpty(etHour.getText()) && Integer.parseInt(etHour.getText().toString()) > 0) ||
                (!TextUtils.isEmpty(etMin.getText()) && Integer.parseInt(etMin.getText().toString()) > 0) ||
                (!TextUtils.isEmpty(etSec.getText()) && Integer.parseInt(etSec.getText().toString()) > 0));
    }//it is a method constructed the environment where start button is enabled

    private void startTimer() {
        if (timerTask == null) {
            alltimerCount = Integer.parseInt(etHour.getText().toString()) * 60 * 60 + Integer.parseInt(etMin.getText().toString()) * 60 + Integer.parseInt(etSec.getText().toString());
            //timerTask is a method import by java, alltimerCount is to transfer hout and minute to second and compute the sum.
            timerTask = new TimerTask() {
                @Override
                public void run() {
                    alltimerCount--;
                    handler.sendEmptyMessage(MSG_WHAT_TIME_TICK);//update the hour minute and second, the method can be seen later.
                    if (alltimerCount <= 0) {
                        handler.sendEmptyMessage(MSG_WHAT_TIME_IS_UP);//when alltimerCount = 0, the timer appear "timer is up"
                        stopTimer();//when alltimerCount =0, timer will be stoped
                    }
                }
            };
            timer.schedule(timerTask, 1000, 1000);


        }
    }

    private void stopTimer() {
        if (timerTask != null) {
            timerTask.cancel();
            timerTask = null;
//timerTask will be stoped
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_WHAT_TIME_TICK:
                    int hour = alltimerCount / 60 / 60;
                    int min = (alltimerCount / 60) % 60;
                    int sec = alltimerCount % 60;

                    etHour.setText(hour + "");
                    etMin.setText(min + "");
                    etSec.setText(sec + "");//when alltimerCount--, update the hour minute and second

                    break;
                case MSG_WHAT_TIME_IS_UP:
                    new AlertDialog.Builder(TimerView.this).setTitle("Time is up").setMessage("Time is up").setNegativeButton("Cancel", null).show();
                    btnReset.setVisibility(View.GONE);
                    btnResume.setVisibility(View.GONE);
                    btnPause.setVisibility(View.GONE);
                    btnStart.setVisibility(View.VISIBLE);
                    break;//when time is up start button appear and other buttons disappear.
                default:
                    break;
            }
        }
    };
    private static final int MSG_WHAT_TIME_IS_UP = 1;
    private static final int MSG_WHAT_TIME_TICK = 2;
    private int alltimerCount = 0;
    private Timer timer = new Timer();
    private TimerTask timerTask = null;
    private Button btnStart, btnPause, btnReset, btnResume;
    private EditText etHour, etMin, etSec;


    @Override
    public void onStart() {
        super.onStart();


    }

    @Override
    public void onStop() {
        super.onStop();


    }
}
