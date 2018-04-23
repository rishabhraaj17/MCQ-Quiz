package com.example.rish.project;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ShowAllQuestions extends AppCompatActivity {



    ArrayList<HashMap<String, String>> questionsList;

    // url to get all products list
    public static String url_all_questions = "http://192.168.0.4/android_login_api/readAllQ.php";



    ListView lv;

    private static String TAG = ShowAllQuestions.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_all_questions);

        // Hashmap for ListView
        questionsList = new ArrayList<HashMap<String, String>>();

        // Loading products in Background Thread
        new LoadAllQuestions().execute();

        // Get listview
        lv = (ListView)findViewById(R.id.list);

    }


class LoadAllQuestions extends AsyncTask<String, String, String> {

    /**
     * Before starting background thread Show Progress Dialog
     * */

    // Progress Dialog
    public ProgressDialog pDialog;

    // Creating JSON Parser object
    JSONParser jParser = new JSONParser();

    // JSON Node names
    public static final String TAG_SUCCESS = "success";
    public static final String TAG_Questions = "questions";
    public static final String TAG_ID = "id";
    public static final String TAG_Ques = "ques";

    // products JSONArray
    JSONArray products = null;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pDialog = new ProgressDialog(ShowAllQuestions.this);
        pDialog.setMessage("Loading products. Please wait...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
    }

    /**
     * getting All products from url
     * */
    protected String doInBackground(String... args) {
         JsonObjectRequest json = new JsonObjectRequest(Request.Method.GET,
                url_all_questions, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {
                    // Checking for SUCCESS TAG
                    int success = response.getInt(TAG_SUCCESS);

                    if (success == 1) {
                        // products found
                        // Getting Array of Products
                        products = response.getJSONArray(TAG_Questions);

                        // looping through All Products
                        for (int i = 0; i < products.length(); i++) {
                            org.json.JSONObject c = products.getJSONObject(i);

                            // Storing each json item in variable
                            String id = c.getString(TAG_ID);
                            String name = c.getString(TAG_Ques);

                            // creating new HashMap
                            HashMap<String, String> map = new HashMap<String, String>();

                            // adding each child node to HashMap key => value
                            map.put(TAG_ID, id);
                            map.put(TAG_Ques, name);

                            // adding HashList to ArrayList
                            questionsList.add(map);
                        }
                    } else {

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
                // hide the progress dialog

            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(json);

        return null;
    }

    /**
     * After completing background task Dismiss the progress dialog
     * **/
    protected void onPostExecute(String file_url) {
        // dismiss the dialog after getting all products
        pDialog.dismiss();
        // updating UI from Background Thread
        runOnUiThread(new Runnable() {
            public void run() {
                /**
                 * Updating parsed JSON data into ListView
                 * */
                Toast.makeText(getApplicationContext(),"in post exe",Toast.LENGTH_SHORT).show();
                ListAdapter adapter = new SimpleAdapter(
                        getApplicationContext(), questionsList,
                        R.layout.list_item, new String[] { "id",
                        "ques"},
                        new int[] { R.id.Nid, R.id.ques });
                // updating listview
                lv.setAdapter(adapter);
            }
        });

    }

    // function get json from url
    // by making HTTP POST or GET mehtod
    public org.json.JSONObject makeHttpRequest(String url, String method,
                                               List<NameValuePair> params) {

        InputStream is = null;
        org.json.JSONObject jObj = null;
        String json = "";

        // Making HTTP request
        try {

            // check for request method
            if(method == "POST"){
                // request method is POST
                // defaultHttpClient
                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(url);
                httpPost.setEntity(new UrlEncodedFormEntity(params));

                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();
                is = httpEntity.getContent();

            }else if(method == "GET"){
                // request method is GET
                DefaultHttpClient httpClient = new DefaultHttpClient();
                String paramString = URLEncodedUtils.format(params, "utf-8");
                url += "?" + paramString;
                HttpGet httpGet = new HttpGet(url);

                HttpResponse httpResponse = httpClient.execute(httpGet);
                HttpEntity httpEntity = httpResponse.getEntity();
                is = httpEntity.getContent();
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            json = sb.toString();
        } catch (Exception e) {
            Log.e("Buffer Error", "Error converting result " + e.toString());
        }

        // try parse the string to a JSON object
        try {
            JSONParser prs = new JSONParser();
            jObj = (org.json.JSONObject)prs.parse(json);
            //jObj = new JSONObject(json);
        } catch (ParseException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
            e.printStackTrace();
        }

        // return JSON String
        return jObj;

    }

}
}


