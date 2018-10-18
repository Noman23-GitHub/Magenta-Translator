package ru.noman23.magentatranslator.ui.TranslatesRecyclerViewAdapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ru.noman23.magentatranslator.R;
import ru.noman23.magentatranslator.network.translate.TranslateEntity;

class TranslateViewHolder extends RecyclerView.ViewHolder {

    private TranslateEntity translateEntity;

    @BindView(R.id.from) TextView mFromTextView;
    @BindView(R.id.to) TextView mToTextView;
    @BindView(R.id.spoiler) RelativeLayout mSpoilerView;
    @BindView(R.id.spoiler_container) LinearLayout mSpoilerContainerView;

    private boolean mIsViewExpanded = false;

    public TranslateViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    @OnClick(R.id.list_item)
    void onSpoilerClick(final View view) {
        // TODO сделать ValueAnimator чтобы красивый экспанд/коллапс сделать
        if (!mIsViewExpanded) {
            mSpoilerView.setVisibility(View.VISIBLE);
            mSpoilerView.setEnabled(true);
            mIsViewExpanded = true;
        } else {
            mSpoilerView.setVisibility(View.GONE);
            mSpoilerView.setEnabled(false);
            mIsViewExpanded = false;
        }
    }

    public void setTranslateEntity(TranslateEntity translateEntity) {
        this.translateEntity = translateEntity;
    }
}
