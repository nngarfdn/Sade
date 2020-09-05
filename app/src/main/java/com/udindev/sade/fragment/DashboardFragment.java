package com.udindev.sade.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.mindorks.editdrawabletext.DrawablePosition;
import com.mindorks.editdrawabletext.EditDrawableText;
import com.mindorks.editdrawabletext.onDrawableClickListener;
import com.udindev.sade.R;

import org.jetbrains.annotations.NotNull;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DashboardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DashboardFragment extends Fragment {

    private static final String TAG = "DashboardFragment";
    EditDrawableText search;

    public DashboardFragment() {

    }
    public static DashboardFragment newInstance(String param1, String param2) {
        DashboardFragment fragment = new DashboardFragment();
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
        return inflater.inflate(R.layout.fragment_dashboard, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        search = view.findViewById(R.id.search);
        search.setDrawableClickListener(target -> {
            if (target == DrawablePosition.RIGHT) {
                String text = search.getText().toString();
                Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
            }
        });
    }
}