package com.successpoint.wingo.view.wallet.fragment.wings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.successpoint.wingo.App;
import com.successpoint.wingo.R;

import androidx.fragment.app.Fragment;

public class WingsFragment extends Fragment {
    private View view;

    TextView Wings_count;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_wings, container, false);
            Wings_count = view.findViewById(R.id.Wings_count);
            Wings_count.setText(App.userModelObject_of_Project.getWings() + "");
        }
        return view;
    }

}