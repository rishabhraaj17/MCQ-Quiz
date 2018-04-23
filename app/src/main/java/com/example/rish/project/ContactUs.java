package com.example.rish.project;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ContactUs extends AppCompatActivity {

    private String responseURL = "http://192.168.43.180/android_login_api/Contact.php";
    EditText e;
    Button b;
    SQLiteHandler db;
    int uInputCount=0;
    String msg="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        e = (EditText)findViewById(R.id.helpInputBox);
        b = (Button) findViewById(R.id.ContactButton);

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Thank You for Contacting Us!",Toast.LENGTH_LONG).show();
                msg = e.getText().toString().trim();
                putResponse();
            }
        });
    }

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
                params.put("U_NAME", name);
                params.put("U_EMAIL", email);
                params.put("MSG", msg);
                return params;
            }
        };
        Volley.newRequestQueue(this).add(postRequest);
    }

}
