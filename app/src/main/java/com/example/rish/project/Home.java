package com.example.rish.project;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

public class Home extends AppCompatActivity {

    private TextView txtName;
    private Button btnLogout;
    private Button q1;
    private Button q2;
    private Button q3;
    private Button q4;
    private Button q5;
    private Button q6;

    private SQLiteHandler db;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        txtName = (TextView) findViewById(R.id.displayName);
        btnLogout = (Button) findViewById(R.id.btnLogout);
        q1=(Button)findViewById(R.id.quizPage1);
        q2=(Button)findViewById(R.id.quizPage2);
        q3=(Button)findViewById(R.id.quizPage3);
        q4=(Button)findViewById(R.id.quizPage4);
        q5=(Button)findViewById(R.id.quizPage5);
        q6=(Button)findViewById(R.id.quizPage6);

        // SqLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // session manager
        session = new SessionManager(getApplicationContext());

        if (!session.isLoggedIn()) {
            logoutUser();
        }

        // Fetching user details from sqlite
        HashMap<String, String> user = db.getUserDetails();

        String name = user.get("name");
        String email = user.get("email");

        // Displaying the user details on the screen
        txtName.setText(name);

        // Logout button click event
        btnLogout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                logoutUser();
            }
        });

        q1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),QuizPage.class);
                startActivity(i);
            }
        });

        q2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),ShowAllQuestions.class);
                startActivity(i);
            }
        });

        q3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"To be Uploaded Soon!",Toast.LENGTH_SHORT).show();
            }
        });

        q4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"To be Uploaded Soon!",Toast.LENGTH_SHORT).show();
            }
        });

        q5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"To be Uploaded Soon!",Toast.LENGTH_SHORT).show();
            }
        });

        q6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"To be Uploaded Soon!",Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Logging out the user. Will set isLoggedIn flag to false in shared
     * preferences Clears the user data from sqlite users table
     * */
    private void logoutUser() {
        session.setLogin(false);

        db.deleteUsers();

        // Launching the login activity
        Intent intent = new Intent(Home.this, LoginTab.class);
        startActivity(intent);
        finish();
    }
}
