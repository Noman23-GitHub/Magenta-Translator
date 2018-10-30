package ru.noman23.magentatranslator.utils;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;

import ru.noman23.magentatranslator.ui.TranslatesRecyclerView.OnSwipeListener;
import ru.noman23.magentatranslator.ui.TranslatesRecyclerView.TranslateViewSwipeController;

// TODO: Мб потом еще больше унифицировать и юзать код в других проектах
public class SwipeControllerBuilder {

    private Activity mActivity;
    private OnSwipeListener mOnSwipeListener = null;

    private int mSwipeFlags = 0;
    private int mSwipeDirections = 0;

    private int mLeftColor = Color.TRANSPARENT;
    private int mRightColor = Color.TRANSPARENT;

    private Drawable mLeftDrawable = null;
    private Drawable mRightDrawable = null;

    public SwipeControllerBuilder(Activity activity) {
        this.mActivity = activity;
    }

    public SwipeControllerBuilder setListener(OnSwipeListener onSwipeListener) {
        this.mOnSwipeListener = onSwipeListener;
        return this;
    }

    public SwipeControllerBuilder setFlags(int swipeFlags) {
        this.mSwipeFlags = swipeFlags;
        return this;
    }

    public SwipeControllerBuilder setDirections(int swipeDirections) {
        this.mSwipeDirections = swipeDirections;
        return this;
    }

    public SwipeControllerBuilder setLeftColor(int leftColor) {
        this.mLeftColor = leftColor;
        return this;
    }

    public SwipeControllerBuilder setLeftColorById(@ColorRes int leftColorId) {
        this.mLeftColor = mActivity.getResources().getColor(leftColorId);
        return this;
    }

    public SwipeControllerBuilder setRightColor(int rightColor) {
        this.mRightColor = rightColor;
        return this;
    }

    public SwipeControllerBuilder setRightColorById(@ColorRes int rightColorId) {
        this.mRightColor = mActivity.getResources().getColor(rightColorId);
        return this;
    }

    public SwipeControllerBuilder setRightDrawable(Drawable rightDrawable) {
        this.mRightDrawable = rightDrawable;
        return this;
    }

    public SwipeControllerBuilder setRightDrawableById(@DrawableRes int rightDrawableId) {
        this.mRightDrawable = mActivity.getResources().getDrawable(rightDrawableId);
        return this;
    }

    public SwipeControllerBuilder setLeftDrawable(Drawable leftDrawable) {
        this.mLeftDrawable = leftDrawable;
        return this;
    }

    public SwipeControllerBuilder setLeftDrawableById(@DrawableRes int leftDrawableId) {
        this.mLeftDrawable = mActivity.getResources().getDrawable(leftDrawableId);
        return this;
    }

    public TranslateViewSwipeController build() {
        return new TranslateViewSwipeController(
                mActivity,
                mOnSwipeListener,
                mSwipeFlags,
                mSwipeDirections,
                mLeftColor,
                mRightColor,
                mLeftDrawable,
                mRightDrawable);
    }
}
