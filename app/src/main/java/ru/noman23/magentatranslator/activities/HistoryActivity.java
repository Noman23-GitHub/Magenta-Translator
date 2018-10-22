package ru.noman23.magentatranslator.activities;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.noman23.magentatranslator.DaggerMagentaComponent;
import ru.noman23.magentatranslator.R;
import ru.noman23.magentatranslator.database.tables.TranslatesDaoWrapper;
import ru.noman23.magentatranslator.network.translate.TranslateEntity;
import ru.noman23.magentatranslator.ui.NavigationUI.NavigationUI;
import ru.noman23.magentatranslator.ui.TranslatesRecyclerView.TranslateRecyclerViewAdapter;
import ru.noman23.magentatranslator.ui.TranslatesRecyclerView.TranslateViewSwipeController;

public class HistoryActivity extends AppCompatActivity {

    @BindView(R.id.recycler_view) RecyclerView mRecyclerView;

    @Inject TranslatesDaoWrapper mDatabase;
    @Inject NavigationUI navigationUI;

    private List<TranslateEntity> history = new LinkedList<>();
    private TranslateRecyclerViewAdapter mRecyclerAdapter = new TranslateRecyclerViewAdapter(history);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        DaggerMagentaComponent.builder().context(this).build().inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        ButterKnife.bind(this);

        navigationUI.setNavigation(this);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mRecyclerAdapter);
        TranslateViewSwipeController swipeController = new TranslateViewSwipeController(this, mRecyclerAdapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(swipeController);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);

        new Thread(() -> {
            try {
                history.addAll(mDatabase.getHistoryEnts());
                runOnUiThread(() -> mRecyclerAdapter.notifyDataSetChanged());
            } catch (Exception ex) {
                runOnUiThread(() -> Snackbar.make(mRecyclerView, R.string.snackbar_history_error, Snackbar.LENGTH_LONG).show());
                ex.printStackTrace();
            }
        }).start();
    }
}
