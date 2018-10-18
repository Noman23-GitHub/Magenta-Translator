package ru.noman23.magentatranslator;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;

import com.google.gson.Gson;

import java.util.LinkedList;

import javax.inject.Inject;

import ru.noman23.magentatranslator.database.tables.TranslatesDaoWrapper;
import ru.noman23.magentatranslator.network.YandexDictionaryApi;
import ru.noman23.magentatranslator.network.translate.TranslateEntity;
import ru.noman23.magentatranslator.ui.TranslatesRecyclerViewAdapter.TranslateRecyclerAdapter;

import static ru.noman23.magentatranslator.database.tables.TranslatesDao.TYPE_HISTORY;

public class TranslateAsyncTask extends AsyncTask<Void, Void, TranslateEntity> {

    // TODO ТУТ СОВСЕМ БАРДАК, надо все переделать

    // FIXME field leaks заменить это все на RxAndroid
    private Activity activity;
    private RecyclerView resultView;

    private String text;
    private String lang;

    @Inject TranslatesDaoWrapper database;
    @Inject YandexDictionaryApi yaApi;
    @Inject Gson gson;

    public TranslateAsyncTask(Activity activity, RecyclerView resultView, String text, String lang) {
        DaggerMagentaComponent.builder().context(activity).build().inject(this);
        this.activity = activity;
        this.text = text;
        this.lang = lang;
        this.resultView = resultView;
    }

    private ProgressDialog progress;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progress = new ProgressDialog(activity);
        // TODO В строковые ресурсы
        progress.setTitle("Загрузка");
        progress.setMessage("Пожалуйста подождите...");
        progress.setCancelable(false);
        progress.show();
    }

    @Override
    protected TranslateEntity doInBackground(Void... params) {
        try {
            TranslateEntity result = database.findEntity(text, lang);
            if (result == null) {
                TranslateEntity response = yaApi.lookup(text, lang).execute().body();
                if (response != null && response.getDef().size() > 0) {
                    long recordId = database.insert(response);
                    response.setId(recordId);
                    response.setSaveType(TYPE_HISTORY);
                    result = response;
                } else {
                    return null;
                }
            }
            return result;
        } catch (Exception e) {
            Snackbar.make(resultView, "Ошибка. " + e.getLocalizedMessage(), Snackbar.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        TranslateEntity error = new TranslateEntity();
        error.setId(-1);
        return error;
    }

    @Override
    protected void onPostExecute(TranslateEntity result) {
        super.onPostExecute(result);
        progress.dismiss();
        if (result != null && result.getId() != -1) {
            Snackbar.make(resultView, "Завершено", Snackbar.LENGTH_SHORT).show();
            resultView.setAdapter(new TranslateRecyclerAdapter(new LinkedList<TranslateEntity>() {{
                add(result);
            }}));
        } else if (result == null) {
            // TODO: Вывести хардкодед строку в стрковые ресурсы
            Snackbar.make(resultView, "Результат не найден", Snackbar.LENGTH_SHORT).show();
        }
    }
}
