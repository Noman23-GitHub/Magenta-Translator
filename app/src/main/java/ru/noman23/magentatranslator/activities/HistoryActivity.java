package ru.noman23.magentatranslator.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.noman23.magentatranslator.DaggerMagentaComponent;
import ru.noman23.magentatranslator.R;
import ru.noman23.magentatranslator.database.tables.TranslatesDaoWrapper;
import ru.noman23.magentatranslator.network.translate.TranslateEntity;
import ru.noman23.magentatranslator.ui.NavigationUI.NavigationUI;
import ru.noman23.magentatranslator.ui.TranslatesRecyclerViewAdapter.TranslateRecyclerAdapter;

public class HistoryActivity extends AppCompatActivity {

    @BindView(R.id.recycler_view) RecyclerView mRecyclerView;

    @Inject TranslatesDaoWrapper mDatabase;
    @Inject NavigationUI navigationUI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        DaggerMagentaComponent.builder().context(this).build().inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        ButterKnife.bind(this);

        navigationUI.setNavigation(this);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // TODO Чтобы не делать лишний запрос в бд при OnConfig...Changed  полученный лист можно в instanceState сохранить или вообще через лоадеры сделать скорее всего RxAndroid
        new Thread(() -> {
            List<TranslateEntity> history = mDatabase.getHistoryEnts();
            runOnUiThread(() -> {
                mRecyclerView.setAdapter(new TranslateRecyclerAdapter(history));
                mRecyclerView.getAdapter().notifyDataSetChanged();
            });
        }).start();
    }
}
