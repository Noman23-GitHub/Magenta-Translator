package ru.noman23.magentatranslator.activities.main;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ru.noman23.magentatranslator.DaggerMagentaComponent;
import ru.noman23.magentatranslator.R;
import ru.noman23.magentatranslator.database.tables.TranslatesDaoWrapper;
import ru.noman23.magentatranslator.network.translate.TranslateEntity;
import ru.noman23.magentatranslator.ui.NavigationUI.NavigationUI;
import ru.noman23.magentatranslator.ui.RecentRecyclerView.RecentRecyclerViewAdapter;
import ru.noman23.magentatranslator.ui.TranslatesRecyclerView.TranslateRecyclerViewAdapter;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.drawer_layout) DrawerLayout mDrawerLayout;
    @BindView(R.id.recycler_view) RecyclerView mRecyclerView;
    @BindView(R.id.to_translate) EditText mToTranslateEditView;
    @BindView(R.id.left_spinner) Spinner mLeftSpinner;
    @BindView(R.id.right_spinner) Spinner mRightSpinner;
    @BindView(R.id.recent_recycler) RecyclerView mRecentRecyclerView;

    @Inject NavigationUI mNavigationUI;
    @Inject TranslatesDaoWrapper mDatabase;

    private List<TranslateEntity> history = new LinkedList<>();
    private List<TranslateEntity> results = new LinkedList<>();
    private TranslateRecyclerViewAdapter mRecyclerAdapter = new TranslateRecyclerViewAdapter(results);
    private RecentRecyclerViewAdapter mRecentRecyclerAdapter = new RecentRecyclerViewAdapter(history);

    // TODO Добавить сохранение выбранного направления перевода в Sh.Pr
    // TODO Сделать чтобы сохранялся последний перевод при onConfigurationChanged
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        DaggerMagentaComponent.builder().context(this).build().inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mNavigationUI.setNavigation(this);
        // Иначе Иконка приложения это ссылка на это активити и у иконки название "Переводчик"
        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle(R.string.activity_main_title);


        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mRecyclerAdapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new MainSwipeConfigurator(this, null).getController());
        itemTouchHelper.attachToRecyclerView(mRecyclerView);

        mRecentRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecentRecyclerView.setAdapter(mRecentRecyclerAdapter);
        loadRecent();
    }

    private final int recentSize = 15;

    public void loadRecent() {
        new Thread(() -> {
            history.clear();
            List<TranslateEntity> historyEnts = mDatabase.getHistoryEnts();
            history.addAll(historyEnts.size() > recentSize ? historyEnts.subList(0, recentSize) : historyEnts);
            runOnUiThread(() -> mRecentRecyclerAdapter.notifyDataSetChanged());
        }).start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        menu.findItem(R.id.action_right).setOnMenuItemClickListener(this::onRightRecentClick);
        return true;
    }

    private boolean onRightRecentClick(MenuItem item) {
        mDrawerLayout.openDrawer(GravityCompat.END);
        return true;
    }

    @OnClick(R.id.direction_swap_button)
    void onDirectionSwapClick(View button) {
        int position = mLeftSpinner.getSelectedItemPosition();
        mLeftSpinner.setSelection(mRightSpinner.getSelectedItemPosition());
        mRightSpinner.setSelection(position);
    }

    @OnClick(R.id.translate_button)
    void onTranslateButtonClick(View button) {
        hideSoftKeyboard(button);
        String text = mToTranslateEditView.getText().toString();
        String lang = getLangDirection(mLeftSpinner.getSelectedItemPosition(), mRightSpinner.getSelectedItemPosition());
        new TranslateAsyncTask(this, mRecyclerAdapter, text, lang).execute();
    }

    private void hideSoftKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
        }
    }

    // TODO С языками можно поступить по-клевому, можно короч делать запрос к яндексу (который getLang) брать эти языки и по ним генерировать вот это вот все.
    private String getLangDirection(int leftPosition, int rightPosition) {
        return getShortLangName(leftPosition) + "-" + getShortLangName(rightPosition);
    }
    private String getShortLangName(int position) {
        // Ну что это такое D:
        switch (position) {
            case 0:
                return "ru";
            case 1:
                return "en";
            case 2:
                return "de";
            case 3:
                return "es";
        }
        return "ru";
    }
}
