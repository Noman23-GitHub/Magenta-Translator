package ru.noman23.magentatranslator.activities.main;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;

import com.google.gson.Gson;

import javax.inject.Inject;

import ru.noman23.magentatranslator.DaggerMagentaComponent;
import ru.noman23.magentatranslator.R;
import ru.noman23.magentatranslator.database.tables.TranslatesDaoWrapper;
import ru.noman23.magentatranslator.network.YandexDictionaryApi;
import ru.noman23.magentatranslator.network.translate.TranslateEntity;
import ru.noman23.magentatranslator.ui.TranslatesRecyclerView.TranslateRecyclerViewAdapter;

import static ru.noman23.magentatranslator.database.tables.TranslatesDao.TYPE_HISTORY;

public class TranslateAsyncTask extends AsyncTask<Void, Void, TranslateEntity> {

    // TODO ТУТ СОВСЕМ БАРДАК, надо все переделать

    // FIXME field leaks
    private MainActivity activity;
    private TranslateRecyclerViewAdapter adapter;

    private String text;
    private String lang;

    @Inject TranslatesDaoWrapper database;
    @Inject YandexDictionaryApi yaApi;
    @Inject Gson gson;

    public TranslateAsyncTask(MainActivity activity, TranslateRecyclerViewAdapter adapter, String text, String lang) {
        DaggerMagentaComponent.builder().context(activity).build().inject(this);
        this.activity = activity;
        this.text = text;
        this.lang = lang;
        this.adapter = adapter;
    }

    private ProgressDialog progress;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progress = new ProgressDialog(activity);
        progress.setTitle(activity.getString(R.string.dialog_progress_title));
        progress.setMessage(activity.getString(R.string.dialog_progress_message));
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
            Snackbar.make(activity.findViewById(R.id.recycler_view), activity.getString(R.string.snackbar_error) + ". " + e.getLocalizedMessage(), Snackbar.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        // FIXME Плохой костыль, нужно по-человечески сделать
        TranslateEntity error = new TranslateEntity();
        error.setId(-1);
        return error;
    }

    @Override
    protected void onPostExecute(TranslateEntity result) {
        super.onPostExecute(result);
        progress.dismiss();
        if (result != null && result.getId() != -1) {
            Snackbar.make(activity.findViewById(R.id.recycler_view), R.string.snackbar_completed, Snackbar.LENGTH_SHORT).show();
            adapter.clear();
            adapter.addItem(result);
            activity.loadRecent();
        } else if (result == null) {
            Snackbar.make(activity.findViewById(R.id.recycler_view), R.string.snackbar_not_found, Snackbar.LENGTH_SHORT).show();
        }
    }
}
