package ru.noman23.magentatranslator;

import android.content.Context;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import ru.noman23.magentatranslator.activities.HistoryActivity;
import ru.noman23.magentatranslator.activities.MainActivity;
import ru.noman23.magentatranslator.activities.SavedActivity;
import ru.noman23.magentatranslator.database.DatabaseModule;
import ru.noman23.magentatranslator.network.NetworkModule;
import ru.noman23.magentatranslator.ui.UIModule;
import ru.noman23.magentatranslator.utils.UtilsModule;

@Singleton
@Component(modules = {UtilsModule.class, DatabaseModule.class, NetworkModule.class, UIModule.class})
public interface MagentaComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder context(Context context);

        MagentaComponent build();
    }

    void inject(TranslateAsyncTask translateAsyncTask);

    //void inject(AppCompatActivity mainActivity);
    void inject(MainActivity mainActivity);

    void inject(HistoryActivity historyActivity);

    void inject(SavedActivity savedActivity);
}