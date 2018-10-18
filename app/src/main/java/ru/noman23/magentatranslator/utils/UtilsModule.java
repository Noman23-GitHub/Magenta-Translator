package ru.noman23.magentatranslator.utils;

import com.google.gson.Gson;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class UtilsModule {

    @Singleton
    @Provides
    Gson providesGson() {
        return new Gson();
    }
}
