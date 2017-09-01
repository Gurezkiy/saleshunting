package com.gurezkiygmail.foxhunting.fragments;

/**
 * Created by gurez on 16.08.2017.
 */
import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.View;

/**
 * Created by gurez on 30.03.2017.
 */
public class AbstractFragment extends Fragment {
    protected View view;
    private String title;
    protected Context context;

    public void setTitle(String title){
        this.title = title;
    }
    public String getTitle(){
        return this.title;
    }
}