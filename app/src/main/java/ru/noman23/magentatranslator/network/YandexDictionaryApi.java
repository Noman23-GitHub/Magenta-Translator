package ru.noman23.magentatranslator.network;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import ru.noman23.magentatranslator.network.translate.TranslateEntity;

/**
 * Интерфейс Retrofit для для работы с Яндекс.Словарь
 */
public interface YandexDictionaryApi {

    /**
     * Флаг применения семейного фильтра
     */
    int FAMILY = 0x0001;
    /**
     * Флаг обображения частей речи в краткой форме
     */
    int SHORT_POS = 0x0002;     //
    /**
     * Флаг включает поиск по форме слова
     */
    int MORPHO = 0x0004;
    /**
     * Флаг включает фильтр, требующий соответствия частей речи искомого слова и перевода
     */
    int POS_FILTER = 0x0008;

    /**
     * @return Возвращает список поддерживаемых направлений перевода
     */
    @GET("api/v1/dicservice.json/getLangs")
    Call<List<String>> getLangs();

    /**
     * @param text Переводимое слово или словосочетание
     * @param lang Направление перевода вида "en-ru"
     * @return Возвращает POJO хранящий ответ от сервера
     */
    @GET("api/v1/dicservice.json/lookup?ui=ru&flags=0")
    Call<TranslateEntity> lookup(@Query(value = "text", encoded = true) String text, @Query("lang") String lang);

    /**
     * @param text  Переводимое слово или словосочетание
     * @param lang  Направление перевода в виде "en-ru"
     * @param ui    Язык на котором будут получены части речи. "ru" -> "существительное", "en" -> "noun"
     * @param flags Флаги запроса
     * @return Возвращает POJO хранящий ответ от сервера
     */
    @GET("api/v1/dicservice.json/lookup")
    Call<TranslateEntity> lookup(@Query(value = "text", encoded = true) String text, @Query("lang") String lang, @Query("ui") String ui, @Query("flags") int flags);
}
