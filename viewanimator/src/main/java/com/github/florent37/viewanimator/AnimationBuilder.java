package com.github.florent37.viewanimator;

import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.TextView;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.ArgbEvaluator;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.view.ViewHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by florentchampigny on 22/12/2015.
 */
public class AnimationBuilder {

    private final ViewAnimator viewAnimator;
    private final View[] views;
    private final List<Animator> animatorList = new ArrayList<>();

    protected boolean waitForHeight;
    protected boolean nextValueWillBeDp = false;

    public AnimationBuilder(ViewAnimator viewAnimator, View...views) {
        this.viewAnimator = viewAnimator;
        this.views = views;
    }

    public AnimationBuilder dp() {
        nextValueWillBeDp = true;
        return this;
    }

    protected AnimationBuilder add(Animator animator) {
        this.animatorList.add(animator);
        return this;
    }

    protected float toDp(final float px) {
        return px / views[0].getContext().getResources().getDisplayMetrics().density;
    }

    protected float toPx(final float dp) {
        return dp * views[0].getContext().getResources().getDisplayMetrics().density;
    }

    protected float[] getValues(float...values) {
        if (!nextValueWillBeDp)
            return values;

        float[] pxValues = new float[values.length];
        for (int i = 0; i < values.length; ++i) {
            pxValues[i] = toPx(values[i]);
        }
        return pxValues;
    }

    public AnimationBuilder property(String propertyName, float... values) {
        for(View view : views) {
            this.animatorList.add(ObjectAnimator.ofFloat(view, propertyName, getValues(values)));
        }
        return this;
    }

    public AnimationBuilder translationY(float... y) {
        return property("translationY", y);
    }

    public AnimationBuilder translationX(float... x) {
        return property("translationX", x);
    }

    public AnimationBuilder alpha(float... alpha) {
        return property("alpha", alpha);
    }

    public AnimationBuilder scaleX(float... scaleX) {
        return property("scaleX", scaleX);
    }

    public AnimationBuilder scaleY(float... scaleY) {
        return property("scaleY", scaleY);
    }

    public AnimationBuilder scale(float... scale) {
        scaleX(scale);
        scaleY(scale);
        return this;
    }

    public AnimationBuilder pivotX(float pivotX) {
        for(View view : views) {
            ViewHelper.setPivotX(view, pivotX);
        }
        return this;
    }

    public AnimationBuilder pivotY(float pivotY) {
        for(View view : views) {
            ViewHelper.setPivotY(view, pivotY);
        }
        return this;
    }

    public AnimationBuilder rotationX(float... rotationX) {
        return property("rotationX", rotationX);
    }

    public AnimationBuilder rotationY(float... rotationY) {
        return property("rotationY", rotationY);
    }

    public AnimationBuilder rotation(float... rotation) {
        return property("rotation", rotation);
    }

    public AnimationBuilder backgroundColor(int... colors) {
        for(View view : views) {
            ObjectAnimator objectAnimator = ObjectAnimator.ofInt(view, "backgroundColor", colors);
            objectAnimator.setEvaluator(new ArgbEvaluator());
            this.animatorList.add(objectAnimator);
        }
        return this;
    }

    public AnimationBuilder textColor(int... colors) {
        for(View view : views) {
            if (view instanceof TextView) {
                ObjectAnimator objectAnimator = ObjectAnimator.ofInt(view, "textColor", colors);
                objectAnimator.setEvaluator(new ArgbEvaluator());
                this.animatorList.add(objectAnimator);
            }
        }
        return this;
    }

    public AnimationBuilder custom(final AnimationListener.Update update, float... values) {
        for(final View view : views) {
            ValueAnimator valueAnimator = ValueAnimator.ofFloat(getValues((values)));
            if (update != null)
                valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override public void onAnimationUpdate(ValueAnimator animation) {
                        update.update(view, (Float) animation.getAnimatedValue());
                    }
                });
            add(valueAnimator);
        }
        return this;
    }

    public AnimationBuilder height(float... height) {
        return custom(new AnimationListener.Update() {
            @Override public void update(View view, float value) {
                view.getLayoutParams().height = (int) value;
                view.requestLayout();
            }
        }, height);
    }

    public AnimationBuilder width(float... width) {
        return custom(new AnimationListener.Update() {
            @Override public void update(View view, float value) {
                view.getLayoutParams().width = (int) value;
                view.requestLayout();
            }
        }, width);
    }

    public AnimationBuilder waitForHeight() {
        waitForHeight = true;
        return this;
    }

    protected List<Animator> createAnimators() {
        return animatorList;
    }

    //region Animate New View
    public AnimationBuilder andAnimate(View...views) {
        return viewAnimator.addAnimationBuilder(views);
    }

    public AnimationBuilder thenAnimate(View...views) {
        return viewAnimator.thenAnimate(views);
    }
    //endregion

    //region Back to ViewAnimator

    public ViewAnimator start() {
        return viewAnimator.start();
    }

    public ViewAnimator duration(long duration) {
        return viewAnimator.duration(duration);
    }

    public ViewAnimator startDelay(long startDelay) {
        return viewAnimator.startDelay(startDelay);
    }

    //region callbacks

    public ViewAnimator onStart(AnimationListener.Start start) {
        return viewAnimator.onStart(start);
    }

    public ViewAnimator onStop(AnimationListener.Stop stop) {
        return viewAnimator.onStop(stop);
    }

    public ViewAnimator onEnd(AnimationListener.Stop stop) {
        return onStop(stop);
    }

    //endregion

    //region interpolator

    public ViewAnimator interpolator(Interpolator interpolator) {
        return viewAnimator.interpolator(interpolator);
    }

    public ViewAnimator accelerate() {
        return viewAnimator.interpolator(new AccelerateInterpolator());
    }

    public ViewAnimator descelerate() {
        return viewAnimator.interpolator(new DecelerateInterpolator());
    }

    //endregion

    //endregion

    public View[] getViews() {
        return views;
    }
    public View getView() {
        return views[0];
    }

    public boolean isWaitForHeight() {
        return waitForHeight;
    }
}
