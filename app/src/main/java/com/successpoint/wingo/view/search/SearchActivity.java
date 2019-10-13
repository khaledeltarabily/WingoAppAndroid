package com.successpoint.wingo.view.search;

import android.content.Intent;
import android.os.Bundle;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hannesdorfmann.mosby3.mvp.MvpActivity;
import com.successpoint.wingo.R;
import com.successpoint.wingo.model.FollowingModel;
import com.successpoint.wingo.view.search.adapter.FollowingAdapter;
import com.successpoint.wingo.view.searchresult.SearchResultActivity;

public class SearchActivity extends MvpActivity<SearchView, SearchPresenter> implements SearchView {

    private EditText etSearchId;
    private TextView cancel;
    private LinearLayout lScan;
    private LinearLayout lFB;
    private LinearLayout lContacts;
    private RecyclerView rv;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        etSearchId = findViewById(R.id.etSearchId);
        cancel = findViewById(R.id.cancel);
        lScan = findViewById(R.id.lScan);
        lFB = findViewById(R.id.lFB);
        lContacts = findViewById(R.id.lContacts);
        rv = findViewById(R.id.rv);

        presenter.getFollows();

        cancel.setOnClickListener(view -> {
            if (etSearchId.getText().toString().length() > 0)
                etSearchId.setText("");
        });

        etSearchId.setOnEditorActionListener((textView, i, keyEvent) -> {
            if (i == EditorInfo.IME_ACTION_SEARCH) {
                if (etSearchId.getText().toString().length() > 0)
                    startActivity(new Intent(SearchActivity.this, SearchResultActivity.class).putExtra("quary", etSearchId.getText().toString()));
                return true;
            }
            return false;
        });
    }

    @NonNull
    @Override
    public SearchPresenter createPresenter() {
        return new SearchPresenter(this, this);
    }

    @Override
    public void publishFollowing(FollowingModel response) {
        FollowingAdapter followingAdapter = new FollowingAdapter(this, response.getContent(), item -> {
        });
        rv.setAdapter(followingAdapter);
        rv.setLayoutManager(new LinearLayoutManager(this));
        followingAdapter.notifyDataSetChanged();
    }
}