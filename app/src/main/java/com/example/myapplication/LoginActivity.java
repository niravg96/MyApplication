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

import java.lang.ref.ReferenceQueue;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    EditText email,pasword;
    Button loginbutton;
    TextView signuptv;
    String email_login,password_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email =findViewById(R.id.edittext_emailaddress_login);
        pasword =findViewById(R.id.edittextpassword);
        signuptv =findViewById(R.id.linkforlogin2);
        loginbutton =findViewById(R.id.login_button);

        email_login = email.getText().toString().trim();
        password_login = pasword.getText().toString().trim();

        signuptv.setOnClickListener(new View.OnClickListener() {  // CLICK EVENT OF SIGN UP (LINK OF SIGN UP)
            @Override
            public void onClick(View v) {

                Intent i =new Intent(LoginActivity.this,MainActivity.class);
                startActivity(i);
                finish();
            }
        });
        loginbutton.setOnClickListener(new View.OnClickListener() {  // CLICK EVENT OF SIGN IN
            @Override
            public void onClick(View v) {

                try
                {
                    if(email.getText().toString().length() > 0 && pasword.getText().toString().length() > 0)
                    {
                        token();
                    }
                    else
                    {
                        Toast.makeText(LoginActivity.this, "Please Enter All Details", Toast.LENGTH_SHORT).show();
                    }
                }
                catch(Exception e)
                {
                    Toast.makeText(LoginActivity.this, "Error : "+e, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    private void postlogindata(String access_token)
    {
        try
        {
            String url = "https://admin.p9bistro.com/index.php/SignIn";

            RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);

            StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response)
                {

                    try
                    {

                        JSONObject resobj = new JSONObject(response);
                        String email2 = resobj.getString("email");
                        String mobile_no = resobj.getString("mobile_no");

                        Toast.makeText(LoginActivity.this, "Log In Successfully", Toast.LENGTH_SHORT).show();

                        email.setText("");
                        pasword.setText("");

                        Log.e("Check", email2 + mobile_no);

                    } catch (JSONException ex) {
                        ex.printStackTrace();

                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(LoginActivity.this, "Fail to get Response : "+error, Toast.LENGTH_SHORT).show();
                }
            }){
                @Override
                protected Map<String,String>getParams()
                {
                    Map<String,String> params = new HashMap<String,String>();
                    params.put("email",email_login);
                    params.put("password",password_login);
                    params.put("login_by", String.valueOf(5));
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
        }
        catch (Exception ex)
        {
            Toast.makeText(LoginActivity.this, "Error 3 : "+ex, Toast.LENGTH_SHORT).show();
        }
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
                    postlogindata(access_token);

                } catch (JSONException je) {
                    Toast.makeText(LoginActivity.this, "Error 2 : " + je, Toast.LENGTH_SHORT).show();
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
                public Map<String,String> getHeaders() throws AuthFailureError{
                    HashMap<String,String> headers = new HashMap<>();
                    headers.put("x-api-key","XABRTYUX@123YTUFGB");
                    return headers;
                }
        };
        RequestQueue requestquese = Volley.newRequestQueue(getApplicationContext());
        requestquese.add(stringRequest);
    }
}