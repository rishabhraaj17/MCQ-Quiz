package com.example.rish.project;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.example.rish.project.R.id.loginbtn;
import static com.example.rish.project.R.id.regNow;

public class LoginTab extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */



    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_tab);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

       /* FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

    }


   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login_tab, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";
        private static final String TAG = LoginTab.class.getSimpleName();
        private Button btnLogin;
        private Button regnow;
        public EditText Iemail;
        public EditText Ipassword;
        private ProgressDialog pDialog;
        private SessionManager session;
        private SQLiteHandler db;

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            if(getArguments().getInt(ARG_SECTION_NUMBER)==1){
                View view = inflater.inflate(R.layout.fragment_login_tab, container, false);
                btnLogin = (Button) view.findViewById(loginbtn);
                regnow=(Button) view.findViewById(regNow);
                Iemail = (EditText) view.findViewById(R.id.username);
                Ipassword = (EditText) view.findViewById(R.id.password);

                // Progress dialog
                pDialog = new ProgressDialog(getContext());
                pDialog.setCancelable(false);

                // SQLite database handler
                db = new SQLiteHandler(getContext());

                // Session manager
                session = new SessionManager(getContext());

                // Check if user is already logged in or not
                if (session.isLoggedIn()) {
                    // User is already logged in. Take him to main activity
                    Intent intent = new Intent(getContext(), Home.class);
                    startActivity(intent);
                }


                btnLogin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getContext(),"Examinee",Toast.LENGTH_SHORT).show();
                        String email = Iemail.getText().toString().trim();
                        String password = Ipassword.getText().toString().trim();

                        // Check for empty data in the form
                        if (!email.isEmpty() && !password.isEmpty()) {
                            // login user
                            checkLogin(email, password);
                        } else {
                            // Prompt user to enter credentials
                            Toast.makeText(getContext(),
                                    "Please enter the credentials!", Toast.LENGTH_LONG)
                                    .show();
                        }

                    }
                });

                regnow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i=new Intent(getContext(),RegisterTab.class);
                        startActivity(i);
                    }
                });
                //TextView textView = (TextView) rootView.findViewById(R.id.section_label);
                //textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
                return view;
            }
            else{
                View view = inflater.inflate(R.layout.fragment_login_tab0, container, false);
                btnLogin = (Button) view.findViewById(R.id.loginbtn0);
                btnLogin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getContext(),"Examiner",Toast.LENGTH_SHORT).show();
                    }
                });
                //TextView textView = (TextView) rootView.findViewById(R.id.section_label);
                //textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
                return view;
            }
        }

        /**
         * function to verify login details in mysql db
         * */
        private void checkLogin(final String email, final String password) {
            // Tag used to cancel the request
            String tag_string_req = "req_login";

            pDialog.setMessage("Logging in ...");
            showDialog();

            StringRequest strReq = new StringRequest(Request.Method.POST,
                    AppConfig.URL_LOGIN, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    Log.d(TAG, "Login Response: " + response.toString());
                    hideDialog();

                    try {
                        JSONObject jObj = new JSONObject(response);
                        boolean error = jObj.getBoolean("error");

                        // Check for error node in json
                        if (!error) {
                            // user successfully logged in
                            // Create login session
                            session.setLogin(true);

                            // Now store the user in SQLite
                            String uid = jObj.getString("uid");

                            JSONObject user = jObj.getJSONObject("user");
                            String name = user.getString("name");
                            String email = user.getString("email");
                            String created_at = user
                                    .getString("created_at");

                            // Inserting row in users table
                            db.addUser(name, email, uid, created_at);
                            Toast.makeText(getContext(), "User successfully logged in now!", Toast.LENGTH_LONG).show();

                            Intent i=new Intent(getContext(),Home.class);
                            startActivity(i);

                        } else {
                            // Error in login. Get the error message
                            String errorMsg = jObj.getString("error_msg");
                            Toast.makeText(getContext(),
                                    errorMsg, Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        // JSON error
                        e.printStackTrace();
                        Toast.makeText(getContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }

                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e(TAG, "Login Error: " + error.getMessage());
                    Toast.makeText(getContext(),
                            error.getMessage(), Toast.LENGTH_LONG).show();
                    hideDialog();
                }
            }) {

                @Override
                protected Map<String, String> getParams() {
                    // Posting parameters to login url
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("email", email);
                    params.put("password", password);

                    return params;
                }

            };

            // Adding request to request queue
            AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
        }

        private void showDialog() {
            if (!pDialog.isShowing())
                pDialog.show();
        }

        private void hideDialog() {
            if (pDialog.isShowing())
                pDialog.dismiss();
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Examinee";
                case 1:
                    return "Examiner";
            }
            return null;
        }
    }
}
