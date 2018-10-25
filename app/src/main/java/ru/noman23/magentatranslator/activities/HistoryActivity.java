package ru.noman23.magentatranslator.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.noman23.magentatranslator.DaggerMagentaComponent;
import ru.noman23.magentatranslator.R;
import ru.noman23.magentatranslator.database.tables.TranslatesDao;
import ru.noman23.magentatranslator.database.tables.TranslatesDaoWrapper;
import ru.noman23.magentatranslator.network.translate.TranslateEntity;
import ru.noman23.magentatranslator.ui.NavigationUI.NavigationUI;
import ru.noman23.magentatranslator.ui.TranslatesRecyclerView.TranslateRecyclerViewAdapter;
import ru.noman23.magentatranslator.ui.TranslatesRecyclerView.TranslateViewSwipeController;

import static android.support.v7.widget.helper.ItemTouchHelper.LEFT;

public class HistoryActivity extends AppCompatActivity {

    @BindView(R.id.recycler_view) RecyclerView mRecyclerView;

    @Inject TranslatesDaoWrapper mDatabase;
    @Inject NavigationUI navigationUI;

    private List<TranslateEntity> history = new LinkedList<>();
    private TranslateRecyclerViewAdapter mRecyclerAdapter = new TranslateRecyclerViewAdapter(history);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        DaggerMagentaComponent.builder().context(this).build().inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        ButterKnife.bind(this);

        navigationUI.setNavigation(this);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mRecyclerAdapter);
        TranslateViewSwipeController swipeController = new TranslateViewSwipeController(this, this::onItemSwipe);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(swipeController);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);

        new Thread(() -> {
            try {
                history.addAll(mDatabase.getHistoryEnts());
                runOnUiThread(() -> mRecyclerAdapter.notifyDataSetChanged());
            } catch (Exception ex) {
                runOnUiThread(() -> Snackbar.make(mRecyclerView, R.string.snackbar_history_error, Snackbar.LENGTH_LONG).show());
                ex.printStackTrace();
            }
        }).start();
    }

    private void onItemSwipe(RecyclerView.ViewHolder viewHolder, int direction) {
        final TranslateEntity swipedItem = mRecyclerAdapter.getTranslateEntities().get(viewHolder.getAdapterPosition());
        final int swipedIndex = viewHolder.getAdapterPosition();

        if (direction == LEFT) {
            new Thread(() -> mDatabase.delete(swipedItem)).start();
            mRecyclerAdapter.removeItem(swipedIndex);

            Snackbar snackbar = Snackbar.make(mRecyclerView, R.string.snackbar_swipe_delete, Snackbar.LENGTH_LONG);
            snackbar.setAction(R.string.snackbar_action_cancel, view -> {
                // FIXME Очевидно, что при ресторе с автоинкрементным ID в бд и сортировкой по ID восстановленных элемент будет первым в списке
                // Добавить дату использования? С текущей архитектурой ничего нормально не заработает :D
                new Thread(() -> mDatabase.insert(swipedItem)).start();
                mRecyclerAdapter.restoreItem(swipedItem, swipedIndex);
            });
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();
        } else {
            new Thread(() -> mDatabase.setFlag(swipedItem.getId(), TranslatesDao.TYPE_SAVED)).start();
            mRecyclerAdapter.saveItem(swipedIndex);

            Snackbar snackbar = Snackbar.make(mRecyclerView, R.string.snackbar_swipe_save, Snackbar.LENGTH_LONG);
            snackbar.setAction(R.string.snackbar_action_cancel, view -> {
                new Thread(() -> mDatabase.removeFlag(swipedItem.getId(), TranslatesDao.TYPE_SAVED)).start();
                mRecyclerAdapter.unsaveItem(swipedIndex);
            });
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();
        }
    }
}
