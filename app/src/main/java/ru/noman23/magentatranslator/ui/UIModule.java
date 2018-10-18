package ru.noman23.magentatranslator.ui;

import dagger.Module;
import dagger.Provides;
import ru.noman23.magentatranslator.ui.NavigationUI.NavigationUI;

@Module
public class UIModule {
    @Provides
    NavigationUI providesNavigationUI() {
        return new NavigationUI();
    }
}
