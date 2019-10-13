package com.successpoint.wingo.view.mainactivity.fragments.explore;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.GsonBuilder;
import com.hannesdorfmann.mosby3.mvp.MvpFragment;
import com.successpoint.wingo.R;
import com.successpoint.wingo.model.CountryModel;
import com.successpoint.wingo.model.PopularMainModel;
import com.successpoint.wingo.utils.Constants;
import com.successpoint.wingo.utils.Mainsharedprefs;
import com.successpoint.wingo.view.mainactivity.fragments.explore.adapter.CountriesAdapter;
import com.successpoint.wingo.view.mainactivity.fragments.popular.adapter.PopularAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.successpoint.wingo.utils.CommonMethods.LogMe;

public class ExploreFragment extends MvpFragment<ExploreView, ExplorePresenter> implements ExploreView {

    private View view;
    private RecyclerView rvCountries;
    private RecyclerView rvExplore;
    private List<CountryModel> list = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_explore, container, false);

            rvCountries = view.findViewById(R.id.rvCountries);
            rvExplore = view.findViewById(R.id.rvExplore);

            try {
                JSONArray jsonArray = new JSONArray(loadJSONFromAsset());
                for (int i = 0; i < 6; i++) {
                    LogMe(jsonArray.getJSONObject(i).toString());
                    list.add(new GsonBuilder().create().fromJson(jsonArray.getJSONObject(i).toString(),CountryModel.class));
                }

                CountriesAdapter countriesAdapter = new CountriesAdapter(getContext(), list, item -> {

                });
                GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),3,RecyclerView.VERTICAL,false);
                rvCountries.setLayoutManager(gridLayoutManager);
                rvCountries.setAdapter(countriesAdapter);
                countriesAdapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            HashMap<String, String> params = new HashMap<>();
            params.put("api_token", Constants.api_token);
            params.put("user_token", Mainsharedprefs.getToken());
            params.put("page", "1");
            params.put("ads", "0");
            params.put("country", "all");
            params.put("gender", "all");
            presenter.getHotLiveList(params);
        }
        return view;
    }

    @NonNull
    @Override
    public ExplorePresenter createPresenter() {
        return new ExplorePresenter(getContext(),this);
    }

    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = getActivity().getAssets().open("countries.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    @Override
    public void publishMainData(JSONObject result) {
        List<PopularMainModel> models = new ArrayList<>();

        try {
            for (int i = 0; i < result.getJSONArray("content").length(); i++) {
                JSONObject single = result.getJSONArray("content").getJSONObject(i);
                if (single.getString("type").equals("Stream")) {
                    PopularMainModel popularMainModel = new PopularMainModel(single.getString("type"), single.getString("stream_cover"), null, single.getJSONObject("prodcaster").getString("name"), single.getInt("viewers") + "");
                    models.add(popularMainModel);
                } else {
                    JSONObject singleUrl = result.getJSONArray("content").getJSONObject(i);
                    List<String> urls = new ArrayList<>();
                    for (int j = 0; j < singleUrl.getJSONArray("data").length(); j++) {
                        urls.add(singleUrl.getJSONArray("data").getJSONObject(j).getString("image"));
                    }
                    PopularMainModel popularMainModel = new PopularMainModel(single.getString("type"), null, urls, null, null);
                    models.add(popularMainModel);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        PopularAdapter popularAdapter = new PopularAdapter(getContext(), models, item -> {

        });
        rvExplore.setAdapter(popularAdapter);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2, RecyclerView.VERTICAL, false);
        rvExplore.setLayoutManager(layoutManager);
        popularAdapter.notifyDataSetChanged();
    }
}