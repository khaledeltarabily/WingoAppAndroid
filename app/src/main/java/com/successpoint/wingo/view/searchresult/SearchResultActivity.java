package com.successpoint.wingo.view.searchresult;

import android.os.Bundle;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hannesdorfmann.mosby3.mvp.MvpActivity;
import com.successpoint.wingo.R;
import com.successpoint.wingo.model.SearchResultModel;

public class SearchResultActivity extends MvpActivity<SearchResultView, SearchResultPresenter> implements SearchResultView {

    private EditText etSearchId;
    private TextView cancel;
    private RecyclerView rvResult;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchresult);

        etSearchId = findViewById(R.id.etSearchId);
        cancel = findViewById(R.id.cancel);
        rvResult = findViewById(R.id.rvResult);


        presenter.startSearch(getIntent().getStringExtra("quary"));

        cancel.setOnClickListener(view -> finish());

        etSearchId.setOnEditorActionListener((textView, i, keyEvent) -> {
            if (i == EditorInfo.IME_ACTION_SEARCH) {
                if (etSearchId.getText().toString().length() > 0)
                    presenter.startSearch(etSearchId.getText().toString());
                return true;
            }
            return false;
        });
    }

    @NonNull
    @Override
    public SearchResultPresenter createPresenter() {
        return new SearchResultPresenter(this, this);
    }

    @Override
    public void publishResult(SearchResultModel response) {
        SearchResultAdapter adapter = new SearchResultAdapter(this, response.getContent(), new SearchResultAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(SearchResultModel.Content item) {

            }
        });
        rvResult.setAdapter(adapter);
        rvResult.setLayoutManager(new LinearLayoutManager(this));
        adapter.notifyDataSetChanged();
    }
}