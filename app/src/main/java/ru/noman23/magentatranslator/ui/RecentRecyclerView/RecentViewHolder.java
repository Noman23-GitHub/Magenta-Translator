package ru.noman23.magentatranslator.ui.RecentRecyclerView;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.noman23.magentatranslator.R;

public class RecentViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.from) TextView mFromTextView;
    @BindView(R.id.to) TextView mToTextView;

    public RecentViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
