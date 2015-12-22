package com.github.florent37.viewanimator;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by florentchampigny on 22/12/2015.
 */
public class AnimationBuilder {

    private final ViewAnimator viewAnimator;
    private final View view;
    private final List<Animator> animatorList = new ArrayList<>();

    protected boolean waitForHeight;
    protected boolean nextValueWillBeDp = false;

    public AnimationBuilder(ViewAnimator viewAnimator, View view) {
        this.viewAnimator = viewAnimator;
        this.view = view;
    }

    public AnimationBuilder dp(){
        nextValueWillBeDp = true;
        return this;
    }

    protected AnimationBuilder add(Animator animator) {
        this.animatorList.add(animator);
        return this;
    }

    public float toDp(final float px) {
        return px / view.getContext().getResources().getDisplayMetrics().density;
    }

    public float toPx(final float dp) {
        return dp * view.getContext().getResources().getDisplayMetrics().density;
    }

    protected float[] getValues(float... values){
        if(nextValueWillBeDp){
            float[] dpValues = new float[values.length];
            for(int i=0;i<values.length;++i){
                dpValues[i] = toDp(values[i]);
            }
            return dpValues;
        }
        else
            return values;
    }

    public AnimationBuilder property(String propertyName, float... values) {
        this.animatorList.add(ObjectAnimator.ofFloat(view, propertyName, getValues(values)));
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

    public AnimationBuilder custom(final AnimationListener.Update update, float... values) {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(getValues((values)));
        if (update != null)
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override public void onAnimationUpdate(ValueAnimator animation) {
                    update.update(view, (Float) animation.getAnimatedValue());
                }
            });
        add(valueAnimator);
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
    public AnimationBuilder animate(View view) {
        return viewAnimator.addAnimationBuilder(view);
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

    public View getView() {
        return view;
    }

    public boolean isWaitForHeight() {
        return waitForHeight;
    }
}
