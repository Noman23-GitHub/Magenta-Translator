package ru.noman23.magentatranslator.activities.history;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.helper.ItemTouchHelper;

import butterknife.BindColor;
import butterknife.BindDrawable;
import butterknife.ButterKnife;
import ru.noman23.magentatranslator.R;
import ru.noman23.magentatranslator.ui.TranslatesRecyclerView.OnSwipeListener;
import ru.noman23.magentatranslator.ui.TranslatesRecyclerView.TranslateViewSwipeController;
import ru.noman23.magentatranslator.utils.SwipeConfigurator;
import ru.noman23.magentatranslator.utils.SwipeControllerBuilder;

public class HistorySwipeConfigurator implements SwipeConfigurator {

    private static final int mSwipeFlags = 0;

    // FIXME (TAG: SAVE) Отключен свайп для сохранения
    private static final int mSwipeDirections = ItemTouchHelper.LEFT /*| ItemTouchHelper.RIGHT*/;

    @BindColor(R.color.item_green) int mRightColor;
    @BindColor(R.color.item_red) int mLeftColor;

    @BindDrawable(R.drawable.icon_save_96px) Drawable mRightDrawable;
    @BindDrawable(R.drawable.icon_delete_96px) Drawable mLeftDrawable;

    private TranslateViewSwipeController mController;

    public HistorySwipeConfigurator(Activity activity, OnSwipeListener onSwipeListener) {
        ButterKnife.bind(this, activity);
        this.mController = new SwipeControllerBuilder(activity)
                .setListener(onSwipeListener)
                .setFlags(mSwipeFlags)
                .setDirections(mSwipeDirections)
                .setLeftColor(mLeftColor)
                .setRightColor(mRightColor)
                .setLeftDrawable(mLeftDrawable)
                .setRightDrawable(mRightDrawable)
                .build();
    }

    public TranslateViewSwipeController getController() {
        return mController;
    }
}
