package com.example.magic8ball;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private final String TEXT_CONTENTS = "TextContents";
    private ImageButton magicBtnAnswer;
    private ImageView triangleAnswer;
    private EditText questionAsked;
    private TextView answerOutput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Get notified when the user has clicked on the button.
        magicBtnAnswer = (ImageButton) findViewById(R.id.btnAnswer);
        triangleAnswer = (ImageView) findViewById(R.id.imageViewAnswer);
        questionAsked = (EditText) findViewById(R.id.editText);
        answerOutput = (TextView) findViewById(R.id.tvAnswer);

        View.OnClickListener ourOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("magic", "clicked on Magic 8 ball");
                questionAsked.setText("");
                makeMeShake(magicBtnAnswer, 20, 5);
                Animation aniRotateClk = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_clockwise);
                Animation fadAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fadin_animation);
                triangleAnswer.startAnimation(aniRotateClk);
                answerOutput.startAnimation(fadAnim);
                answerOutput.setText(randomAnswer());
            }
        };
        magicBtnAnswer.setOnClickListener(ourOnClickListener);

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
        String answers[] = new String[]{"It is certain", "It is decidedly so", "Without a doubt", "Yes - definitely",
                "You may rely on it", "As I see it, yes", "Most likely", "Outlook good", "Yes", "Signs point to yes",
                "Reply hazy, try again", "Ask again later", "Better not tell you now", "Cannot predict now", "Concentrate and ask again",
                "Don't count on it", "My reply is no", "My sources say no", "Outlook not so good", "Very doubtful"};
        Random randomAnswer = new Random();
        return answers[randomAnswer.nextInt(answers.length)];
    }

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
}