package com.github.florent37.viewanimator;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ValueAnimator;

import android.support.annotation.IntDef;
import android.support.annotation.IntRange;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Interpolator;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by florentchampigny on 22/12/2015.
 * Modified by gzu-liyujiang on 24/01/2016.
 */
public class ViewAnimator {
    public static final int RESTART = ValueAnimator.RESTART;
    public static final int REVERSE = ValueAnimator.REVERSE;
    public static final int INFINITE = ValueAnimator.INFINITE;
    private static final long DEFAULT_DURATION = 3000;

    private List<AnimationBuilder> animationList = new ArrayList<>();
    private long duration = DEFAULT_DURATION;
    private long startDelay = 0;
    private Interpolator interpolator = null;

    private int repeatCount = 0;
    private int repeatMode = RESTART;

    private AnimatorSet animatorSet;
    private View waitForThisViewHeight = null;

    private AnimationListener.Start startListener;
    private AnimationListener.Stop stopListener;

    private ViewAnimator prev = null;
    private ViewAnimator next = null;

    @IntDef(flag = false, value = {RESTART, REVERSE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface RepeatMode {
        // use this instead enum, I heard that the enumeration will take a lot of memory
    }

    public static AnimationBuilder animate(View... view) {
        ViewAnimator viewAnimator = new ViewAnimator();
        return viewAnimator.addAnimationBuilder(view);
    }

    public AnimationBuilder thenAnimate(View... views) {
        ViewAnimator nextViewAnimator = new ViewAnimator();
        this.next = nextViewAnimator;
        nextViewAnimator.prev = this;
        return nextViewAnimator.addAnimationBuilder(views);
    }

    public AnimationBuilder addAnimationBuilder(View... views) {
        AnimationBuilder animationBuilder = new AnimationBuilder(this, views);
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

        for (Animator animator : animators) {
            if (animator instanceof ValueAnimator) {
                ValueAnimator valueAnimator = (ValueAnimator) animator;
                valueAnimator.setRepeatCount(repeatCount);
                valueAnimator.setRepeatMode(repeatMode);
            }
        }

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(animators);

        animatorSet.setDuration(duration);
        animatorSet.setStartDelay(startDelay);
        if (interpolator != null)
            animatorSet.setInterpolator(interpolator);

        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                if (startListener != null) startListener.onStart();
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (stopListener != null) stopListener.onStop();
                if (next != null) {
                    next.prev = null;
                    next.start();
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        return animatorSet;
    }

    public void start() {
        if (prev != null) {
            prev.start();
        } else {
            animatorSet = createAnimatorSet();

            if (waitForThisViewHeight != null) {
                waitForThisViewHeight.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {
                        animatorSet.start();
                        waitForThisViewHeight.getViewTreeObserver().removeOnPreDrawListener(this);
                        return false;
                    }
                });
            } else {
                animatorSet.start();
            }
        }
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

    public ViewAnimator duration(@IntRange(from = 1) long duration) {
        this.duration = duration;
        return this;
    }

    public ViewAnimator startDelay(@IntRange(from = 0) long startDelay) {
        this.startDelay = startDelay;
        return this;
    }

    /**
     * -1 or ValueAnimator.INFINITE will repeat forever
     *
     * @param repeatCount the repeat count
     * @return the view animation
     */
    public ViewAnimator repeatCount(@IntRange(from = -1) int repeatCount) {
        this.repeatCount = repeatCount;
        return this;
    }

    /**
     * ValueAnimator.RESTART or ValueAnimator.REVERSE
     *
     * @param repeatMode the repeat mode
     * @return the view animation
     */
    public ViewAnimator repeatMode(@RepeatMode int repeatMode) {
        this.repeatMode = repeatMode;
        return this;
    }

    public ViewAnimator onStart(AnimationListener.Start startListener) {
        this.startListener = startListener;
        return this;
    }

    public ViewAnimator onStop(AnimationListener.Stop stopListener) {
        this.stopListener = stopListener;
        return this;
    }

    /**
     * see https://github.com/cimi-chen/EaseInterpolator
     *
     * @param interpolator the interpolator
     * @return the view animator
     */
    public ViewAnimator interpolator(Interpolator interpolator) {
        this.interpolator = interpolator;
        return this;
    }

}
