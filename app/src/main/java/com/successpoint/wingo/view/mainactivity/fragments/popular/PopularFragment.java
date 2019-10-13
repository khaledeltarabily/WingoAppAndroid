package com.successpoint.wingo.view.mainactivity.fragments.popular;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hannesdorfmann.mosby3.mvp.MvpFragment;
import com.successpoint.wingo.R;
import com.successpoint.wingo.model.PopularMainModel;
import com.successpoint.wingo.utils.Constants;
import com.successpoint.wingo.utils.Mainsharedprefs;
import com.successpoint.wingo.view.mainactivity.fragments.popular.adapter.PopularAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PopularFragment extends MvpFragment<PopularView, PopularPresenter> implements PopularView {
    private View view;
    private RecyclerView rvPopular;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_popular, container, false);

            rvPopular = view.findViewById(R.id.rvPopular);

            HashMap<String, String> params = new HashMap<>();
            params.put("api_token", Constants.api_token);
            params.put("user_token", Mainsharedprefs.getToken());
            params.put("page", "1");
            params.put("ads", "1");
            params.put("country", "all");
            params.put("gender", "all");
            presenter.getPopularList(params);

        }
        return view;
    }

    @NonNull
    @Override
    public PopularPresenter createPresenter() {
        return new PopularPresenter(getContext(), this);
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
        rvPopular.setAdapter(popularAdapter);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2, RecyclerView.VERTICAL, false);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (position == 0) {
                    return 2;
                } else {
                    return 1;
                }
            }
        });
        rvPopular.setLayoutManager(layoutManager);
        popularAdapter.notifyDataSetChanged();
    }
}