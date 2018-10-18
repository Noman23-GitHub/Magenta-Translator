package ru.noman23.magentatranslator.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Spinner;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ru.noman23.magentatranslator.DaggerMagentaComponent;
import ru.noman23.magentatranslator.R;
import ru.noman23.magentatranslator.TranslateAsyncTask;
import ru.noman23.magentatranslator.ui.NavigationUI.NavigationUI;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.drawer_layout) DrawerLayout mDrawerLayout;
    @BindView(R.id.recycler_view) RecyclerView mRecyclerView;
    @BindView(R.id.to_translate) EditText mToTranslateEditView;
    @BindView(R.id.left_spinner) Spinner mLeftSpinner;
    @BindView(R.id.right_spinner) Spinner mRightSpinner;

    @Inject NavigationUI mNavigationUI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        DaggerMagentaComponent.builder().context(this).build().inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mNavigationUI.setNavigation(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
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
        new TranslateAsyncTask(this, mRecyclerView, text, lang).execute();
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
