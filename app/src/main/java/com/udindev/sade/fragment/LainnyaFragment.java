package com.udindev.sade.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.udindev.sade.R;

public class LainnyaFragment extends Fragment {

    public LainnyaFragment() {
        // Required empty public constructor
    }

    public static LainnyaFragment newInstance(String param1, String param2) {
        LainnyaFragment fragment = new LainnyaFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_lainnya, container, false);
    }
}