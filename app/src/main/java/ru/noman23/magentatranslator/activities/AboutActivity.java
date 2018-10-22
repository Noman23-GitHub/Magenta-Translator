package ru.noman23.magentatranslator.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import javax.inject.Inject;

import ru.noman23.magentatranslator.DaggerMagentaComponent;
import ru.noman23.magentatranslator.R;
import ru.noman23.magentatranslator.ui.NavigationUI.NavigationUI;

public class AboutActivity extends AppCompatActivity {

    @Inject NavigationUI navigationUI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        DaggerMagentaComponent.builder().context(this).build().inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        navigationUI.setNavigation(this);
    }
}
