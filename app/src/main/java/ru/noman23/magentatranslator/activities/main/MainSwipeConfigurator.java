package ru.noman23.magentatranslator.activities.main;

import android.app.Activity;
import android.graphics.drawable.Drawable;

import butterknife.BindColor;
import butterknife.BindDrawable;
import butterknife.ButterKnife;
import ru.noman23.magentatranslator.R;
import ru.noman23.magentatranslator.ui.TranslatesRecyclerView.OnSwipeListener;
import ru.noman23.magentatranslator.ui.TranslatesRecyclerView.TranslateViewSwipeController;
import ru.noman23.magentatranslator.utils.SwipeConfigurator;
import ru.noman23.magentatranslator.utils.SwipeControllerBuilder;

public class MainSwipeConfigurator implements SwipeConfigurator {

    private static final int mSwipeFlags = 0;

    // FIXME (TAG: SAVE) Отключен свайп для сохранения
    private static final int mSwipeDirections = 0 /*| ItemTouchHelper.RIGHT*/;

    @BindColor(R.color.item_green) int mRightColor;

    @BindDrawable(R.drawable.icon_save_96px) Drawable mRightDrawable;

    private TranslateViewSwipeController mController;

    public MainSwipeConfigurator(Activity activity, OnSwipeListener onSwipeListener) {
        ButterKnife.bind(this, activity);
        this.mController = new SwipeControllerBuilder(activity)
                .setListener(onSwipeListener)
                .setFlags(mSwipeFlags)
                .setDirections(mSwipeDirections)
                .setRightColor(mRightColor)
                .setRightDrawable(mRightDrawable)
                .build();
    }

    public TranslateViewSwipeController getController() {
        return mController;
    }
}
