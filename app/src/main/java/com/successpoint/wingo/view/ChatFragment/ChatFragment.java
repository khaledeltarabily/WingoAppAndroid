package com.successpoint.wingo.view.ChatFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hannesdorfmann.mosby3.mvp.MvpFragment;
import com.successpoint.wingo.R;
import com.successpoint.wingo.model.MainChats;
import java.util.ArrayList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ChatFragment extends MvpFragment<ChatView, ChatPresenter> implements ChatView {
    View view;
    RecyclerView recyclerView;
    MainChatsRecycler adapter;
    ArrayList<MainChats> List_Data;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.list_view_data, container, false);
            List_Data = new ArrayList<>();
            recyclerView=(RecyclerView)view.findViewById(R.id.recycler);
            recyclerView.setNestedScrollingEnabled(true);
            recyclerView.setHasFixedSize(true);
            LinearLayoutManager layoutManager2 = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(layoutManager2);
            adapter = new MainChatsRecycler(getContext(),List_Data);
            recyclerView.setAdapter(adapter);

            presenter.getAllChatsFirst();

        }
        return view;
    }

    @Override
    public ChatPresenter createPresenter() {
        return new ChatPresenter(getContext(),this);
    }

    @Override
    public void GetAllChatsDoneFirst(ArrayList<MainChats> object) {
        List_Data.addAll(object);
        adapter.notifyDataSetChanged();
    }
}