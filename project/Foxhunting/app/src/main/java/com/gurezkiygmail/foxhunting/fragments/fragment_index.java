package com.gurezkiygmail.foxhunting.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gurezkiygmail.foxhunting.R;

/**
 * Created by gurez on 16.08.2017.
 */
public class fragment_index extends AbstractFragment {
    private static final int LAYOUT = R.layout.fragment_index;

    public static fragment_index getInstance(Context context) {
        Bundle args = new Bundle();
        fragment_index fragment = new fragment_index();
        fragment.setArguments(args);
        fragment.setContext(context);
        fragment.setTitle("Домашняя");
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(LAYOUT, container, false);
        return view;
    }
    public void setContext(Context context) {
        this.context = context;
    }
}
