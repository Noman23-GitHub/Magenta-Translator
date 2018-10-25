package ru.noman23.magentatranslator.ui.TranslatesRecyclerView;

import android.animation.Animator;

/**
 * Интерфейс используемый для возможности опускать реализацию ненужных методов. С этим интерфейсом можно реализовывать например только onAnimationEnd
 */
public interface DefaultAnimatorListener extends Animator.AnimatorListener {
    @Override
    default void onAnimationStart(Animator animation, boolean isReverse) {

    }

    @Override
    default void onAnimationEnd(Animator animation, boolean isReverse) {

    }

    @Override
    default void onAnimationStart(Animator animation) {

    }

    @Override
    default void onAnimationEnd(Animator animation) {

    }

    @Override
    default void onAnimationCancel(Animator animation) {

    }

    @Override
    default void onAnimationRepeat(Animator animation) {

    }
}
