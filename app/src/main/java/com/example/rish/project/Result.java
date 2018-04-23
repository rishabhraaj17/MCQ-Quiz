package com.example.rish.project;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Result extends AppCompatActivity {

    TextView correct, incorrect, marks;

    Button ok;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        correct = (TextView) findViewById(R.id.CorrectTVResult);
        incorrect = (TextView) findViewById(R.id.IncorrectTVResult);
        marks = (TextView) findViewById(R.id.CorrectTVResult);

        ok = (Button) findViewById(R.id.Resultbtn);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),Home.class);
                startActivity(i);
            }
        });

    }
}
