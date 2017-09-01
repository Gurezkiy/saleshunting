package com.gurezkiygmail.foxhunting.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.gurezkiygmail.foxhunting.AppController;
import com.gurezkiygmail.foxhunting.Constants;
import com.gurezkiygmail.foxhunting.MainActivity;
import com.gurezkiygmail.foxhunting.R;
import com.gurezkiygmail.foxhunting.SessionManager;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by gurez on 16.08.2017.
 */
public class fragment_profile extends AbstractFragment {
    private static final int LAYOUT = R.layout.fragment_profile;
    private SessionManager session;
    private TextView settings_phone;
    private TextView settings_name;
    private  TextView name;
    public static fragment_profile getInstance(Context context) {
        Bundle args = new Bundle();
        fragment_profile fragment = new fragment_profile();
        fragment.setArguments(args);
        fragment.setContext(context);
        fragment.setTitle("Мой профиль");
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        session = new SessionManager(getActivity().getApplicationContext());
        view = inflater.inflate(LAYOUT, container, false);
        de.hdodenhof.circleimageview.CircleImageView img = (de.hdodenhof.circleimageview.CircleImageView)view.findViewById(R.id.profile_image);
        ImageLoader.getInstance().displayImage(AppController.getInstance().Profile.avatar, img, Constants.options);
        name = (TextView)view.findViewById(R.id.profile_name);
        TextView best_sale = (TextView)view.findViewById(R.id.best_sale);
        best_sale.setText(String.valueOf(AppController.getInstance().Profile.max_sale));
        TextView count_sales = (TextView)view.findViewById(R.id.count_sales);
        count_sales.setText(String.valueOf(AppController.getInstance().Profile.count_sales));
        name.setText(AppController.getInstance().Profile.name);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });
        settings_phone = (TextView)view.findViewById(R.id.settings_phone);
        settings_phone.setText(AppController.getInstance().Profile.phone);
        Button button_settings_phone = (Button)view.findViewById(R.id.button_settings_phone);
        button_settings_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MaterialDialog.Builder(view.getContext())
                        .title(R.string.phone)
                        .inputRangeRes(5, 20, R.color.colorPrimaryDarkRed)
                        .inputType(InputType.TYPE_CLASS_PHONE)
                        .input(null,null, new MaterialDialog.InputCallback() {
                            @Override
                            public void onInput(MaterialDialog dialog, CharSequence input) {
                                updateProfile("phone", input.toString());
                            }
                        }).show();
            }
        });
        settings_name = (TextView)view.findViewById(R.id.settings_name);
        settings_name.setText(AppController.getInstance().Profile.name);
        Button button_settings_name = (Button)view.findViewById(R.id.button_settings_name);
        button_settings_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MaterialDialog.Builder(view.getContext())
                        .title(R.string.name)
                        .inputRangeRes(3, 20, R.color.colorPrimaryDarkRed)
                        .inputType(InputType.TYPE_CLASS_TEXT)
                        .input(null, null, new MaterialDialog.InputCallback() {
                            @Override
                            public void onInput(MaterialDialog dialog, CharSequence input) {
                                updateProfile("name", input.toString());
                            }
                        }).show();
            }
        });
        Button button_settings_session_close = (Button)view.findViewById(R.id.button_settings_session_close);
        button_settings_session_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MaterialDialog.Builder(view.getContext())
                        .title(R.string.settings_password_session_close)
                        .inputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)
                        .input(null,null, new MaterialDialog.InputCallback() {
                            @Override
                            public void onInput(MaterialDialog dialog, CharSequence input) {
                                updateProfile("session", input.toString());
                            }
                        }).show();
            }
        });
        TextView settings_password = (TextView)view.findViewById(R.id.settings_password);
        settings_password.setText("**********");
        Button button_settings_password = (Button)view.findViewById(R.id.button_settings_password);
        button_settings_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MaterialDialog.Builder(view.getContext())
                        .title(R.string.password)
                        .inputRangeRes(8, 20, R.color.colorPrimaryDarkRed)
                        .inputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)
                        .input(null,null, new MaterialDialog.InputCallback() {
                            @Override
                            public void onInput(MaterialDialog dialog, CharSequence input) {
                                updateProfile("password", input.toString());
                            }
                        }).show();
            }
        });
        return view;
    }
    private void updateProfile(final String what, final String value){
        String tag_string_req = "req_update_profile";
        StringRequest strReq = new StringRequest(Request.Method.POST, Constants.URL.UPDATE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean success = jObj.getBoolean("success");
                    if (success) {
                        Snackbar.make(view, "Успешно", Snackbar.LENGTH_SHORT).show();
                        switch (what){
                            case "phone":{
                                AppController.getInstance().Profile.setPhone(value);
                                settings_phone.setText(value);
                                MainActivity.profile.withEmail(value);
                                MainActivity.UpdateMaterialProfile();
                                break;
                            }
                            case "name":{
                                AppController.getInstance().Profile.setName(value);
                                name.setText(value);
                                settings_name.setText(value);
                                MainActivity.profile.withName(value);
                                MainActivity.UpdateMaterialProfile();
                                break;
                            }
                            case "session":{
                                break;
                            }
                            default:break;
                        }

                    } else {
                        String errorMsg = jObj.getString("errormsg");
                        Snackbar.make(view, Constants.getErrorMessage(errorMsg), Snackbar.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();
                params.put("type", what);
                params.put("value", value);
                params.put("token", session.getToken());
                params.put("device_id",AppController.getInstance().getDeviceId());
                return params;
            }

        };
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }
    private void showFileChooser() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        getActivity().startActivityForResult(galleryIntent, Constants.SELECT_PHOTO);
    }
    public void setContext(Context context) {
        this.context = context;
    }
}
