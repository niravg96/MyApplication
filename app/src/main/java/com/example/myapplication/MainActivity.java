package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Toolbar;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

       EditText username2,password2,mobile_no2,email2;
       Button register_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        username2= findViewById(R.id.edittextusername);
         password2= findViewById(R.id.edittextpassword);
        mobile_no2= findViewById(R.id.edittextmobileno);
        email2= findViewById(R.id.edittextemailaddress);
        register_btn =findViewById(R.id.registerbutton);
        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if(username2.getText().toString().isEmpty() || password2.getText().toString().isEmpty() || mobile_no2.getText().toString().isEmpty() || email2.getText().toString().isEmpty())
                    {
                        Toast.makeText(MainActivity.this, "Please Enter All Details", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        token();
                     //   postDatausingVolley(username2.getText().toString().trim(),password2.getText().toString().trim(),mobile_no2.getText().toString().trim(),email2.getText().toString().trim());
                    }
                }
                catch (Exception e)
                {
                    Toast.makeText(MainActivity.this, "Error "+e, Toast.LENGTH_SHORT).show();
                }

            }
        });

       /* Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });*/
    }
    private void postDatausingVolley(String access_token)
    {
        try
        {
            String url = "https://admin.p9bistro.com/index.php/SignUp";
            RequestQueue queue = Volley.newRequestQueue(MainActivity.this);

            StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    username2.setText("");
                    password2.setText("");
                    email2.setText("");
                    mobile_no2.setText("");

                    try {
                        JSONObject resobj = new JSONObject(response);
                        String username = resobj.getString("username");
                        String password = resobj.getString("password");
                        String mobile_no = resobj.getString("mobile_no");
                        String email = resobj.getString("email");

                        Toast.makeText(MainActivity.this, "Registered Successfully", Toast.LENGTH_SHORT).show();
                        Log.e("Check",username+password+mobile_no+email);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    Toast.makeText(MainActivity.this, "Fail to get Response : "+error, Toast.LENGTH_SHORT).show();

                }
            }){
                @Override
                protected Map<String ,String> getParams()
                {
                    Map<String ,String> params = new HashMap<String,String>();
                    params.put("username", String.valueOf(username2));
                    params.put("password", String.valueOf(password2));
                    params.put("mobile_no", String.valueOf(mobile_no2));
                    params.put("email", String.valueOf(email2));
                    params.put("profile","Nirav");
                    params.put("register_by", String.valueOf(5));

                    return params;
                }
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("authorization", access_token);
                    return params;

                }
            };

        }
        catch (Exception e)
        {
            Toast.makeText(MainActivity.this, "Error 2 "+e, Toast.LENGTH_SHORT).show();
        }

    }
    private void token() {

        String url = "https://admin.p9bistro.com/index.php/generate_auth_token";

        Log.e("checklog", url + "");

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("checklog", response + "");
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);
                    String access_token = jsonObject.getString("access_token");
                    Log.e("ACCESSTOKEN", access_token);
                    postDatausingVolley(access_token);


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                                Log.e("checklog", error + "");
                                Toast.makeText(getApplicationContext(), "Timeout Error", Toast.LENGTH_LONG).show();
                            }
                        }) {
                            @Override
                            public Map<String, String> getHeaders() throws AuthFailureError {
                                HashMap<String, String> headers = new HashMap<>();
                                headers.put("x-api-key", "XABRTYUX@123YTUFGB");
                                return headers;
                            }
                        };
                        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                        requestQueue.add(stringRequest);

                    }
}