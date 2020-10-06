package com.example.magic8ball;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    MediaPlayer player;
    private static final String TAG = "MainActivity";
    private final String TEXT_CONTENTS = "TextContents";
    private Button playerButton;
    private ImageButton magicBtnAnswer;
    private ImageView triangleAnswer;
    private TextView answerOutput;
    private SensorManager sensorManager;
    private Sensor accelerometerSensor;
    private Switch audioSwitchBtn;
    private boolean isAccelerometerSensorAvailable, itIsNotFirstTime = false;
    private float currentX, currentY, currentZ, lastX, lastY, lastZ;
    private float xDifference, yDifference, zDifference;
    private float shakeThreshold = 5f;
    private Vibrator vibrator;
    private int audioIndexTest = 0;
    private Boolean switchState = false;
    //private boolean trueFalseValue = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Get notified when the user has clicked on the button.
        playerButton = findViewById(R.id.playBtn);
        magicBtnAnswer = findViewById(R.id.btnAnswer);
        triangleAnswer = findViewById(R.id.imageViewAnswer);
        answerOutput = findViewById(R.id.tvAnswer);
        audioSwitchBtn = findViewById(R.id.audioSwitch);
        //switchState = audioSwitchBtn.isChecked();

        audioSwitchBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    switchState = true;
                } else {
                    switchState = false;
                }
            }
        });

        View.OnClickListener ourOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Log.i("magic", "clicked on Magic 8 ball");
                if (switchState == true) {
                    eightBallEffect();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            play(v);
                        }
                    }, 3000);
                } else {
                    eightBallEffect();
                }
            }
        };
        magicBtnAnswer.setOnClickListener(ourOnClickListener);

        final View.OnClickListener playerButtonListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                play(v);
            }
        };

        playerButton.setOnClickListener(playerButtonListener);

        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        if (sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null) {
            accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            isAccelerometerSensorAvailable = true;
        } else {
            isAccelerometerSensorAvailable = false;
        }
    }

    public void play(View v) {
        final int[] resID = {R.raw.it_is_certian, R.raw.it_is_decidedly_so, R.raw.without_a_doubt, R.raw.yes_definitely,
                R.raw.you_may_rely_on_it, R.raw.as_i_see_it_yes, R.raw.most_likely, R.raw.out_look_good, R.raw.yes, R.raw.signs_point_to_yes,
                R.raw.reply_hazy_ask_again, R.raw.ask_again_later, R.raw.better_not_tell_you_now, R.raw.cannot_predict_now, R.raw.concentrate_and_ask_again,
                R.raw.dont_count_on_it, R.raw.my_reply_is_no, R.raw.my_sources_say_no, R.raw.outlook_not_so_good, R.raw.very_doubtful};
        if (player == null) {
            player = MediaPlayer.create(this, resID[audioIndexTest]);
            player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    stopPlayer();
                }
            });
        }
        player.start();
    }

    public void pause(View v) {
        if (player != null) {
            player.pause();
        }
    }

    public void stop(View v) {
        stopPlayer();
    }

    private void stopPlayer() {
        if (player != null) {
            player.release();
            player = null;
            Toast.makeText(this, "MediaPlayer released", Toast.LENGTH_SHORT);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopPlayer();
    }

    public void eightBallEffect() {
        makeMeShake(magicBtnAnswer, 20, 5);
        Animation aniRotateClk = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_clockwise);
        Animation fadAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fadin_animation);
        triangleAnswer.startAnimation(aniRotateClk);
        answerOutput.startAnimation(fadAnim);
        answerOutput.setText(randomAnswer());
    }

    public static View makeMeShake(View view, int duration, int offset) {
        Animation anim = new TranslateAnimation(-offset, offset, 0, 0);
        anim.setDuration(duration);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(5);
        view.startAnimation(anim);
        return view;
    }

    private String randomAnswer() {
        String[] answers = new String[]{"It is certain", "It is decidedly so", "Without a doubt", "Yes - definitely",
                "You may rely on it", "As I see it, yes", "Most likely", "Outlook good", "Yes", "Signs point to yes",
                "Reply hazy, ask again", "Ask again later", "Better not tell you now", "Cannot predict now", "Concentrate and ask again",
                "Don't count on it", "My reply is no", "My sources say no", "Outlook not so good", "Very doubtful"};
        Random randomAnswer = new Random();
        audioIndexTest = getAnswerIndex(randomAnswer.nextInt(answers.length));
        return answers[audioIndexTest];
    }

    public int getAnswerIndex(int index) {
        int currentIndex = index;
        return currentIndex;
    }

    /*
    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        String savedString = savedInstanceState.getString(TEXT_CONTENTS);
        answerOutput.setText(savedString);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString(TEXT_CONTENTS, answerOutput.getText().toString());
        super.onSaveInstanceState(outState);
    }
    */
    @Override
    public void onSensorChanged(SensorEvent event) {
        currentX = event.values[0];
        currentY = event.values[1];
        currentZ = event.values[2];
        if (itIsNotFirstTime) {
            xDifference = Math.abs(lastX - currentX);
            yDifference = Math.abs(lastY - currentY);
            zDifference = Math.abs(lastZ - currentZ);
            if ((xDifference > shakeThreshold && yDifference > shakeThreshold) ||
                    (xDifference > shakeThreshold && zDifference > shakeThreshold) ||
                    (yDifference > shakeThreshold && zDifference > shakeThreshold)) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vibrator.vibrate(VibrationEffect.createOneShot(600, VibrationEffect.DEFAULT_AMPLITUDE));
                    eightBallEffect();
                } else {
                    vibrator.vibrate(500);
                    //deprecated in API.26
                }
            }
        }
        lastX = currentX;
        lastY = currentY;
        lastZ = currentZ;
        itIsNotFirstTime = true;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isAccelerometerSensorAvailable == true) {
            sensorManager.registerListener(this, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isAccelerometerSensorAvailable == false) {
            sensorManager.unregisterListener(this);
        }
    }
}
