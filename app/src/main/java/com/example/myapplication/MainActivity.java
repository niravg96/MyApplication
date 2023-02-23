package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

public class MainActivity extends AppCompatActivity
{

    EditText username_edt,password_edt,mobileno_edt,email_edt;
    Button registerbutton;
    TextView signIntv;
    String username,password,mobileno,email;

    @Override
    protected void onCreate(Bundle savedInstanceState) { 
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        username_edt = findViewById(R.id.edittextusername);
        password_edt = findViewById(R.id.edittextpassword);
        mobileno_edt = findViewById(R.id.edittextmobileno);
        email_edt = findViewById(R.id.edittextemailaddress);
        registerbutton = findViewById(R.id.registerbutton);
        signIntv = findViewById(R.id.linkforlogin2);

        username = username_edt.getText().toString();
        password = password_edt.getText().toString();
        mobileno = mobileno_edt.getText().toString();
        email = email_edt.getText().toString();

        signIntv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i =new Intent(MainActivity.this,LoginActivity.class);
                startActivity(i);
              //  finish();
            }
        });

        registerbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try
                {
                    if(username_edt.getText().toString().length() > 0  && password_edt.getText().toString().length() > 0  && mobileno_edt.getText().toString().length() > 0  && email_edt.getText().toString().length() > 0)
                    {
                        token();
                    }
                    else
                    {
                        Toast.makeText(MainActivity.this, "Enter All Details", Toast.LENGTH_SHORT).show();
                    }
                }
                catch (Exception ex)
                {
                    Toast.makeText(MainActivity.this, "Exception : "+ex, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void postregisterdata(String access_token)
    {


            String url = "https://admin.p9bistro.com/index.php/SignUp";

            RequestQueue queue = Volley.newRequestQueue(MainActivity.this);

            StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response)
                {

                    try
                    {
                        JSONObject resobj = new JSONObject(response);

                        if(resobj.getString("status").equals("true"))
                        {
                            String message = resobj.getString("message");
                            Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();

                            JSONObject jsonData = resobj.getJSONObject("data");  // responses which we got after successful tun

                            String id = jsonData.getString("id");
                            String username = jsonData.getString("username");
                            String mobile_no = jsonData.getString("mobile_no");
                            String email = jsonData.getString("email");
                            String profile = jsonData.getString("profile");
                            String api_key = jsonData.getString("api_key");

                        }

                        username_edt.setText("");
                        password_edt.setText("");
                        mobileno_edt.setText("");
                        email_edt.setText("");

                        Log.e("Check Data", username+password+email+mobileno);

                    } catch (JSONException ex)
                    {
                        ex.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    Toast.makeText( MainActivity.this, "Fail to get Response : "+error, Toast.LENGTH_SHORT).show();
                }
            }){
                @Override
                protected Map<String,String> getParams()
                {
                    Map<String,String> params = new HashMap<String,String>();

                    params.put("username",username);  // DATA OF field which we will enter while doing sign up
                    params.put("password",password);
                    params.put("mobile_no",mobileno);
                    params.put("email",email);
                    params.put("profile","nirav");
                    params.put("register_by", String.valueOf(5));
                    return params;
                }
                @Override
                public Map<String,String>getHeaders()throws AuthFailureError
                {
                    Map<String,String>params = new HashMap<String ,String>();
                    params.put("authorization",access_token);
                    return params;
                }
            };
        RequestQueue requestquese = Volley.newRequestQueue(getApplicationContext());
        requestquese.add(request);

    }
    private void token()
    {

            String url = "https://admin.p9bistro.com/index.php/generate_auth_token";

            Log.e("checklog", url + "");

            StringRequest stringRequest =new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    Log.e("checklog", response + "");
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response);
                        String access_token = jsonObject.getString("access_token");
                        Log.e("ACCESSTOKEN", access_token);
                        postregisterdata(access_token);

                    } catch (JSONException je) {
                        Toast.makeText(MainActivity.this, "Error 2 : " + je, Toast.LENGTH_SHORT).show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    Log.e("checklog",error + "");
                    Toast.makeText(getApplicationContext(), "Timeout Error", Toast.LENGTH_LONG).show();

                }
            }){
                @Override
                public Map<String,String> getHeaders() throws AuthFailureError
                {
                    HashMap<String,String> headers = new HashMap<>();
                    headers.put("x-api-key","XABRTYUX@123YTUFGB");
                    return headers;
                }
            };
            RequestQueue requestquese = Volley.newRequestQueue(getApplicationContext());
            requestquese.add(stringRequest);
        }
    }