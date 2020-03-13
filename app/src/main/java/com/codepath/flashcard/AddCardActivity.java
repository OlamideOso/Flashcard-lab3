package com.codepath.flashcard;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddCardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_card);
      findViewById(R.id.cancel_button).setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              finish();
          }
      });

      findViewById(R.id.save_button).setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              String question = ((EditText) findViewById(R.id.editQuestion)).getText().toString();
              String answer = ((EditText) findViewById(R.id.editAnswer)).getText().toString();
              Intent data = new Intent();
              data.putExtra("string1", question);
              data.putExtra("string2", answer);
              setResult(RESULT_OK, data);
              finish();

          }
      });
    }

}
