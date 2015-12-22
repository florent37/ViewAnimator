package com.github.florent37.viewanimator;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Interpolator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by florentchampigny on 22/12/2015.
 */
public class ViewAnimator {

    List<AnimationBuilder> animationList = new ArrayList<>();
    Long duration = null;
    Long startDelay = null;
    Interpolator interpolator = null;

    AnimatorSet animatorSet;
    View waitForThisViewHeight = null;

    AnimationListener.Start startListener;
    AnimationListener.Stop stopListener;

    ViewAnimator next = null;

    public static AnimationBuilder animate(View view) {
        ViewAnimator viewAnimator = new ViewAnimator();
        return viewAnimator.addAnimationBuilder(view);
    }

    public static ViewAnimator queue(ViewAnimator first, ViewAnimator... viewAnimators) {
        if (first != null) {
            if (viewAnimators.length > 0) {
                first.next = viewAnimators[0];
                for (int i = 0; i < viewAnimators.length - 1; ++i) {
                    viewAnimators[i].next = viewAnimators[i + 1];
                }
            }
            return first.start();
        } else return null;
    }

    public AnimationBuilder addAnimationBuilder(View view) {
        AnimationBuilder animationBuilder = new AnimationBuilder(this, view);
        animationList.add(animationBuilder);
        return animationBuilder;
    }

    protected AnimatorSet createAnimatorSet() {
        List<Animator> animators = new ArrayList<>();
        for (AnimationBuilder animationBuilder : animationList) {
            animators.addAll(animationBuilder.createAnimators());
        }

        for (AnimationBuilder animationBuilder : animationList) {
            if (animationBuilder.isWaitForHeight()) {
                waitForThisViewHeight = animationBuilder.getView();
                break;
            }
        }

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(animators);

        if (duration != null)
            animatorSet.setDuration(duration);
        if (startDelay != null)
            animatorSet.setDuration(startDelay);
        if (interpolator != null)
            animatorSet.setInterpolator(interpolator);

        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override public void onAnimationStart(Animator animation) {
                if (startListener != null) startListener.onStart();
            }

            @Override public void onAnimationEnd(Animator animation) {
                if (stopListener != null) stopListener.onStop();
                if (next != null)
                    next.start();
            }

            @Override public void onAnimationCancel(Animator animation) {

            }

            @Override public void onAnimationRepeat(Animator animation) {

            }
        });

        return animatorSet;
    }

    public ViewAnimator start() {
        animatorSet = createAnimatorSet();

        if (waitForThisViewHeight != null)
            waitForThisViewHeight.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override public boolean onPreDraw() {
                    animatorSet.start();
                    waitForThisViewHeight.getViewTreeObserver().removeOnPreDrawListener(this);
                    return false;
                }
            });
        else
            animatorSet.start();
        return this;
    }

    public void cancel() {
        if (animatorSet != null) {
            animatorSet.cancel();
        }
        if (next != null) {
            next.cancel();
            next = null;
        }
    }

    public ViewAnimator duration(long duration) {
        this.duration = duration;
        return this;
    }

    public ViewAnimator startDelay(long startDelay) {
        this.startDelay = startDelay;
        return this;
    }

    //region callbacks

    public ViewAnimator onStart(AnimationListener.Start startListener) {
        this.startListener = startListener;
        return this;
    }

    public ViewAnimator onStop(AnimationListener.Stop stopListener) {
        this.stopListener = stopListener;
        return this;
    }

    //endregion

    //region interpolator

    public ViewAnimator interpolator(Interpolator interpolator) {
        this.interpolator = interpolator;
        return this;
    }

    //endregion

}
