package ru.noman23.magentatranslator.activities.saved;

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

public class SaveSwipeConfigurator implements SwipeConfigurator {

    private static final int mSwipeFlags = 0;
    private static final int mSwipeDirections = ItemTouchHelper.LEFT;

    @BindColor(R.color.item_red) int mLeftColor;

    @BindDrawable(R.drawable.icon_save_96px) Drawable mLeftDrawable;

    private TranslateViewSwipeController mController;

    public SaveSwipeConfigurator(Activity activity, OnSwipeListener onSwipeListener) {
        ButterKnife.bind(this, activity);
        this.mController = new SwipeControllerBuilder(activity)
                .setListener(onSwipeListener)
                .setFlags(mSwipeFlags)
                .setDirections(mSwipeDirections)
                .setLeftColor(mLeftColor)
                .setLeftDrawable(mLeftDrawable)
                .build();
    }

    public TranslateViewSwipeController getController() {
        return mController;
    }
}
