package com.gurezkiygmail.foxhunting;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.VolleyError;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {

    EditText input_name,input_phone,input_password,input_date,input_sms;
    Button signup,sendsms,gotosignin;
    TextView textView;
    ProgressDialog progressDialog;
    Animation animationIn;
    Animation animationOut;
    Animation animationIn2;
    Animation animationOut2;
    RelativeLayout fisrt,second,third;
    private SessionManager session;
    String tag_string_req;
    private static final String TAG = SignupActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        session = new SessionManager(getApplicationContext());
        input_phone=(EditText)findViewById(R.id.phone);
        input_date=(EditText)findViewById(R.id.date);
        input_date.addTextChangedListener(new TextWatcher() {
            private String current = "";
            private String ddmmyyyy = "DDMMYYYY";
            private Calendar cal = Calendar.getInstance();

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().equals(current)) {
                    String clean = s.toString().replaceAll("[^\\d.]", "");
                    String cleanC = current.replaceAll("[^\\d.]", "");

                    int cl = clean.length();
                    int sel = cl;
                    for (int i = 2; i <= cl && i < 6; i += 2) {
                        sel++;
                    }
                    if (clean.equals(cleanC)) sel--;

                    if (clean.length() < 8){
                        clean = clean + ddmmyyyy.substring(clean.length());
                    }else{
                        int day  = Integer.parseInt(clean.substring(0,2));
                        int mon  = Integer.parseInt(clean.substring(2,4));
                        int year = Integer.parseInt(clean.substring(4,8));

                        if(mon > 12) mon = 12;
                        cal.set(Calendar.MONTH, mon-1);
                        year = (year<1900)?1900:(year>2100)?2100:year;
                        cal.set(Calendar.YEAR, year);

                        day = (day > cal.getActualMaximum(Calendar.DATE))? cal.getActualMaximum(Calendar.DATE):day;
                        clean = String.format("%02d%02d%02d",day, mon, year);
                    }

                    clean = String.format("%s/%s/%s", clean.substring(0, 2),
                            clean.substring(2, 4),
                            clean.substring(4, 8));

                    sel = sel < 0 ? 0 : sel;
                    current = clean;
                    input_date.setText(current);
                    input_date.setSelection(sel < current.length() ? sel : current.length());
                }
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void afterTextChanged(Editable s) {}
        });
        input_sms =(EditText)findViewById(R.id.sms);
        signup=(Button)findViewById(R.id.signup);
        sendsms=(Button)findViewById(R.id.sendSms);
        gotosignin = (Button)findViewById(R.id.gotosignin);
        textView=(TextView)findViewById(R.id.create_account);
        session = new SessionManager(getApplicationContext());
        fisrt = (RelativeLayout)findViewById(R.id.firstStep);
        second = (RelativeLayout)findViewById(R.id.secondStep);
        third = (RelativeLayout)findViewById(R.id.thirdStep);
        session = new SessionManager(getApplicationContext());
        animationIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.in);
        animationOut = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.out);
        animationIn2 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.in);
        animationOut2 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.out);
        animationIn.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                second.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        animationOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                fisrt.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        animationIn2.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                third.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        animationOut2.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                second.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        tag_string_req = "req_register";
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                if(!validate()){
                    Snackbar.make(view,"Ошибка! Некорректные данные",Snackbar.LENGTH_SHORT).show();
                }
                else{
                    progressDialog=ProgressDialog.show(SignupActivity.this,"","Создание профиля",false);

                    StringRequest strReq = new StringRequest(Request.Method.POST, Constants.URL.REGISTRATION, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jObj = new JSONObject(response);
                                boolean success = jObj.getBoolean("success");
                                if (success) {
                                    progressDialog.setMessage("Успешно!");
                                    Handler handler=new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            progressDialog.dismiss();
                                            fisrt.startAnimation(animationOut);
                                            second.startAnimation(animationIn);
                                        }
                                    }, 2000);

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
                            Log.e(TAG, "Registration Error: " + error.getMessage());
                            progressDialog.setMessage( Constants.getErrorMessage(error.getMessage()));
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
                            Date dateN = null;
                            boolean correct = true;
                            String dd = "0";
                            try {
                                DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                                formatter.setLenient(false);
                                dateN = formatter.parse(input_date.getText().toString());
                                dd = String.valueOf((long) dateN.getTime() / 1000);
                            } catch (ParseException e) {
                                dd = "0";
                            }
                            params.put("birthday",dd);
                            params.put("device_id",AppController.getInstance().getDeviceId());
                            return params;
                        }

                    };
                    AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
                }
            }
        });
        sendsms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(input_sms.getText().length()<4){
                    input_sms.setError("Введите корректный код");
                }else{
                    input_sms.setError(null);
                    progressDialog=ProgressDialog.show(SignupActivity.this,"","Активация...",false);
                    StringRequest strReq = new StringRequest(Request.Method.POST, Constants.URL.SEND_CODE, new Response.Listener<String>() {
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
                                        session.setToken(token);
                                        session.setLogin(true);
                                        progressDialog.setMessage("Успешно!");
                                        Handler handler=new Handler();
                                        handler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                progressDialog.dismiss();
                                                third.startAnimation(animationIn2);
                                                second.startAnimation(animationOut2);
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
                            Log.e(TAG, "Registration Error: " + error.getMessage());
                            progressDialog.setMessage("Ошибка: " + Constants.getErrorMessage(error.getMessage()));
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
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("phone", input_phone.getText().toString());
                            params.put("code", input_sms.getText().toString());
                            params.put("device_id",AppController.getInstance().getDeviceId());
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
                Intent intent = new Intent(
                        SignupActivity.this,
                        SigninActivity.class);
                startActivity(intent);
                finish();
            }
        });
        gotosignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(
                        SignupActivity.this,
                        MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public boolean validate(){
        boolean valid=true;
        String phone=input_phone.getText().toString();
        String date=input_date.getText().toString();
        if(phone.isEmpty() || !Patterns.PHONE.matcher(phone).matches()){
            input_phone.setError("Введите корректный номер телефона");
            valid=false;
        }
        else{
            input_phone.setError(null);
        }

        Date dateN = null;
        boolean correct = true;
        try {
            DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            formatter.setLenient(false);
            dateN = formatter.parse(date);
        } catch (ParseException e) {
            correct = false;
        }

        if(date.isEmpty() || !correct){
            input_date.setError("Введите корректную дату");
            valid=false;
        }
        else{
            input_phone.setError(null);
        }
        return valid;
    }
}
