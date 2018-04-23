package com.example.rish.project;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.IdRes;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class QuizPage extends AppCompatActivity {

    // json object response url
    private String urlJsonObj = "http://192.168.43.180/android_login_api/readAllQ.php";
    private String responseURL = "http://192.168.43.180/android_login_api/interResponse.php";

    private static String TAG = QuizPage.class.getSimpleName();

    private Button previous, submit, next, end;

    private SessionManager session;
    SQLiteHandler db;

    public int ques_count=0;
    public int ques_now=0;
    public int uInputCount=0;
    public CharSequence ans_given="";

    // Progress dialog
    private ProgressDialog pDialog;

    private TextView Qarea, timerArea;

    private RadioGroup OptionGroup;
    private RadioButton OptionA, OptionB, OptionC, OptionD;

    CountDownTimer myTimer;

    // temporary string to show the parsed response
    private String jsonResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_page);

        Qarea = (TextView) findViewById(R.id.quesArea);
        timerArea = (TextView) findViewById(R.id.timer);
        previous = (Button) findViewById(R.id.QPrevious);
        submit = (Button) findViewById(R.id.QSubmit);
        next = (Button) findViewById(R.id.QNext);
        end = (Button) findViewById(R.id.QEnd);
        OptionGroup = (RadioGroup) findViewById(R.id.OptionGroup);

        OptionA = (RadioButton)findViewById(R.id.A_Opt);
        OptionB = (RadioButton)findViewById(R.id.B_Opt);
        OptionC = (RadioButton)findViewById(R.id.C_Opt);
        OptionD = (RadioButton)findViewById(R.id.D_Opt);

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);

        //makeJsonObjectRequest(ques_now);

        myTimer = new CountDownTimer((60000),1000) {     //(300000*6)
            @Override
            public void onTick(long millisUntilFinished) {
               // timerArea.setText("Time Remaining : " + (millisUntilFinished/1000)/60);
                timerArea.setText("Time Remaining : " + (millisUntilFinished/1000));
            }

            @Override
            public void onFinish() {
                //finish();
                //Intent i = new Intent(getApplicationContext(),Result.class);
                //startActivity(i);
                topen(getCurrentFocus());
            }
        };

        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ques_now==0){
                    ques_now=ques_count-1;
                    makeJsonObjectRequest(ques_now);
                }
                else {
                    ques_now--;
                    makeJsonObjectRequest(ques_now);
                }
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (OptionGroup.getCheckedRadioButtonId()){
                    case R.id.A_Opt:
                        Toast.makeText(getApplicationContext(),"A",Toast.LENGTH_SHORT).show();
                        ans_given=OptionA.getText();
                        putResponse();
                        return;
                    case R.id.B_Opt:
                        Toast.makeText(getApplicationContext(),"B",Toast.LENGTH_SHORT).show();
                        ans_given=OptionB.getText();
                        putResponse();
                        return;
                    case R.id.C_Opt:
                        Toast.makeText(getApplicationContext(),"C",Toast.LENGTH_SHORT).show();
                        ans_given=OptionC.getText();
                        putResponse();
                        return;
                    case R.id.D_Opt:
                        Toast.makeText(getApplicationContext(),"D",Toast.LENGTH_SHORT).show();
                        ans_given=OptionD.getText();
                        putResponse();
                        return;
                    default:
                        return;
                }
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ques_now==ques_count-1){
                    ques_now=0;
                    makeJsonObjectRequest(ques_now);
                }
                else{
                    ques_now++;
                    makeJsonObjectRequest(ques_now);
                }
            }
        });

        end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Eopen(v);
            }
        });

        OptionGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {

            }
        });

    }



    /**
     * Method to make json object request where json response starts wtih {
     * */
    private void makeJsonObjectRequest(final int q_no) {
        showpDialog();

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                urlJsonObj, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {
                    // Parsing json object response
                    // response will be a json object
                    JSONArray questions = response.getJSONArray("questions");
                    ques_count=questions.length();
                    JSONObject Q = questions.getJSONObject(q_no);
                    String id = Q.getString("id");
                    String ques = Q.getString("ques");
                    String a = Q.getString("a");
                    String b = Q.getString("b");
                    String c = Q.getString("c");
                    String d = Q.getString("d");

                    jsonResponse = "";
                    jsonResponse += "Q : " + id + "\n\n";
                    jsonResponse += "Ques : " + ques + "\n\n";
                    //jsonResponse += "A : " + a + "\n\n";
                    //jsonResponse += "B : " + b + "\n\n";
                    //jsonResponse += "C : " + c + "\n\n";
                    //jsonResponse += "D : " + d + "\n\n";

                    Qarea.setText(jsonResponse);
                    OptionA.setText(a);
                    OptionB.setText(b);
                    OptionC.setText(c);
                    OptionD.setText(d);

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
                hidepDialog();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
                // hide the progress dialog
                hidepDialog();
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }

    /**
     * Method to make json array request where response starts with [
     * */
    private void putResponse() {
        StringRequest postRequest = new StringRequest(Request.Method.POST, responseURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<>();
                db=new SQLiteHandler(getApplicationContext());
                // Fetching user details from sqlite
                HashMap<String, String> user = db.getUserDetails();
                uInputCount++;
                String name = user.get("name");
                String email = user.get("email");
                String u_id_response = user.get("uid");
                // the POST parameters:
                params.put("ID", Integer.toString(uInputCount));
                params.put("U_ID", u_id_response);
                params.put("U_Name", name);
                params.put("U_Email", email);
                params.put("Q_Num", Integer.toString(ques_now));
                params.put("Ans", ans_given.toString());
                params.put("Quiz_No", "1");
                return params;
            }
        };
        Volley.newRequestQueue(this).add(postRequest);
    }

    private void showpDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.quiz_options, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {

        switch (item.getItemId()) {
            case R.id.quiz_start:
                makeJsonObjectRequest(ques_now);
                myTimer.start();
                return true;
            case R.id.quiz_help:
                Intent i = new Intent(getApplicationContext(),ContactUs.class);
                startActivity(i);
                return true;
            case R.id.quiz_quit:
                Qopen(getCurrentFocus());
                return true;
            case R.id.quiz_logout:
                open(getCurrentFocus());
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void logoutUser() {
        session.setLogin(false);

        db.deleteUsers();

        // Launching the login activity
        Intent intent = new Intent(getApplicationContext(), LoginTab.class);
        startActivity(intent);
        finish();
    }

    public void Qopen(View view){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Are you sure?");
        alertDialogBuilder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        Intent i=new Intent(getApplicationContext(),Home.class);
                        startActivity(i);
                    }
                });

        alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void open(View view){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Are you sure?");
        alertDialogBuilder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        session = new SessionManager(getApplicationContext());

                        if (!session.isLoggedIn()) {
                            logoutUser();
                        }
                    }
                });

        alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void Eopen(View view){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Are you sure? " +
                "\n Your Marked Answers will be Submitted! ");
        alertDialogBuilder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        Intent i=new Intent(getApplicationContext(),Result.class);
                        startActivity(i);
                    }
                });

        alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void topen(View view){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Time Over!!!");
        alertDialogBuilder.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        Intent i = new Intent(getApplicationContext(),Result.class);
                        startActivity(i);
                    }
                });

        alertDialogBuilder.setNegativeButton("Give Review",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent i = new Intent(getApplicationContext(),ContactUs.class);
                startActivity(i);
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}




