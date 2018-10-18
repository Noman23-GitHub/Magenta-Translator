package ru.noman23.magentatranslator.network;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class NetworkModule {

    private static final String YANDEX_DICT_API_KEY = "dict.1.1.20181014T075759Z.9d05f6d85393608a.86bbdcb7c80855542fbb3c9309dc6ffe74133c8c";
    private static final String YANDEX_DICT_BASE_URL = "https://dictionary.yandex.net/";

    @Provides
    OkHttpClient providesYandexDictionaryOkHttpClient() {
        return new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    Request original = chain.request();
                    HttpUrl originalHttpUrl = original.url();

                    HttpUrl url = originalHttpUrl.newBuilder()
                            .addQueryParameter("key", YANDEX_DICT_API_KEY)
                            .build();

                    Request.Builder requestBuilder = original.newBuilder()
                            .url(url);

                    Request request = requestBuilder.build();
                    return chain.proceed(request);
                })
                .build();
    }

    @Provides
    Retrofit providesYandexRetrofit(OkHttpClient yandexDictionaryOkHttpClient) {
        return new Retrofit.Builder()
                .client(yandexDictionaryOkHttpClient)
                .baseUrl(YANDEX_DICT_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    @Provides
    @Singleton
    YandexDictionaryApi providesYandexDictionaryApi(Retrofit retrofit) {
        return retrofit.create(YandexDictionaryApi.class);
    }
}
