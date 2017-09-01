package com.gurezkiygmail.foxhunting;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SigninActivity extends AppCompatActivity {
    EditText input_password,input_phone;
    Button login;
    TextView textView;
    ProgressDialog progressDialog;
    SessionManager session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        input_phone=(EditText)findViewById(R.id.phone);
        input_password=(EditText)findViewById(R.id.password);
        login=(Button)findViewById(R.id.login);
        textView=(TextView)findViewById(R.id.create_account);
        View view = findViewById(android.R.id.content);
        session = new SessionManager(getApplicationContext());
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!validate()){
                    Snackbar.make(view, "Ошибка ввода!", Snackbar.LENGTH_SHORT).show();
                }
                else{
                    progressDialog=ProgressDialog.show(SigninActivity.this,"","Авторизация...");
                    String tag_string_req = "req_auth";
                    StringRequest strReq = new StringRequest(Request.Method.POST, Constants.URL.AUTHORIZE, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jObj = new JSONObject(response);
                                boolean success = jObj.getBoolean("success");
                                if (success) {
                                    JSONObject data = jObj.getJSONObject("data");
                                    boolean auth = data.getBoolean("auth");
                                    String token = data.getString("token");
                                    if(auth){
                                        progressDialog.setMessage("Успешно!");
                                        session.setToken(token);
                                        session.setLogin(true);
                                        progressDialog.setMessage("Успешно!");
                                        Handler handler=new Handler();
                                        handler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                progressDialog.dismiss();
                                                Intent intent = new Intent(
                                                        SigninActivity.this,
                                                         MainActivity.class);
                                                startActivity(intent);
                                                finish();
                                            }
                                        }, 2000);
                                    }else{
                                        progressDialog.setMessage(Constants.getErrorMessage("-1"));
                                        Handler handler=new Handler();
                                        handler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                progressDialog.dismiss();
                                            }
                                        }, 2000);
                                    }
                                } else {
                                    String errorMsg = jObj.getString("errormsg");
                                    progressDialog.setMessage(Constants.getErrorMessage(errorMsg));
                                    Handler handler=new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            progressDialog.dismiss();
                                        }
                                    }, 2000);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressDialog.setMessage(Constants.getErrorMessage("Ошибка: " + error.getMessage()));
                            Handler handler=new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    progressDialog.dismiss();
                                }
                            }, 2000);
                        }
                    }){

                        @Override
                        protected Map<String, String> getParams() {
                            // Posting params to register url
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("phone", input_phone.getText().toString());
                            params.put("password", input_password.getText().toString());
                            params.put("device_id",AppController.getInstance().getDeviceId());
                            params.put("gcm_registration_id",session.getGSM());
                            return params;
                        }

                    };
                    AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
                }
            }
        });

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(SigninActivity.this,SignupActivity.class);
                startActivity(intent);
            }
        });
    }

    public boolean validate(){

        boolean valid=true;

        String phone=input_phone.getText().toString();
        String password=input_password.getText().toString();

        if(phone.isEmpty() || !Patterns.PHONE.matcher(phone).matches()){
            input_phone.setError("Введите корректный номер телефона");
            valid=false;
        }
        else{
            input_phone.setError(null);
        }

        if (password.isEmpty() || password.length() < 5) {
            input_password.setError("Пароль не должен быть короче 5");
            valid = false;
        } else {
            input_password.setError(null);
        }
        return valid;
    }
}
