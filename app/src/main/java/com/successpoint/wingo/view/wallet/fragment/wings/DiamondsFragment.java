package com.successpoint.wingo.view.wallet.fragment.wings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.hannesdorfmann.mosby3.mvp.MvpFragment;
import com.hannesdorfmann.mosby3.mvp.MvpView;
import com.successpoint.wingo.App;
import com.successpoint.wingo.R;

public class DiamondsFragment extends Fragment {
    private View view;

    TextView diamonds_count;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.wallet_diamonds, container, false);
            diamonds_count = view.findViewById(R.id.diamonds);
            diamonds_count.setText(App.userModelObject_of_Project.getDiamonds() + "");
        }
        return view;
    }

}