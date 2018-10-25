package ru.noman23.magentatranslator.ui.TranslatesRecyclerView;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ru.noman23.magentatranslator.R;

class TranslateViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.from) TextView mFromTextView;
    @BindView(R.id.to) TextView mToTextView;
    @BindView(R.id.spoiler) RelativeLayout mSpoilerView;
    @BindView(R.id.spoiler_container) LinearLayout mSpoilerContainerView;
    @BindView(R.id.lang_direction) TextView mLangDirectionTextView;

    private boolean mIsViewExpanded = false;

    public TranslateViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    @OnClick(R.id.list_item)
    void onSpoilerClick(final View view) {
        View spoilerView = view.findViewById(R.id.spoiler);
        int widthSpec = View.MeasureSpec.makeMeasureSpec(view.getWidth(), View.MeasureSpec.EXACTLY);
        int heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        spoilerView.measure(widthSpec, heightSpec);
        int spoilerViewHeight = spoilerView.getMeasuredHeight();

        ValueAnimator valueAnimator;
        if (!mIsViewExpanded) {
            valueAnimator = ValueAnimator.ofInt(view.getHeight(), view.getHeight() + spoilerViewHeight);
            mSpoilerView.setVisibility(View.VISIBLE);
            mSpoilerView.setEnabled(true);
            mIsViewExpanded = true;
        } else {
            valueAnimator = ValueAnimator.ofInt(view.getHeight(), view.getHeight() - spoilerViewHeight);
            valueAnimator.addListener(new DefaultAnimatorListener() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mSpoilerView.setVisibility(View.GONE);
                }
            });
            mSpoilerView.setEnabled(false);
            mIsViewExpanded = false;
        }

        valueAnimator.setDuration(200);
        valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        valueAnimator.addUpdateListener(animation -> {
            view.getLayoutParams().height = (Integer) animation.getAnimatedValue();
            view.requestLayout();
        });

        valueAnimator.start();
    }
}
