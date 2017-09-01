package com.gurezkiygmail.foxhunting;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.gurezkiygmail.foxhunting.fragments.fragment_index;
import com.gurezkiygmail.foxhunting.fragments.fragment_profile;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.mikepenz.materialdrawer.util.AbstractDrawerImageLoader;
import com.mikepenz.materialdrawer.util.DrawerImageLoader;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {
    SessionManager session;
    private TextView txtName;
    private TextView txtPhone;
    private EditText input_name;
    private Button btn_next;
    private Toolbar toolbar;
    private  Drawer result;
    public  int PAGE = 1;
    public static IProfile profile;
    private static AccountHeader headerResult;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(getApplicationContext());
        ImageLoader.getInstance().init(config.build());
        Constants.options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.deactivated)
                .showImageForEmptyUri(R.drawable.deactivated)
                .showImageOnFail(R.drawable.deactivated)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
        session = new SessionManager(getApplicationContext());
        if(session.isLoggedIn()){
            getProfile();
        }else{
            Intent intent = new Intent(this, SignupActivity.class);
            startActivity(intent);
            finish();
        }
    }
    private void buildMain(){
        if(AppController.getInstance().Profile.name.length()==0){
            setContentView(R.layout.activity_main_first);
            input_name = (EditText) findViewById(R.id.input_name);
            input_name.requestFocus();
            txtName = (TextView) findViewById(R.id.name);
            txtPhone = (TextView) findViewById(R.id.email);
            txtName.setText(AppController.getInstance().Profile.name);
            txtPhone.setText(AppController.getInstance().Profile.phone);
            btn_next = (Button) findViewById(R.id.btn_next);
            btn_next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(input_name.getText().toString().length()>3 && input_name.getText().toString().length()<21){
                        String tag_string_req = "req_set_name";
                        progressDialog=ProgressDialog.show(MainActivity.this,"","Отправка...");
                        StringRequest strReq = new StringRequest(Request.Method.POST, Constants.URL.UPDATE, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jObj = new JSONObject(response);
                                    boolean success = jObj.getBoolean("success");
                                    if (success) {
                                        progressDialog.setMessage("Успешно!");
                                        AppController.getInstance().Profile.name = input_name.getText().toString();
                                        Handler handler=new Handler();
                                        handler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                progressDialog.dismiss();
                                                buildMain();
                                                return;
                                            }
                                        }, 1000);

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
                                progressDialog.setMessage(error.getMessage());
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
                                params.put("type", "name");
                                params.put("value", input_name.getText().toString());
                                params.put("token", session.getToken());
                                params.put("device_id",AppController.getInstance().getDeviceId());
                                return params;
                            }

                        };
                        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
                    }else{
                        input_name.setError("Длина имени менее 3 символов и не более 20");
                    }
                }
            });
            return;
        }

        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        NestedScrollView scrollView = (NestedScrollView) findViewById (R.id.nest_scrollview);
        scrollView.setFillViewport(true);
        DrawerImageLoader.init(new AbstractDrawerImageLoader() {
            @Override
            public void set(ImageView imageView, Uri uri, Drawable placeholder) {
                ImageLoader.getInstance().displayImage(uri.toString(), imageView, Constants.options);
            }

            @Override
            public void cancel(ImageView imageView) {

            }
        });
        profile = new ProfileDrawerItem().withName(AppController.getInstance().Profile.name).withEmail(AppController.getInstance().Profile.phone).withIcon(AppController.getInstance().Profile.avatar);
        headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.header)
                .addProfiles(
                        profile
                )
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean currentProfile) {
                        return false;
                    }
                })
                .build();

        PrimaryDrawerItem item1 = new PrimaryDrawerItem().withIcon(FontAwesome.Icon.faw_home).withIdentifier(1).withName(R.string.drawer_item_home);
        PrimaryDrawerItem item2 = new PrimaryDrawerItem().withIcon(FontAwesome.Icon.faw_user).withIdentifier(2).withName(R.string.drawer_item_profile);

        //SecondaryDrawerItem item3 = (SecondaryDrawerItem) new SecondaryDrawerItem().withIcon(FontAwesome.Icon.faw_cog).withIdentifier(2).withName(R.string.drawer_item_settings);
        SecondaryDrawerItem item_logout = (SecondaryDrawerItem) new SecondaryDrawerItem().withIcon(FontAwesome.Icon.faw_sign_out).withIdentifier(99).withName(R.string.drawer_item_exit);
        Drawer builder = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withActionBarDrawerToggle(true)
                .withAccountHeader(headerResult)
                .addDrawerItems(
                        item1,
                        item2,
                        new DividerDrawerItem(),
                        item_logout
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        switch ((int) drawerItem.getIdentifier()) {
                            case 1: {
                                PAGE = 1;
                                break;
                            }
                            case 2: {
                                PAGE = 2;
                                break;
                            }
                            case 99: {
                                Logout();
                                break;
                            }
                            default:
                                break;
                        }
                        setFragment();
                        return false;
                    }
                }).withOnDrawerListener(new Drawer.OnDrawerListener() {
                    @Override
                    public void onDrawerOpened(View drawerView) {
                        InputMethodManager inputMethodManager = (InputMethodManager) MainActivity.this.getSystemService(Activity.INPUT_METHOD_SERVICE);
                        inputMethodManager.hideSoftInputFromWindow(MainActivity.this.getCurrentFocus().getWindowToken(), 0);
                    }

                    @Override
                    public void onDrawerClosed(View drawerView) {
                    }

                    @Override
                    public void onDrawerSlide(View drawerView, float slideOffset) {

                    }
                }).build();
        setFragment();
    }
    public static void UpdateMaterialProfile(){
        headerResult.updateProfile(profile);
    }
    private void Logout(){
        session.setToken("");
        session.setLogin(false);
        Intent intent = new Intent(MainActivity.this, SigninActivity.class);
        startActivity(intent);
        finish();
    }
    private void setFragment(){
        android.support.v4.app.FragmentManager myFragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = myFragmentManager.beginTransaction();
        switch (PAGE){
            case 1:{
                fragmentTransaction.replace(R.id.viewPager, fragment_index.getInstance(getApplicationContext()));
                fragmentTransaction.commit();
                break;
            }
            case 2:{
                fragmentTransaction.replace(R.id.viewPager, fragment_profile.getInstance(getApplicationContext()));
                fragmentTransaction.commit();
                break;
            }
            default:break;
        }
    }
    @Override
    public void onBackPressed() {
        if(result!=null)
        if(result.isDrawerOpen()){
            result.closeDrawer();
        }
        else{
            super.onBackPressed();
        }
    }
    private void getProfile(){
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
                            JSONObject user = data.getJSONObject("userProfile");
                            String id = user.getString("id");
                            String name = user.getString("name");
                            String phone = user.getString("phone");
                            String birthday = user.getString("birthday");
                            String avatar = user.getString("avatar");
                            String access_level = user.getString("access_level");
                            String pet = user.getString("pet");
                            String max_sale = user.getString("max_sale");
                            String count_sales = user.getString("count_sales");
                            AppController.getInstance().Profile = new User(id,name,phone,birthday,avatar,access_level,pet,max_sale,count_sales);
                            session.setToken(token);
                            session.setLogin(true);
                            Handler handler=new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    buildMain();
                                }
                            }, 1000);
                        }else{
                            progressDialog=ProgressDialog.show(MainActivity.this,"",Constants.getErrorMessage("-1"));
                            Handler handler=new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    progressDialog.dismiss();
                                    Intent intent = new Intent(
                                            MainActivity.this,
                                            SigninActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }, 2000);
                        }
                    } else {
                        String errorMsg = jObj.getString("errormsg");
                        progressDialog=ProgressDialog.show(MainActivity.this,"",Constants.getErrorMessage(errorMsg));
                        Handler handler=new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                progressDialog.dismiss();
                                Intent intent = new Intent(
                                        MainActivity.this,
                                        SigninActivity.class);
                                startActivity(intent);
                                finish();
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
                progressDialog=ProgressDialog.show(MainActivity.this,"",error.getMessage());
                Handler handler=new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                        Intent intent = new Intent(
                                MainActivity.this,
                                SigninActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }, 2000);
            }
        }){

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("token", session.getToken());
                params.put("device_id",AppController.getInstance().getDeviceId());
                return params;
            }

        };
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        if (resultCode == this.RESULT_CANCELED) {
            return;
        }
        switch(requestCode) {
            case Constants.SELECT_PHOTO:{
                if(resultCode == RESULT_OK){
                    try {
                        final String tag_string_req = "upload_photo";
                        final Uri imageUri = imageReturnedIntent.getData();
                        final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                        final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                        final String img = Constants.getStringImage(selectedImage);
                        progressDialog=ProgressDialog.show(MainActivity.this,"","Отправка...");
                        StringRequest strReq = new StringRequest(Request.Method.POST, Constants.URL.UPDATE, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jObj = new JSONObject(response);
                                    boolean success = jObj.getBoolean("success");
                                    if (success) {
                                        String avatar = jObj.getString("data");
                                        AppController.getInstance().Profile.setAvatar(avatar);
                                        profile.withIcon(avatar);
                                        UpdateMaterialProfile();
                                        progressDialog.setMessage("Успешно!");
                                        Handler handler=new Handler();
                                        handler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                progressDialog.dismiss();
                                                setFragment();
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
                                params.put("type", "avatar");
                                params.put("value", img);
                                params.put("token", session.getToken());
                                return params;
                            }

                        };
                        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                }
                break;
            }
            default:{
                break;
            }

        }
    }
}
