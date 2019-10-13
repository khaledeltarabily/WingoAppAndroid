package com.successpoint.wingo.view.mainactivity.fragments.nearby;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.easywaylocation.EasyWayLocation;
import com.example.easywaylocation.Listener;
import com.hannesdorfmann.mosby3.mvp.MvpFragment;
import com.successpoint.wingo.R;
import com.successpoint.wingo.model.NearbyModel;
import com.successpoint.wingo.utils.CommonMethods;
import com.successpoint.wingo.utils.Constants;
import com.successpoint.wingo.utils.Mainsharedprefs;
import com.successpoint.wingo.view.mainactivity.fragments.nearby.adapter.NearbyAdapter;

import java.util.HashMap;
import java.util.List;

public class NearbyFragment extends MvpFragment<NearbyView, NearbyPresenter> implements NearbyView {
    private View view;

    private TextView tvFilter;
    private RecyclerView rvNearby;
    EasyWayLocation easyWayLocation;
    private TextView tvMale;
    private TextView tvFemale;
    private TextView tvBoth;
    String lat,lon;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_nearby, container, false);

            tvFilter = view.findViewById(R.id.tvFilter);
            rvNearby = view.findViewById(R.id.rvNearby);

            easyWayLocation = new EasyWayLocation(getContext());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (getContext().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED && getContext().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    easyWayLocation.beginUpdates();
                } else {
                    String[] permissionRequest = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
                    requestPermissions(permissionRequest, 111);
                }
            } else {
                easyWayLocation.beginUpdates();
            }
            easyWayLocation.setListener(new Listener() {
                @Override
                public void locationOn() {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (getContext().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED && getContext().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                            easyWayLocation.beginUpdates();
                            lat =  String.valueOf(easyWayLocation.getLatitude());
                            lon = String.valueOf(easyWayLocation.getLongitude());
                            HashMap<String,String> params = new HashMap<>();
                            params.put("api_token", Constants.api_token);
                            params.put("user_token", Mainsharedprefs.getToken());
                            params.put("lat",lat);
                            params.put("lon", lon);
                            params.put("page", "1");
                            params.put("gender", "all");
                            presenter.getNearby(params);
                        } else {
                            String[] permissionRequest = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
                            requestPermissions(permissionRequest, 111);
                        }
                    } else {
                        easyWayLocation.beginUpdates();
                        lat =  String.valueOf(easyWayLocation.getLatitude());
                        lon = String.valueOf(easyWayLocation.getLongitude());
                        HashMap<String,String> params = new HashMap<>();
                        params.put("api_token", Constants.api_token);
                        params.put("user_token", Mainsharedprefs.getToken());
                        params.put("lat",lat);
                        params.put("lon", lon);
                        params.put("page", "1");
                        params.put("gender", "all");
                        presenter.getNearby(params);
                    }
                }

                @Override
                public void onPositionChanged() {

                }

                @Override
                public void locationCancelled() {

                }
            });

        }

        tvFilter.setOnClickListener(view1 -> {
            AlertDialog dialog = CommonMethods.createCustomDialog(getContext(),R.layout.custom_filter_dialog);

            tvMale = dialog.findViewById(R.id.tvMale);
            tvFemale = dialog.findViewById(R.id.tvFemale);
            tvBoth = dialog.findViewById(R.id.tvBoth);

            tvMale.setOnClickListener(view2 -> {
                lat =  String.valueOf(easyWayLocation.getLatitude());
                lon = String.valueOf(easyWayLocation.getLongitude());
                HashMap<String,String> params = new HashMap<>();
                params.put("api_token", Constants.api_token);
                params.put("user_token", Mainsharedprefs.getToken());
                params.put("lat",lat);
                params.put("lon", lon);
                params.put("page", "1");
                params.put("gender", "male");
                presenter.getNearby(params);
                dialog.dismiss();
            });

            tvFemale.setOnClickListener(view2 -> {
                lat =  String.valueOf(easyWayLocation.getLatitude());
                lon = String.valueOf(easyWayLocation.getLongitude());
                HashMap<String,String> params = new HashMap<>();
                params.put("api_token", Constants.api_token);
                params.put("user_token", Mainsharedprefs.getToken());
                params.put("lat",lat);
                params.put("lon", lon);
                params.put("page", "1");
                params.put("gender", "female");
                presenter.getNearby(params);
                dialog.dismiss();
            });

            tvBoth.setOnClickListener(view2 -> {
                lat =  String.valueOf(easyWayLocation.getLatitude());
                lon = String.valueOf(easyWayLocation.getLongitude());
                HashMap<String,String> params = new HashMap<>();
                params.put("api_token", Constants.api_token);
                params.put("user_token", Mainsharedprefs.getToken());
                params.put("lat",lat);
                params.put("lon", lon);
                params.put("page", "1");
                params.put("gender", "all");
                presenter.getNearby(params);
                dialog.dismiss();
            });
        });
        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

    }

    @Override
    public void onPause() {
        easyWayLocation.endUpdates();
        super.onPause();
    }

    @NonNull
    @Override
    public NearbyPresenter createPresenter() {
        return new NearbyPresenter(getContext(),this);
    }

    @Override
    public void publishNearby(List<NearbyModel.Content> content) {
        NearbyAdapter adapter = new NearbyAdapter(getContext(), content, item -> {

        });
        rvNearby.setAdapter(adapter);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(),2,RecyclerView.VERTICAL,false);
        rvNearby.setLayoutManager(layoutManager);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 111) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                easyWayLocation.beginUpdates();
            }
        }
    }
}