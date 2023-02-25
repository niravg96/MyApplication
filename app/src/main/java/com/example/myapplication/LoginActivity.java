package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.util.Patterns;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.ReferenceQueue;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {

    EditText email;
    TextInputEditText password;
    Button loginbutton;
    TextView signuptv;
    String email_login,password_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email =findViewById(R.id.edittext_emailaddress_login);
        password =findViewById(R.id.edittextpasswordlogin);
        signuptv =findViewById(R.id.linkforlogin2);
        loginbutton =findViewById(R.id.login_button);



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

                    email_login = email.getText().toString().trim();
                    password_login = password.getText().toString().trim();

                    if(email.getText().toString().length() > 0 && password.getText().toString().length() > 0)
                    {
                        if(Patterns.EMAIL_ADDRESS.matcher(email_login).matches())
                        {
                            if(password.getText().toString().length() < 8)
                            {
                                Toast.makeText(getApplicationContext(),"Provide Password Length above 8",Toast.LENGTH_LONG).show();
                            }
                            else if(!isValidPassword(password_login))
                            {
                                Toast.makeText(getApplicationContext(),"Password is not as criteria",Toast.LENGTH_LONG).show();
                            }
                            else if(password.getText().toString().length() > 24)
                            {
                                Toast.makeText(getApplicationContext(),"Provide Password Length equal to 24",Toast.LENGTH_LONG).show();
                            }
                            else
                            {
                                token();
                            }
                            // valid email address
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(),"Please Enter valid Email address",Toast.LENGTH_LONG).show();
                        }
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
            Log.e("Error ","2");
            JSONObject req = new JSONObject();

            try
            {
                req.put("email",email.getText().toString());  // DATA OF field which we will enter while doing sign in
                req.put("password",password.getText().toString());

                req.put("login_by", 5);
            }
            catch(Exception ex)
            {
                Toast.makeText( LoginActivity.this, "Exception : "+ex, Toast.LENGTH_SHORT).show();
            }

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url,req, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response)
                {
                    try
                    {
                        if(response.getBoolean("status"))
                        {

                            String message = response.getString("message");
                            Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();


                            /*String email2 = resobj.getString("email");
                            String mobile_no = resobj.getString("mobile_no");*/

                          /*  Toast.makeText(LoginActivity.this, "Log In Successfully", Toast.LENGTH_SHORT).show();*/

                            email.setText("");
                            password.setText("");

                           /* Log.e("Check", email2 + mobile_no);*/
                        }
                        email.setText("");password.setText("");


                    } catch (Exception ex) {
                        ex.printStackTrace();

                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(LoginActivity.this, "Fail to get Response : "+error, Toast.LENGTH_SHORT).show();
                }
            }){
              /*  @Override
                protected Map<String,String>getParams()
                {
                    Map<String,String> params = new HashMap<String,String>();
                    params.put("email",email_login);
                    params.put("password",password_login);
                    params.put("login_by", String.valueOf(5));
                    return params;
                }*/
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
    public void emailvalidator(EditText email)
    {
        String emailtotext = email.getText().toString();

        // Android offers the inbuilt patterns which the entered
        // data from the EditText field needs to be compared with
        // In this case the entered data needs to compared with
        // the EMAIL_ADDRESS, which is implemented same below
        if(!emailtotext.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(emailtotext).matches())
        {
                // valid email address
        }
        else
        {
            Toast.makeText(getApplicationContext(),"Please Enter valid Email address",Toast.LENGTH_LONG).show();
        }
    }
    public static boolean isValidPassword(final String password)  // 1 number , 1 Uppercase , 1 special symbol
    {
        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();
    }
}