package ru.noman23.magentatranslator.ui.TranslatesRecyclerView;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.support.v7.widget.helper.ItemTouchHelper.Callback;
import android.view.View;

import butterknife.BindColor;
import butterknife.BindDimen;
import butterknife.ButterKnife;
import ru.noman23.magentatranslator.R;

import static android.support.v7.widget.helper.ItemTouchHelper.LEFT;
import static android.support.v7.widget.helper.ItemTouchHelper.RIGHT;

public class TranslateViewSwipeController extends Callback {

    @BindDimen(R.dimen.item_main_swipe_icon_size) int mIconSize;
    @BindDimen(R.dimen.item_main_swipe_icon_margin) int mIconMargin;

    @BindColor(R.color.white) int mWhiteColor;

    private Activity mActivity;
    private OnSwipeListener mOnSwipeListener;

    private int mSwipeFlags;
    private int mSwipeDirections;

    private int mLeftColor;
    private int mRightColor;

    private Drawable mLeftDrawable;
    private Drawable mRightDrawable;

    public TranslateViewSwipeController(Activity activity,
                                        OnSwipeListener onSwipeListener,
                                        int swipeFlags,
                                        int swipeDirections,
                                        int leftColor,
                                        int rightColor,
                                        Drawable leftDrawable,
                                        Drawable rightDrawable) {
        ButterKnife.bind(this, activity);
        this.mActivity = activity;
        this.mOnSwipeListener = onSwipeListener;
        this.mSwipeFlags = swipeFlags;
        this.mSwipeDirections = swipeDirections;
        this.mLeftColor = leftColor;
        this.mRightColor = rightColor;
        this.mLeftDrawable = leftDrawable;
        this.mRightDrawable = rightDrawable;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        return makeMovementFlags(mSwipeFlags, mSwipeDirections);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }

    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {

            View itemView = viewHolder.itemView;
            Paint p = new Paint();

            if (dX > 0) {
                if ((mSwipeDirections & RIGHT) != 0) {
                    p.setColor(mRightColor);
                    c.drawRect((float) itemView.getLeft(), (float) itemView.getTop(), dX, (float) itemView.getBottom(), p);

                    if (mRightDrawable != null) {
                        mRightDrawable.setBounds(itemView.getLeft() + mIconMargin, itemView.getTop() + (itemView.getBottom() - itemView.getTop() - mIconSize) / 2, itemView.getLeft() + mIconMargin + mIconSize, itemView.getBottom() - (itemView.getBottom() - itemView.getTop() - mIconSize) / 2);
                        mRightDrawable.setColorFilter(new PorterDuffColorFilter(mWhiteColor, PorterDuff.Mode.SRC_IN));
                        mRightDrawable.draw(c);
                    }
                }
            } else {
                if ((mSwipeDirections & LEFT) != 0) {
                    p.setColor(mLeftColor);
                    c.drawRect((float) itemView.getRight() + dX, (float) itemView.getTop(), (float) itemView.getRight(), (float) itemView.getBottom(), p);

                    if (mLeftDrawable != null) {
                        mLeftDrawable.setBounds(itemView.getRight() - mIconSize - mIconMargin, itemView.getTop() + (itemView.getBottom() - itemView.getTop() - mIconSize) / 2, itemView.getRight() - mIconMargin, itemView.getBottom() - (itemView.getBottom() - itemView.getTop() - mIconSize) / 2);
                        mLeftDrawable.setColorFilter(new PorterDuffColorFilter(mWhiteColor, PorterDuff.Mode.SRC_IN));
                        mLeftDrawable.draw(c);
                    }
                }
            }

            viewHolder.itemView.setTranslationX(dX);
        } else {
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        if (mOnSwipeListener != null) {
            mOnSwipeListener.onSwiped(viewHolder, direction);
        }
    }
}
