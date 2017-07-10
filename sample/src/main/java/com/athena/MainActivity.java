package com.athena;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.athena.library.data.SearchDataCenter;
import com.athena.library.data.SearchableEntity;
import com.athena.library.data.SortWeight;
import com.athena.library.view.T9KeyBoard;

import java.util.List;

public class MainActivity extends Activity {

    private TextView inputView;
    private RecyclerView recyclerView;
    private T9KeyBoard keyBoard;

    private ContactsAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
    }

    private void initView() {
        inputView = (TextView) findViewById(R.id.tv_input);
        recyclerView = (RecyclerView) findViewById(R.id.recycle_view);
        keyBoard = (T9KeyBoard) findViewById(R.id.t9_keyboard);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        keyBoard.setOnKeyClickListener(new T9KeyBoard.onKeyClickListener() {
            @Override
            public void onResult(String str) {
                inputView.setText(str);
                SearchDataCenter.getInstance().doSearch(str);
            }

            @Override
            public void onResultLastChar(char c) {

            }
        });
    }

    private void initData() {
        ContactsManager.getInstance().initData(getContentResolver());
        List<ContactsEntity> contactsEntityList =  ContactsManager.getInstance().getContactsList();
        adapter = new ContactsAdapter(this);
        recyclerView.setAdapter(adapter);
        SearchDataCenter.getInstance().init(this, new SearchDataCenter.OnSearchCompleteListener() {
            @Override
            public void onComplete(List<SearchableEntity> list) {
                adapter.update(list);
            }
        });
        SearchDataCenter.getInstance().initSearchData(contactsEntityList);
        SearchDataCenter.getInstance().initSortWeight(SortWeight.RANK5, SortWeight.RANK4, SortWeight.RANK3, SortWeight.RANK2, SortWeight.RANK1, 1, 2);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SearchDataCenter.getInstance().destroy();
    }
}
