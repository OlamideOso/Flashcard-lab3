package com.codepath.flashcard;

import android.animation.Animator;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    FlashcardDatabase flashcardDatabase;
    List<Flashcard> allFlashcards;
    int currentCardDisplayedIndex = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        flashcardDatabase = new FlashcardDatabase(getApplicationContext());
        allFlashcards = flashcardDatabase.getAllCards();
        Log.d("ActivityMain", String.valueOf(allFlashcards.size()));
        if (allFlashcards != null && allFlashcards.size() > 0) {
            ((TextView) findViewById(R.id.flashcard_question)).setText(allFlashcards.get(0).getQuestion());
            ((TextView) findViewById(R.id.textView2)).setText(allFlashcards.get(0).getAnswer());
        }

        findViewById(R.id.flashcard_question).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findViewById(R.id.textView2).setVisibility(View.VISIBLE);
                findViewById(R.id.flashcard_question).setVisibility(View.INVISIBLE);
                View answerSideView = findViewById(R.id.textView2);

                // get the center for the clipping circle
                int cx = answerSideView.getWidth() / 2;
                int cy = answerSideView.getHeight() / 2;

                // get the final radius for the clipping circle
                float finalRadius = (float) Math.hypot(cx, cy);

                // create the animator for this view (the start radius is zero)
                Animator anim = ViewAnimationUtils.createCircularReveal(answerSideView, cx, cy, 0f, finalRadius);

                // hide the question and show the answer to prepare for playing the animation!
                findViewById(R.id.flashcard_question).setVisibility(View.INVISIBLE);
                answerSideView.setVisibility(View.VISIBLE);

                anim.setDuration(3000);
                anim.start();
            }

        });

        findViewById(R.id.addbutton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddCardActivity.class);
                MainActivity.this.startActivityForResult(intent,100);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
            }
        });
        findViewById(R.id.nextbutton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // advance our pointer index so we can show the next card
                currentCardDisplayedIndex++;
                // make sure we don't get an IndexOutOfBoundsError if we are viewing the last indexed card in our list
                if (currentCardDisplayedIndex > allFlashcards.size() - 1) {
                    currentCardDisplayedIndex = 0;
                }
                final Animation leftOutAnim = AnimationUtils.loadAnimation(v.getContext(), R.anim.left_out);
                final Animation rightInAnim = AnimationUtils.loadAnimation(v.getContext(), R.anim.right_in);
                leftOutAnim.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        // this method is called when the animation first starts
                    }
                    @Override
                    public void onAnimationEnd(Animation animation) {
                        // this method is called when the animation is finished playing
                        ((TextView) findViewById(R.id.flashcard_question)).setText(allFlashcards.get(currentCardDisplayedIndex).getQuestion());
                        ((TextView) findViewById(R.id.textView2)).setText(allFlashcards.get(currentCardDisplayedIndex).getAnswer());
                        findViewById(R.id.flashcard_question).setVisibility(View.VISIBLE);
                        findViewById(R.id.flashcard_question).startAnimation(rightInAnim);
                    }
                    @Override
                    public void onAnimationRepeat(Animation animation) {
                        // we don't need to worry about this method
                    }
                });
                findViewById(R.id.flashcard_question).startAnimation(leftOutAnim);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 100) { // this 100 needs to match the 100 we used when we called startActivityForResult!
            String string1 = data.getExtras().getString("string1"); // 'string1' needs to match the key we used when we put the string in the Intent
            String string2 = data.getExtras().getString("string2");
            TextView flashcardQuestion =  findViewById(R.id.flashcard_question);
            flashcardQuestion.setText(string1);
            TextView flashcardAnswer =  findViewById(R.id.textView2);
            flashcardAnswer.setText(string2);

            flashcardDatabase.insertCard(new Flashcard(string1, string2));
            allFlashcards = flashcardDatabase.getAllCards();
        }
    }
}
