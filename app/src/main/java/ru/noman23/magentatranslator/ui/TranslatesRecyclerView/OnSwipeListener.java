package ru.noman23.magentatranslator.ui.TranslatesRecyclerView;

import android.support.v7.widget.RecyclerView;

@FunctionalInterface
public interface OnSwipeListener {
    void onSwiped(RecyclerView.ViewHolder viewHolder, int direction);
}
